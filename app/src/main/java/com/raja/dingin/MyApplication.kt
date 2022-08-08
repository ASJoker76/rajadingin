package com.raja.dingin

import android.app.Application
import com.squareup.picasso.Picasso

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            setupPicasso()
        } catch (e: IllegalStateException) {

        }
    }

    private fun setupPicasso() {
        Picasso.setSingletonInstance(Picasso.Builder(applicationContext).build())
    }
}