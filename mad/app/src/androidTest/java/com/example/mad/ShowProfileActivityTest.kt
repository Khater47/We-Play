package com.example.mad

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

const val FILENAME = "userFile"
const val PROFILE = "profile"

@RunWith(AndroidJUnit4ClassRunner::class)
//check UI for Show Profile Activity
internal class ShowProfileActivityTest{

    @get:Rule
    private val activityScenario = ActivityScenario.launch(ShowProfileActivity::class.java)

    //----------------------------
    //PORTRAIT
    //----------------------------

    @Test
    //load show profile activity and check if scrollView and LinearLayout are showed
    fun test_isActivityInView(){

        onView(withId(R.id.scrollShowProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.contentScrollView)).check(matches(isDisplayed()))

    }
    @Test
    //load show profile activity and check if Constraint Layout contains profilePicture,FullName and Email
    fun test_isProfilePictureContainerInView(){
        onView(withId(R.id.containerProfilePicture)).check(matches(isDisplayed()))

        onView(withId(R.id.userProfilePicture)).check(matches(isDisplayed()))
        onView(withId(R.id.userProfilePicture)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.fullNameUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.emailUserProfile)).check(matches(isDisplayed()))

    }

    @Test
    //load show profile activity and check if Linear Layout contains personal info like (email,phoneNumber,nickname)
    fun test_isProfileInfoContainerInView(){
        onView(withId(R.id.containerInfoUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.userInfo)).check(matches(isDisplayed()))
        onView(withId(R.id.userInfo)).check(matches(withText(R.string.personalInfo)))

        onView(withId(R.id.nicknameUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.customNicknameUserProfile)).check(matches(isDisplayed()))


        onView(withId(R.id.phoneNumberUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.customPhoneNumberUserProfile)).check(matches(isDisplayed()))


        onView(withId(R.id.descriptionUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.customDescriptionUserProfile)).check(matches(isDisplayed()))

    }

    @Test
    //check edit button click -> load Edit Profile Activity
    fun test_editProfileButton(){

        onView(withId(R.id.editButton)).check(matches(isDisplayed()))

        onView(withId(R.id.editButton)).perform(click())

        onView(withId(R.id.containerEditProfileActivity)).check(matches(isDisplayed()))

    }


    //----------------------------
    //LANDSCAPE
    //----------------------------

    @Test
    //load show profile activity and check if scrollView and LinearLayout are showed
    fun test_isActivityInViewLand(){
        activityScenario.onActivity {
           it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onView(withId(R.id.scrollShowProfileLand)).check(matches(isDisplayed()))

        onView(withId(R.id.contentScrollViewLand)).check(matches(isDisplayed()))

    }

    @Test
    //load show profile activity in Landscape and check if Constraint Layout contains profilePicture,FullName and Email
    fun test_isProfilePictureContainerInViewLand(){
        activityScenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onView(withId(R.id.containerProfilePictureLand)).check(matches(isDisplayed()))

        onView(withId(R.id.userProfilePicture)).check(matches(isDisplayed()))
        onView(withId(R.id.userProfilePicture)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.fullNameUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.emailUserProfile)).check(matches(isDisplayed()))

    }

    @Test
    //load show profile activity in Landscape and check if Linear Layout contains personal info like (email,phoneNumber,nickname)
    fun test_isProfileInfoContainerInViewLand(){
        activityScenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onView(withId(R.id.containerInfoUserProfileLand)).check(matches(isDisplayed()))

        onView(withId(R.id.userInfo)).check(matches(isDisplayed()))
        onView(withId(R.id.userInfo)).check(matches(withText(R.string.personalInfo)))

        onView(withId(R.id.nicknameUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.customNicknameUserProfile)).check(matches(isDisplayed()))


        onView(withId(R.id.phoneNumberUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.customPhoneNumberUserProfile)).check(matches(isDisplayed()))


        onView(withId(R.id.descriptionUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.customDescriptionUserProfile)).check(matches(isDisplayed()))

    }


    //----------------------------
    //Check sharePreferences
    //----------------------------

    @Before
    fun addUserProfileOnSharedPreferences(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

        if(sharedPreferences.getString(PROFILE,null)!=null)
            sharedPreferences.edit().remove(PROFILE).commit()

        val user = JSONObject()

        user.put("fullName",context.resources.getString(R.string.fullName))
        user.put("nickname",context.resources.getString(R.string.nickname))
        user.put("description",context.resources.getString(R.string.description))
        user.put("email",context.resources.getString(R.string.email))
        user.put("phoneNumber",context.resources.getString(R.string.phoneNumber))

        val editor = sharedPreferences.edit()

        editor.putString(PROFILE,user.toString())

        editor.apply()

    }

    @Test
    //check JSON Object profile in sharedPreferences
    fun userObject_isCorrect() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        val sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

        assertNotNull(sharedPreferences)

        //check sharePreferences and PROFILE object
        if(sharedPreferences!=null){

            val userString = sharedPreferences.getString(PROFILE,null)

            assertNotNull(userString)

            if(userString!=null){
                val user = JSONObject(userString)

                assertNotNull(user.getString("fullName"))
                assertTrue(user.getString("fullName") is String)

                assertNotNull(user.getString("nickname"))
                assertTrue(user.getString("nickname") is String)

                assertNotNull(user.getString("email"))
                assertTrue(user.getString("email") is String)

                assertNotNull(user.getString("description"))
                assertTrue(user.getString("description") is String)

                assertNotNull(user.getString("phoneNumber"))
                assertTrue(user.getString("phoneNumber") is String)

            }

        }


    }

    //----------------------------
    //Check image on local storage
    //----------------------------

    @Before
    fun deleteImageOnLocalStorage(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val directory = context.filesDir

        val files: Array<File>? = directory.listFiles()
        if (files != null) {
            for (file in files) {
                file.delete()
            }
        }

    }

    @Before
    fun saveImageOnLocalStorage(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val directory = context.filesDir
        val imageFile = File(directory, context.getString(R.string.imageName))

        val bitmap: Bitmap? = ContextCompat.getDrawable(context,R.drawable.ic_launcher_background)?.toBitmap()

        val outputStream = FileOutputStream(imageFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()


    }

    @Test
    fun imageIsOnLocalStorage() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val fileName = context.getString(R.string.imageName)
        val directory = context.filesDir

        val imageFile = File(directory, fileName)

        assertNotNull(imageFile)
        assertTrue(imageFile.exists())

    }



}