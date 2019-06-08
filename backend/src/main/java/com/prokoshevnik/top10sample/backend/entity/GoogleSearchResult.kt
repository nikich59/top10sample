package com.prokoshevnik.top10sample.backend.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchResultWithItems

class GoogleSearchResult() {
    @SerializedName("items")
    @Expose
    var items: List<GoogleSearchItem> = listOf()

    var query: String = ""

    constructor(resultInDb: GoogleSearchResultWithItems) : this() {
        query = resultInDb.searchResult?.query ?: ""
        items = arrayListOf<GoogleSearchItem>().apply {
            resultInDb.items?.forEach {
                add(GoogleSearchItem(it))
            }
        }.toList()
    }
}

