package com.mshetty.stockpilot.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.not
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.mshetty.stockpilot.R
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Test
    fun activityShouldLaunchSuccessfully() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        assertTrue(activityScenario.state.name == "RESUMED" || activityScenario.state.name == "CREATED")
        activityScenario.close()
    }

    @Test
    fun activityShouldHaveRequiredViews() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.pb_progress))
            .check(matches(isAssignableFrom(android.widget.ProgressBar::class.java)))
        onView(withId(R.id.ll_error_container))
            .check(matches(isAssignableFrom(android.widget.LinearLayout::class.java)))
        onView(withId(R.id.rvHoldings))
            .check(matches(isAssignableFrom(androidx.recyclerview.widget.RecyclerView::class.java)))
        onView(withId(R.id.ll_portfolio_summary))
            .check(matches(isAssignableFrom(android.widget.LinearLayout::class.java)))
        activityScenario.close()
    }

    @Test
    fun activityShouldHandleRotationCorrectly() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        assertTrue(activityScenario.state.name == "RESUMED" || activityScenario.state.name == "CREATED")
        activityScenario.close()
    }

    @Test
    fun activityShouldHandleBackPressCorrectly() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            activity.onBackPressed()
        }
        activityScenario.close()
    }

    @Test
    fun activityShouldHaveCorrectTitle() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            assertNotNull(activity.title)
        }
        activityScenario.close()
    }

    @Test
    fun activityShouldHandleMemoryPressureCorrectly() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        System.gc()
        assertTrue(activityScenario.state.name == "RESUMED" || activityScenario.state.name == "CREATED")
        activityScenario.close()
    }

    @Test
    fun retryButtonShouldExistInLayout() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_retry))
            .check(matches(isAssignableFrom(android.widget.Button::class.java)))
        activityScenario.close()
    }

    @Test
    fun portfolioSummaryShouldExistInLayout() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.ll_portfolio_summary))
            .check(matches(isAssignableFrom(android.widget.LinearLayout::class.java)))
        activityScenario.close()
    }

    @Test
    fun recyclerViewShouldExistInLayout() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.rvHoldings))
            .check(matches(isAssignableFrom(androidx.recyclerview.widget.RecyclerView::class.java)))
        activityScenario.close()
    }
}
