/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gmail.uli153.workdaymeter.MainActivity
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.util.*
import kotlin.coroutines.CoroutineContext

class ChronometerService: LifecycleService() {

    companion object {
        const val ACTION_GET_IN = "ACTION_GET_IN"
        const val ACTION_GET_OUT = "ACTION_GET_OUT"

        private val _time: MutableLiveData<Long> = MutableLiveData(0L)
        val time: LiveData<Long> = _time

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "chronometer_cannel"
        private const val NOTIFICATION_CHANNEL_NAME = "Chronometer"

        private const val UPDATE_NOTIFICATION_DELAY = 1000L
    }

    private val pendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        Intent(applicationContext, MainActivity::class.java),
        if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Running App")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

    private var firstRun = true
    private var lastRecordTime: Long? = null
    private var timerJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        time.observe(this) {
            val notification = notificationBuilder.setContentText(it.formattedTime)
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_GET_IN -> {
                if (firstRun) {
                    firstRun = false
                    startForegroundService()
                } else {
                    startTimer()
                }
            }
            ACTION_GET_OUT -> {
                stopTimer()
            }
            null -> Unit
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        startTimer()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(UPDATE_NOTIFICATION_DELAY)
                val lastTime = lastRecordTime ?: continue
                val current = Date().time
                val diff = current - lastTime
                if (!isActive) return@launch
                _time.value = diff
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _time.value = 0L
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}