package com.pratyaksh.healthykingdom

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import android.util.Log
import com.pratyaksh.healthykingdom.utils.NotificationChannelsId
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HealthyingdomApp: Application(){

    override fun onCreate() {
        super.onCreate()

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                NotificationChannelsId.AMBULANCE_LIVE_LOC.name,
                "Location Sharing",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notifManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notifManager.createNotificationChannel(mChannel)
            Log.d("onAppCreate", "Created Notification channel")

        }

    }

}