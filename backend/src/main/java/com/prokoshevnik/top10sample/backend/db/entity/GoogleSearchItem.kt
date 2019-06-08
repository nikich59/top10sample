package com.prokoshevnik.top10sample.backend.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "google_search_items")
class GoogleSearchItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "search_result_id")
    var searchResultId: Long = -1L

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "snippet")
    var snippet: String = ""

    @ColumnInfo(name = "link")
    var link: String = ""
}
