/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gmail.uli153.workdaymeter.MainActivity
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.use_cases.GetStateUseCase
import com.gmail.uli153.workdaymeter.domain.use_cases.ToggleStateUseCase
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChronometerService: LifecycleService() {

    companion object {
        const val ACTION_TOGGLE_STATE = "ACTION_TOGGLE_STATE"

        private val _time: MutableLiveData<Long> = MutableLiveData(0L)
        val time: LiveData<Long> = _time

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "chronometer_cannel"
        private const val NOTIFICATION_CHANNEL_NAME = "Chronometer"

        private const val UPDATE_NOTIFICATION_DELAY = 1000L
    }

    @Inject lateinit var toggleStateUseCase: ToggleStateUseCase
    @Inject lateinit var getStateUseCase: GetStateUseCase

    private val flags = if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT

    private val mainPendingIntent: PendingIntent by lazy {
        val intent = Intent(applicationContext, MainActivity::class.java)
        PendingIntent.getActivity(applicationContext, 0, intent, flags)
    }

    private val togglePendingIntent: PendingIntent by lazy {
        val intent = Intent(applicationContext, ChronometerService::class.java)
        intent.action = ACTION_TOGGLE_STATE
        PendingIntent.getService(applicationContext, 0, intent, flags)
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(mainPendingIntent)
            .setContentTitle(applicationContext.getString(R.string.start_tracking_time))
            .setContentText(0L.formattedTime)
    }

    private val state: StateFlow<UIState<Record>> by lazy {
        getStateUseCase()
            .map<Record, UIState<Record>> { UIState.Success(it) }
            .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, UIState.Loading)
    }

    private var lastRecordTime: Long? = null
    private var timerJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        startForegroundService(notificationManager)
        time.observe(this) {
            val notification = notificationBuilder.setContentText(it.formattedTime)
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        }
        CoroutineScope(Dispatchers.Main).launch {
            state.collectLatest { state ->
                when(state) {
                    is UIState.Loading -> {
                        stopTimer()
                        notificationBuilder.clearActions()
                    }
                    is UIState.Success -> {
                        when(state.data.state) {
                            MeterState.StateIn -> {
                                lastRecordTime = state.data.date.time
                                startTimer()
                            }
                            MeterState.StateOut -> {
                                stopTimer()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_TOGGLE_STATE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    toggleStateUseCase()
                }
            }
            else -> Unit
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private fun startForegroundService(manager: NotificationManager) {
        startTimer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(manager)
        }
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startTimer() {
        notificationBuilder.setContentTitle(applicationContext.getString(R.string.tracking_time))
        notificationBuilder.clearActions()
        notificationBuilder.addAction(
            R.drawable.ic_clock_out,
            applicationContext.getString(R.string.stop),
            togglePendingIntent
        )
        timerJob?.cancel()
        lastRecordTime?.let { lastTime ->
            val current = Date().time
            val diff = current - lastTime
            _time.value = diff
        }
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
        notificationBuilder.setContentTitle(applicationContext.getString(R.string.start_tracking_time))
        notificationBuilder.clearActions()
        notificationBuilder.addAction(
            R.drawable.ic_clock_in,
            applicationContext.getString(R.string.start_tracking),
            togglePendingIntent
        )
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