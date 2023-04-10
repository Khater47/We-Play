package com.example.mad

import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
internal class EditProfileActivityLandTest {

    @get:Rule
    private val activityScenario = ActivityScenario.launch(EditProfileActivity::class.java)

    @BeforeAll
    fun loadLandscape(){
        activityScenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

    }

    @Test
    //load edit profile activity in Landscape and check the content of toolbar
    fun test_isToolbarInViewLand(){

        onView(ViewMatchers.withId(R.id.toolbarShowProfile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.arrowBackUserProfile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.arrowBackUserProfile))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(ViewMatchers.withId(R.id.toolbarTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.toolbarTitle))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.toolbarEditProfile)))

    }



    @Test
    //load edit profile activity in Landscape and check the containers for root,profile picture and text edit
    fun test_isContainerInViewLand(){
        onView(ViewMatchers.withId(R.id.containerEditProfileActivityLand))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.scrollViewEditProfileActivityLand))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.containerScrollViewEditProfileActivityLand))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.containerEditProfilePictureLand))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.containerEditTextEditProfileActivityLand))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    //load edit profile activity and check the profile picture and image button
    fun test_isProfilePictureInViewLand(){
        activityScenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        onView(ViewMatchers.withId(R.id.userProfilePicture))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.userProfilePicture))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(ViewMatchers.withId(R.id.imageButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.imageButton))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    }


    @Test
    //load edit profile activity and check the edit text for fullName,nickname,email,phoneNumber,description
    fun test_isEditTextInViewLand(){
        activityScenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        onView(ViewMatchers.withId(R.id.editFullName))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.editFullName))
            .perform(ViewActions.click()).perform(ViewActions.typeText("Mario Rossi"))
        onView(ViewMatchers.withId(R.id.editFullName))
            .check(ViewAssertions.matches(ViewMatchers.withText("Mario Rossi")))


        onView(ViewMatchers.withId(R.id.editNickname))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.editNickname))
            .perform(ViewActions.click()).perform(ViewActions.typeText("mario"))
        onView(ViewMatchers.withId(R.id.editNickname))
            .check(ViewAssertions.matches(ViewMatchers.withText("mario")))


        onView(ViewMatchers.withId(R.id.editEmail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.editEmail))
            .perform(ViewActions.scrollTo()).perform(ViewActions.click()).perform(
            ViewActions.typeText("mariorossi@gmail.com")
        )
        onView(ViewMatchers.withId(R.id.editEmail))
            .check(ViewAssertions.matches(ViewMatchers.withText("mariorossi@gmail.com")))

        onView(ViewMatchers.withId(R.id.editPhoneNumber))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.editPhoneNumber))
            .perform(ViewActions.scrollTo()).perform(ViewActions.click()).perform(
            ViewActions.typeText("1234567890")
        )
        onView(ViewMatchers.withId(R.id.editPhoneNumber))
            .check(ViewAssertions.matches(ViewMatchers.withText("1234567890")))

        onView(ViewMatchers.withId(R.id.editDescription))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.editDescription))
            .perform(ViewActions.scrollTo()).perform(ViewActions.click()).perform(
            ViewActions.typeText("student, location")
        )
        onView(ViewMatchers.withId(R.id.editDescription))
            .check(ViewAssertions.matches(ViewMatchers.withText("student, location")))
    }

}