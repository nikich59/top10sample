package com.prokoshevnik.top10sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prokoshevnik.top10sample.MainActivity
import com.prokoshevnik.top10sample.R
import com.prokoshevnik.top10sample.adapter.QueryResultViewAdapter
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
import com.prokoshevnik.top10sample.entity.GoogleSearchHeader
import kotlinx.android.synthetic.main.search_layout.*


class SearchFragment : Fragment() {
    private lateinit var resultViewAdapter: QueryResultViewAdapter
    private lateinit var resultViewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_layout, container, false)

        view.findViewById<Button>(R.id.searchView_searchButton).setOnClickListener {
            performQuery(searchView_searchButton)
        }

        resultViewManager = LinearLayoutManager(requireContext())
        resultViewAdapter = QueryResultViewAdapter(listOf())

        view.findViewById<RecyclerView>(R.id.searchView_searchResultView).layoutManager = resultViewManager
        view.findViewById<RecyclerView>(R.id.searchView_searchResultView).adapter = resultViewAdapter

        return view
    }

    private val searchResultsObserver = Observer<List<GoogleSearchResult>> {
        if (it != null) {
            setResultList(it)
        }
    }

    override fun onResume() {
        (requireActivity() as MainActivity).searchViewModel.allResults
            .observe(this, searchResultsObserver)

        super.onResume()
    }

    override fun onStop() {
        (requireActivity() as MainActivity).searchViewModel.removeObservers(this)

        super.onStop()
    }

    private fun performQuery(view: View?) {
        /*
        onQueryResultReady(Response.success(GoogleSearchResult().apply {
            query = "Query${Random().nextInt(1000)}"

            val items = arrayListOf<GoogleSearchItem>()

            for (i in 1 until Random().nextInt(15) + 5) {
                items.add(GoogleSearchItem().apply {
                    link =
                        "http://links.govdelivery.com/track?type=click&enid=ZWFzPTEmbXNpZD0mYXVpZD0mbWFpbGluZ2lkPTIwMTgxMjE3Ljk5MTcyNjExJm1lc3NhZ2VpZD1NREItUFJELUJVTC0yMDE4MTIxNy45OTE3MjYxMSZkYXRhYmFzZWlkPTEwMDEmc2VyaWFsPTE3MzQ0MTc4JmVtYWlsaWQ9bGNhbXBiZWxsQHR5bGVycGFwZXIuY29tJnVzZXJpZD1sY2FtcGJlbGxAdHlsZXJwYXBlci5jb20mdGFyZ2V0aWQ9JmZsPSZtdmlkPSZleHRyYT0mJiY=&&&102&&&http://www.trinityrailwayexpress.org/events-destinations/"
                    title = "Events & Destinations – Trinity Railway Express Bla Bla Bla"
                    snippet =
                        "Apr 23, 2019 ... TRE Airport Travel Service. Linking downtown Dallas Union Station or downtown \n" +
                                "Fort Worth's T&P Station with CentrePort/DFW Airport Station ..."
                })
            }

            this.items = items
        }))

        return
        */

        val queryString = getQueryString()

        if (queryString.isEmpty()) {
            onQueryStringEmpty()

            return
        }

        (requireActivity() as MainActivity).searchViewModel.performSearch(
            queryString,
            (requireActivity() as MainActivity)::onGoogleSearchFailure
        )
    }

    private fun onQueryStringEmpty() {
        Toast.makeText(requireContext(), R.string.searchQueryStringEmptyMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setResultList(results: List<GoogleSearchResult>) {
        searchView_searchResultView
            .adapter = QueryResultViewAdapter(arrayListOf<Any>().apply {
            results.forEach { searchResult ->
                add(GoogleSearchHeader(searchResult.query))

                searchResult.items.forEach { searchItem ->
                    add(searchItem)
                }
            }
        })

    }

    private fun getQueryString() = searchView_queryEditText.text.toString()
}










