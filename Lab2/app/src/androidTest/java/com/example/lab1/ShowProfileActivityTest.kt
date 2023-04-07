package com.example.lab1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
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
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

const val FILENAME = "userFile"
const val PROFILE = "profile"

@RunWith(AndroidJUnit4ClassRunner::class)
//check UI for Show Profile Activity
internal class ShowProfileActivityTest{

    @get:Rule
    private val activityScenario = ActivityScenario.launch(ShowProfileActivity::class.java)

    companion object{
        @BeforeClass
        fun clearSharedPreferences(){
            val context = ApplicationProvider.getApplicationContext<Context>()
            val sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

            if(sharedPreferences.getString(PROFILE,null)!=null)
                sharedPreferences.edit().remove(PROFILE).commit()

            var user = JSONObject()

            user.put("fullName",context.resources.getString(R.string.fullName))
            user.put("nickname",context.resources.getString(R.string.nickname))
            user.put("description",context.resources.getString(R.string.description))
            user.put("email",context.resources.getString(R.string.email))
            user.put("phoneNumber",context.resources.getString(R.string.phoneNumber))

            val editor = sharedPreferences.edit()

            editor.putString(PROFILE,user.toString())

            editor.apply()

        }

        @BeforeClass
        fun clearLocalStorage(){
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val files: Array<File> = context.filesDir.listFiles()
            if (files != null) {
                for (file in files) {
                    file.delete()
                }
            }
        }

        @BeforeClass
        fun saveImageOnLocalStorage(){
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            val directory = context.filesDir
            val imageFile = File(directory, context.getString(R.string.imageName))

            val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.ic_launcher_background)

            val outputStream = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

        }


    }


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

        onView(withId(R.id.avatar_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.avatar_user_profile)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.full_name_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.full_name_user_profile)).check(matches(withText(R.string.fullName)))

        onView(withId(R.id.email_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.email_user_profile)).check(matches(withText(R.string.email)))

    }

    @Test
    //load show profile activity and check if Linear Layout contains personal info like (email,phoneNumber,nickname)
    fun test_isProfileInfoContainerInView(){
        onView(withId(R.id.containerInfoUserProfile)).check(matches(isDisplayed()))

        onView(withId(R.id.user_info)).check(matches(isDisplayed()))
        onView(withId(R.id.user_info)).check(matches(withText(R.string.personalInfo)))

        onView(withId(R.id.nickname_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.nickname_user_profile)).check(matches(withText(R.string.labelNickname)))

        onView(withId(R.id.custom_nickname_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.custom_nickname_user_profile)).check(matches(withText(R.string.nickname)))


        onView(withId(R.id.phone_number_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.phone_number_user_profile)).check(matches(withText(R.string.labelPhoneNumber)))

        onView(withId(R.id.custom_phone_number_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.custom_phone_number_user_profile)).check(matches(withText(R.string.phoneNumber)))


        onView(withId(R.id.description_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.description_user_profile)).check(matches(withText(R.string.labelDescription)))

        onView(withId(R.id.custom_description_user_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.custom_description_user_profile)).check(matches(withText(R.string.description)))

    }


    @Test
    //check edit button click -> load Edit Profile Activity
    fun test_editProfileButton(){

        onView(withId(R.id.edit_button)).check(matches(isDisplayed()))

        onView(withId(R.id.edit_button)).perform(click())

        onView(withId(R.id.containerEditProfileActivity)).check(matches(isDisplayed()))

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

                assertNotNull(user.get("fullName"))
                assertTrue(user.get("fullName") is String)

                assertNotNull(user.get("nickname"))
                assertTrue(user.get("nickname") is String)

                assertNotNull(user.get("email"))
                assertTrue(user.get("email") is String)

                assertNotNull(user.get("description"))
                assertTrue(user.get("description") is String)

                assertNotNull(user.get("phoneNumber"))
                assertTrue(user.get("phoneNumber") is String)

            }

        }


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