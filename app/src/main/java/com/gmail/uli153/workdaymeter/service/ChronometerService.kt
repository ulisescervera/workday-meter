/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.gmail.uli153.workdaymeter.MainActivity
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.use_cases.GetStateUseCase
import com.gmail.uli153.workdaymeter.domain.use_cases.ToggleStateUseCase
import com.gmail.uli153.workdaymeter.ui.widget.StateWidgetProvider
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import com.gmail.uli153.workdaymeter.utils.extensions.millisSince
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.threeten.bp.OffsetDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChronometerService: LifecycleService() {

    companion object {
        const val ACTION_TOGGLE_STATE = "ACTION_TOGGLE_STATE"
        const val requestCode = 0

        private val _time: MutableStateFlow<Long> = MutableStateFlow(0L)
        val time: StateFlow<Long> = _time

        val intentFlags = if (Build.VERSION.SDK_INT >= 23){
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "chronometer_cannel"
        private const val NOTIFICATION_CHANNEL_NAME = "Chronometer"
        private const val UPDATE_NOTIFICATION_DELAY = 1000L
    }

    @Inject lateinit var toggleStateUseCase: ToggleStateUseCase
    @Inject lateinit var getStateUseCase: GetStateUseCase

    private val mainPendingIntent: PendingIntent by lazy {
        val intent = Intent(applicationContext, MainActivity::class.java)
        PendingIntent.getActivity(applicationContext, requestCode, intent, intentFlags)
    }

    private val togglePendingIntent: PendingIntent by lazy {
        val intent = Intent(applicationContext, ChronometerService::class.java)
        intent.action = ACTION_TOGGLE_STATE
        PendingIntent.getService(applicationContext, requestCode, intent, intentFlags)
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

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private var lastRecordTime: OffsetDateTime? = null
    private var timerJob: Job? = null
    private var currentState: UIState<Record> = UIState.Loading

    override fun onCreate() {
        super.onCreate()
        startForegroundService(notificationManager)
        CoroutineScope(Dispatchers.Main).launch {
            state.collectLatest { state ->
                currentState = state
                when(state) {
                    is UIState.Loading -> {
                        stopTimer()
                        notificationBuilder.clearActions()
                        updateWidget(state, null)
                    }
                    is UIState.Success -> {
                        when(state.data.state) {
                            MeterState.StateIn -> {
                                lastRecordTime = state.data.date
                                startTimer()
                            }
                            MeterState.StateOut -> {
                                stopTimer()
                                updateWidget(state, null)
                            }
                        }
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            time.collectLatest {
                val current = currentState
                if (current is UIState.Success && current.data.state == MeterState.StateIn) {
                    val formattedTime = it.formattedTime
                    val notification = notificationBuilder.setContentText(formattedTime)
                    notificationManager.notify(NOTIFICATION_ID, notification.build())
                    updateWidget(currentState, formattedTime)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when(intent?.action) {
            ACTION_TOGGLE_STATE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    toggleStateUseCase()
                }
            }
            else -> Unit
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        if (Build.VERSION.SDK_INT >= 24) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
    }

    private fun startForegroundService(manager: NotificationManager) {
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
        val lastTime = lastRecordTime ?: return

        val diff = OffsetDateTime.now().millisSince(lastTime)
        _time.value = diff
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(UPDATE_NOTIFICATION_DELAY)
                val diff = OffsetDateTime.now().millisSince(lastTime)
                if (!isActive) {
                    return@launch
                }
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
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(true)
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

    private fun updateWidget(state: UIState<Record>, formattedTime: String?) {
        StateWidgetProvider.updateViews(
            applicationContext,
            AppWidgetManager.getInstance(applicationContext),
            state,
            formattedTime
        )
    }
}