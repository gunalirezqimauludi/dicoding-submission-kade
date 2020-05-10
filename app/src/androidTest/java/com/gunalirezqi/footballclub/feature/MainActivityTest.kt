package com.gunalirezqi.footballclub.feature


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.KeyEvent
import android.widget.EditText
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.feature.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    object EspressoIdlingResource {
        private val RESOURCE = "GLOBAL"
        private val countingIdlingResource = CountingIdlingResource(RESOURCE)

        val idlingresource: IdlingResource
            get() = countingIdlingResource

//        fun increment() {
//            countingIdlingResource.increment()
//        }

//        fun decrement() {
//            countingIdlingResource.decrement()
//        }
    }

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun testSearch() {
        onView(withId(R.id.action_search))
            .check(matches(isDisplayed()))

        onView(withId(R.id.action_search))
            .perform(click())
        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("Arsenal"), pressKey(KeyEvent.KEYCODE_ENTER))
        delay(5)

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
        delay(2)

        onView(withId(R.id.action_search))
            .perform(click())
        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText("Barcelona"), pressKey(KeyEvent.KEYCODE_ENTER))
    }

    @Test
    fun testPlayer() {
        delay(5)
        onView(withText("TEAM"))
            .check(matches(isDisplayed()))

        onView(withText("TEAM")).perform(click())
        delay(5)

        onView(withText("Aldosivi"))
            .check(matches(isDisplayed()))

        onView(withText("Aldosivi")).perform(click())
        delay(5)

        onView(withText("PLAYER"))
            .check(matches(isDisplayed()))

        onView(withText("PLAYER")).perform(click())
        delay(5)

        onView(withText("Joel Acosta"))
            .check(matches(isDisplayed()))

        onView(withText("Joel Acosta")).perform(click())
        delay(5)
    }

    private fun delay(second: Long = 1) {
        Thread.sleep(1000 * second)
    }
}
