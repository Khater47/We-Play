package com.example.mad

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Test
import org.junit.runner.RunWith

const val TEXT_DENY = "DENY"
const val TEXT_ALLOW = "ALLOW"

@RunWith(AndroidJUnit4ClassRunner::class)

internal class ShowProfileActivityTestUIAutomator{


    //check run time permission camera
    @Test
    fun test_DialogPermissionCamera(){

        //check if API>=28 and after check run time permission for camera
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                ApplicationProvider.getApplicationContext<Context>().packageName,
                "android.permission.CAMERA"
            )

            val device = UiDevice.getInstance(getInstrumentation())

            //check camera dialog
            assertViewWithTextIsVisible(device, TEXT_ALLOW)
            assertViewWithTextIsVisible(device, TEXT_DENY);

        }
    }

    //check run time permission gallery
    @Test
    fun test_DialogPermissionGallery(){

        //check if API>=28 and after check run time permission for camera
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                ApplicationProvider.getApplicationContext<Context>().packageName,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )

            val device = UiDevice.getInstance(getInstrumentation())

            //check gallery dialog
            assertViewWithTextIsVisible(device, TEXT_ALLOW)
            assertViewWithTextIsVisible(device, TEXT_DENY);

        }
    }

    //assert dialog text = ALLOW/DENY
    fun assertViewWithTextIsVisible(device: UiDevice, text: String) {
        val allowButton: UiObject = device.findObject(UiSelector().text(text))
        if (!allowButton.exists()) {
            throw AssertionError("View with text <$text> not found!")
        }
    }




}