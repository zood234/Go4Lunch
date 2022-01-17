package com.example.go4lunch

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.go4lunch.activity.ListofRestaurantsActivity
import com.example.go4lunch.activity.MainActivity
import com.example.go4lunch.activity.MapsActivity
import com.example.go4lunch.adapters.AllUsersActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class IntegratedTests {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.go4lunch", appContext.packageName)
    }

    @Test
    fun checkThatMapIsOnByDefault() {
        val activityScenario = ActivityScenario.launch(MapsActivity::class.java)

        Espresso.onView(withId(R.id.mapMapBtn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun allUsersAreDisplayed() {
        val activityScenario = ActivityScenario.launch(AllUsersActivity::class.java)
        Espresso.onView(withId(R.id.allUsersRV))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun allReastrauntsAreDisplayed() {
        val activityScenario = ActivityScenario.launch(ListofRestaurantsActivity::class.java)
        Espresso.onView(withId(R.id.allResaurantsRV))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


}