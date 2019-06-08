package com.prokoshevnik.backend

import android.os.Handler
import android.os.Looper
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.prokoshevnik.top10sample.backend.db.AppDatabase
import com.prokoshevnik.top10sample.backend.db.dao.GoogleSearchDao
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchItem
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchResult
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.Semaphore
import kotlin.random.Random


class DatabaseTester {
    /**
     * In this fun we test:
     *   - if database correctly inserts entities
     *   - if database correctly retrieves entities
     *   - if database correctly retrieves relations between entities
     *   - if conversions from representation to database entities and otherwise works correctly
     */
    @Test
    fun testAllEntities() {
        val random = Random(System.currentTimeMillis())

        testAllImpl(3, random)
        testAllImpl(0, random)
    }

    private fun testAllImpl(itemCount: Int, random: Random) {
        val queryString = TestingUtils.getRandomDbString(random)

        val items = arrayListOf<GoogleSearchItem>().apply {
            for (i in 0 until itemCount) {
                add(GoogleSearchItem().apply {
                    title = TestingUtils.getRandomDbString(random)
                    snippet = TestingUtils.getRandomDbString(random)
                    link = TestingUtils.getRandomDbString(random)
                })
            }
        }

        val googleSearchResult = GoogleSearchResult()
        googleSearchResult.query = queryString
        googleSearchResult.items = items

        val database =
            Room.databaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java, DATABASE_NAME).build()

        GoogleSearchDao.insertResultWithItems(database.googleSearchDao(), googleSearchResult)

        var resultSelected: GoogleSearchResult? = null

        val semaphore = Semaphore(1)
        semaphore.acquire()

        Handler(Looper.getMainLooper()).post {
            database.googleSearchDao().getAllResults(1).observeForever {
                resultSelected = GoogleSearchResult(it.first())

                semaphore.release()
            }
        }

        semaphore.acquire()

        assert(false)

        resultSelected!!
        assertTrue("Query is incorrect", resultSelected!!.query == queryString)
        assertTrue("Item count is incorrect", resultSelected!!.items.size == itemCount)

        resultSelected!!.items.forEachIndexed { index, item ->
            assertTrue("Item title is incorrect", item.title == items[index].title)
            assertTrue("Item snippet is incorrect", item.snippet == items[index].snippet)
            assertTrue("Item link is incorrect", item.link == items[index].link)
        }
    }


    companion object {
        const val DATABASE_NAME = "database-test"
    }
}













