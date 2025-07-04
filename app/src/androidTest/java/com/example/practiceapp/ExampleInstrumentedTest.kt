package com.example.practiceapp

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practiceapp.aa.TestActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CounterTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Test
    fun counter_initial_value_is_zero() {
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.countertext)).assertTextEquals("0")
    }

    @Test
    fun clickButton_incrementsCounter() {
        composeTestRule.onNodeWithText("increment").performClick()
        composeTestRule.onNodeWithText("1").assertExists()
    }

}