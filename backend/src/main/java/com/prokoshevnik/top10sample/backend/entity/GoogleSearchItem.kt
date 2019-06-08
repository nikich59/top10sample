package com.prokoshevnik.top10sample.backend.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchItem


class GoogleSearchItem() {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("snippet")
    @Expose
    var snippet: String? = null

    constructor(resultItemInDb: GoogleSearchItem) : this() {
        title = resultItemInDb.title
        snippet = resultItemInDb.snippet
        link = resultItemInDb.link
    }
}







