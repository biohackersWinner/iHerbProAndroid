package ru.biohackers.iherb.android

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IHerbApp : Application(){

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}