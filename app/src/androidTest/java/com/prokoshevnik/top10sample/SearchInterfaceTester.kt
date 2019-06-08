package com.prokoshevnik.top10sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class SearchInterfaceTester {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSearch() {
        val queryString = System.currentTimeMillis().toString()

        onView(withId(R.id.searchView_queryEditText))
            .perform(typeText(queryString))
        onView(withId(R.id.searchView_searchButton))
            .perform(click())

        Thread.sleep(4000)


        onView(withText("\"" + queryString + "\""))
            .check(matches(isDisplayed()))
    }
}















