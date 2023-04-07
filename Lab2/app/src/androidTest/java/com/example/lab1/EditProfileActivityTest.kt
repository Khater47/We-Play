package com.example.lab1

import android.R.attr.bitmap
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.hardware.biometrics.BiometricManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import kotlin.math.exp


const val KEY_IMAGE_DATA="data"

@RunWith(AndroidJUnit4ClassRunner::class)
//check UI for Edit Profile Activity 
internal class EditProfileActivityTest{

    @get:Rule
    private val activityScenario = ActivityScenario.launch(EditProfileActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsTestRule(EditProfileActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    @Test
    //load edit profile activity and check the content of toolbar
    fun test_isToolbarInView(){
        onView(withId(R.id.toolbar_show_profile)).check(matches(isDisplayed()))

        onView(withId(R.id.arrow_back_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.arrow_back_user_profile)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //descriptionShowProfile
        onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar_title)).check(matches(withText(R.string.toolbarEditProfile)))

    }

    @Test
    //load edit profile activity and check the containers for root,profile picture and text edit
    fun test_isContainerInView(){
        onView(withId(R.id.containerEditProfileActivity)).check(matches(isDisplayed()))

        onView(withId(R.id.scrollViewEditProfileActivity)).check(matches(isDisplayed()))

        onView(withId(R.id.containerScrollViewEditProfileActivity)).check(matches(isDisplayed()))

        onView(withId(R.id.containerEditProfilePicture)).check(matches(isDisplayed()))

        onView(withId(R.id.containerEditTextEditProfileActivity)).check(matches(isDisplayed()))
    }

    @Test
    //load edit profile activity and check the profile picture and image button
    fun test_isProfilePictureInView(){
        onView(withId(R.id.avatar_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.avatar_user_profile)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.image_button)).check(matches(isDisplayed()))
        onView(withId(R.id.image_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

    }


    @Test
    //load edit profile activity and check the edit text for fullName,nickname,email,phoneNumber,description
    fun test_isEditTextInView(){

        onView(withId(R.id.edit_full_name)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_full_name)).perform(typeText("Mario Rossi"),click())
        onView(withId(R.id.edit_full_name)).check(matches(withText("Mario Rossi")))


        onView(withId(R.id.edit_nickname)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_nickname)).perform(typeText("mario"),click())
        onView(withId(R.id.edit_nickname)).check(matches(withText("mario")))


        onView(withId(R.id.edit_email)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_email)).perform(typeText("mariorossi@gmail.com"),click())
        onView(withId(R.id.edit_email)).check(matches(withText("mariorossi@gmail.com")))

        onView(withId(R.id.edit_phoneNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_phoneNumber)).perform(typeText("1234567890"),click())
        onView(withId(R.id.edit_phoneNumber)).check(matches(withText("1234567890")))

        onView(withId(R.id.edit_description)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_description)).perform(typeText("student, location"),click())
        onView(withId(R.id.edit_description)).check(matches(withText("student, location")))
    }

    @Test
    //check arrow back button click -> load Show Profile Activity
    fun test_arrowBackButton(){

        onView(withId(R.id.arrow_back_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.arrow_back_user_profile)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.arrow_back_user_profile)).perform(click())

        onView(withId(R.id.scrollShowProfile)).check(matches(isDisplayed()))

    }

    @Test
    fun test_GalleryIntent(){
        val mimeTypes = arrayOf("image/*")
        val expectedIntent = allOf(
            hasAction(Intent.ACTION_PICK),
            hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            hasExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        )

        onView(withId(R.id.image_button)).perform(longClick())

        onView(withText(R.string.gallery)).perform(click())
        intended(expectedIntent)
    }

    @Test
    fun test_CameraIntent(){

        val activityResult = createImageCaptureActivityResultStub()
        val expectedIntent = hasAction(MediaStore.ACTION_IMAGE_CAPTURE)

        intending(expectedIntent).respondWith(activityResult)

        onView(withId(R.id.image_button)).perform(longClick())

        onView(withText(R.string.camera)).perform(click())
        intended(expectedIntent)

    }

    private fun createImageCaptureActivityResultStub():Instrumentation.ActivityResult{
        val bundle = Bundle()
        bundle.putParcelable(
            KEY_IMAGE_DATA,
            BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().context.resources,
                R.drawable.ic_launcher_background
            )
        )
        val resultData = Intent()
        resultData.putExtras(bundle)
        return Instrumentation.ActivityResult(RESULT_OK,resultData)
    }

}


