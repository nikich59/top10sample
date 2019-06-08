package com.prokoshevnik.top10sample.backend.db.entity

import androidx.room.Embedded
import androidx.room.Relation

class GoogleSearchResultWithItems {
    @Embedded
    var searchResult: GoogleSearchResult? = null

    @Relation(parentColumn = "id", entityColumn = "search_result_id")
    var items: List<GoogleSearchItem>? = null
}
