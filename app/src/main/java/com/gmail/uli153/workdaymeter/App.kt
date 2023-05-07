/**
 * Created by Ulises on 24/4/23.
 */
package com.gmail.uli153.workdaymeter

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
    }
}