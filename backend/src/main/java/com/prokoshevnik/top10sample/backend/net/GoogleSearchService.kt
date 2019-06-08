package com.prokoshevnik.top10sample.backend.net

import com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleSearchService {
    @GET("v1")
    fun search(
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("cx") cx: String
    ): Call<GoogleSearchResult>
}

