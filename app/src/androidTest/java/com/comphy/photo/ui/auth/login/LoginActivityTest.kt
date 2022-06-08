package com.comphy.photo.ui.auth.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.comphy.photo.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    private val dummyEmail = "DummyEmail@mail.com"
    private val dummyPassword = "DummyPassword000"

    @Before
    fun setup() {
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun assertGetCircumference() {
        onView(withId(R.id.txtHello)).check(matches(isDisplayed()))
        onView(withId(R.id.txtWelcome)).check(matches(isDisplayed()))
        onView(withId(R.id.txtNotHaveAccount)).check(matches(isDisplayed()))
        onView(withId(R.id.txtLoginTo)).check(matches(isDisplayed()))
        onView(withId(R.id.txtComphy)).check(matches(isDisplayed()))
        onView(withId(R.id.txtOr)).check(matches(isDisplayed()))
        onView(withId(R.id.txtYourEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.txtYourPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnForgotPassword)).check(matches(isDisplayed()))

        onView(withId(R.id.edtEmail)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.edtPassword)).perform(typeText(dummyPassword), closeSoftKeyboard())

        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))

        onView(withId(R.id.btnGoogleLogin)).check(matches(isDisplayed()))

        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).perform(click())
    }

}