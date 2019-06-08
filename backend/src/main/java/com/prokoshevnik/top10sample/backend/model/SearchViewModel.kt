package com.prokoshevnik.top10sample.backend.model


import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.prokoshevnik.top10sample.backend.db.AppDatabase
import com.prokoshevnik.top10sample.backend.db.dao.GoogleSearchDao
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
import com.prokoshevnik.top10sample.backend.net.GoogleSearchConstants.BASE_SEARCH_URL
import com.prokoshevnik.top10sample.backend.net.GoogleSearchConstants.GOOGLE_SEARCH_API_KEY
import com.prokoshevnik.top10sample.backend.net.GoogleSearchConstants.GOOGLE_SEARCH_CX
import com.prokoshevnik.top10sample.backend.net.GoogleSearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel : ViewModel() {
    private lateinit var context: Context
    val allResults = MutableLiveData<List<GoogleSearchResult>>()
    private var resultCountLimit = 5


    fun requestInit(context: Context, runAfter: () -> Unit) {
        this.context = context

        Thread {
            initAllResults()

            Handler(Looper.getMainLooper()).post {
                runAfter.invoke()
            }
        }.start()
    }

    private fun initAllResults() {
        val resultsFromDb = AppDatabase.getInstance(context).googleSearchDao().getAllResults(resultCountLimit)

        Handler(Looper.getMainLooper()).post {
            resultsFromDb.observeForever { listInDb ->
                allResults.value = arrayListOf<GoogleSearchResult>().apply {
                    listInDb?.forEach {
                        add(GoogleSearchResult(it))
                    }
                }.toList()
            }
        }
    }

    fun requestResultInsert(searchResult: GoogleSearchResult) {
        Thread {
            val lastResult = GoogleSearchDao.insertResultWithItems(
                AppDatabase.getInstance(context).googleSearchDao(), searchResult
            )

            Handler(Looper.getMainLooper()).post {
                allResults.value = arrayListOf(GoogleSearchResult(lastResult)).apply {
                    for (item in allResults.value!!) {
                        if (size >= resultCountLimit) {
                            break
                        }

                        add(item)
                    }
                }
            }
        }.start()
    }

    private var searchCall: Call<GoogleSearchResult>? = null
    private lateinit var lastQuery: String

    fun performSearch(
        queryString: String,
        onFailure: (call: Call<GoogleSearchResult>, t: Throwable) -> Unit
    ) {
        searchCall?.cancel()

        lastQuery = queryString

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        val searchService = retrofit.create(GoogleSearchService::class.java)

        searchCall = searchService.search(
            queryString,
            GOOGLE_SEARCH_API_KEY,
            GOOGLE_SEARCH_CX
        )

        searchCall?.enqueue(object : Callback<GoogleSearchResult> {
            override fun onFailure(call: Call<GoogleSearchResult>, t: Throwable) {
                onFailure.invoke(call, t)
            }

            override fun onResponse(call: Call<GoogleSearchResult>, response: Response<GoogleSearchResult>) {
                onQueryResultReady(response)
            }
        })
    }

    private fun onQueryResultReady(response: Response<GoogleSearchResult>) {
        val responseBody = response.body() ?: GoogleSearchResult().apply {
            items = listOf()
        }

        responseBody.query = lastQuery

        requestResultInsert(responseBody)
    }

    fun removeObservers(owner: LifecycleOwner) {
        allResults.removeObservers(owner)
    }
}







