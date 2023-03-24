package com.example.lab1

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        // Load the appropriate layout file based on the device orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main)
        } else {
            setContentView(R.layout.activity_main)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Reload the appropriate layout file when the device orientation changes
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main)
        } else {
            setContentView(R.layout.activity_main)
        }
    }


    
}