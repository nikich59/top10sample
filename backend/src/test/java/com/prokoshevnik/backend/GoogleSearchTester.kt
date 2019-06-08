package com.prokoshevnik.backend

import com.google.gson.GsonBuilder
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
import com.prokoshevnik.top10sample.backend.net.GoogleSearchConstants
import com.prokoshevnik.top10sample.backend.net.GoogleSearchService
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Semaphore

class GoogleSearchTester {
    /**
     * In this fun we test if google search works by assuming that query string is contained by any of 10 search results
     */
    @Test
    fun testSearch() {
        val queryString = "John Doe"

        val retrofit = Retrofit.Builder()
            .baseUrl(GoogleSearchConstants.BASE_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        val searchService = retrofit.create(GoogleSearchService::class.java)

        val call = searchService.search(
            queryString,
            GoogleSearchConstants.GOOGLE_SEARCH_API_KEY,
            GoogleSearchConstants.GOOGLE_SEARCH_CX
        )

        var throwable: Throwable? = null
        var searchResult: GoogleSearchResult? = null
        val semaphore = Semaphore(1)
        semaphore.acquire()

        call.enqueue(object : Callback<GoogleSearchResult> {
            override fun onFailure(call: Call<GoogleSearchResult>, t: Throwable) {
                throwable = t

                semaphore.release()
            }

            override fun onResponse(call: Call<GoogleSearchResult>, response: Response<GoogleSearchResult>) {
                searchResult = response.body()

                semaphore.release()
            }
        })

        semaphore.acquire()

        if (throwable != null) {
            throw throwable!!
        }

        searchResult!!
        assertTrue(
            "Query string \"$queryString\" is not found in any result",
            searchResult!!.items.fold(false) { isFound, item ->
                isFound || item.title!!.toLowerCase().contains(queryString.toLowerCase())
            }
        )
    }
}












