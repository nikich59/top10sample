package com.prokoshevnik.top10sample.backend.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchItem
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchResult
import com.prokoshevnik.top10sample.backend.db.entity.GoogleSearchResultWithItems

@Dao
interface GoogleSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(searchResult: GoogleSearchResult): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResultItems(searchResults: List<GoogleSearchItem>): List<Long>

    @Query("SELECT * FROM google_search_results ORDER BY id DESC LIMIT :limit")
    fun getAllResults(limit: Int): LiveData<List<GoogleSearchResultWithItems>>

    @Query("SELECT * FROM google_search_items WHERE search_result_id = :search_result_id")
    fun getSearchItemsForResult(search_result_id: Long): List<GoogleSearchItem>

    companion object {
        fun insertResultWithItems(
            dao: GoogleSearchDao,
            searchResult: com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
        ): GoogleSearchResultWithItems {
            val searchResultInDb = GoogleSearchResult()
            searchResultInDb.query = searchResult.query
            searchResultInDb.id = dao.insertResult(searchResultInDb)

            val searchItemsInDb = arrayListOf<GoogleSearchItem>()
            for (item in searchResult.items) {
                searchItemsInDb.add(
                    GoogleSearchItem().apply {
                        link = item.link ?: ""
                        title = item.title ?: ""
                        snippet = item.snippet ?: ""

                        searchResultId = searchResultInDb.id
                    }
                )
            }

            dao.insertResultItems(searchItemsInDb).forEachIndexed { index, id ->
                searchItemsInDb[index].id = id
            }

            return GoogleSearchResultWithItems().apply {
                this.searchResult = searchResultInDb
                this.items = searchItemsInDb
            }
        }
    }
}










