package com.prokoshevnik.top10sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
import com.prokoshevnik.top10sample.backend.model.SearchViewModel
import retrofit2.Call

class MainActivity : AppCompatActivity() {
    lateinit var searchViewModel: SearchViewModel
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.requestInit(this) {}
    }

    fun onGoogleSearchFailure(call: Call<GoogleSearchResult>, t: Throwable) {
        Toast.makeText(
            this,
            R.string.searchQueryFailureText,
            Toast.LENGTH_SHORT
        ).show()
    }
}
