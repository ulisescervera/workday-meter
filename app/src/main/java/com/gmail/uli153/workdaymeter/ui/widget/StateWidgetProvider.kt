/**
 * Created by Ulises on 28/4/23.
 */
package com.gmail.uli153.workdaymeter.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.use_cases.GetStateUseCase
import com.gmail.uli153.workdaymeter.service.ChronometerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class StateWidgetProvider: AppWidgetProvider() {

    @Inject lateinit var getStateUseCase: GetStateUseCase

    private val stateFlow by lazy {
        getStateUseCase().map { UIState.Success(it) }.stateIn(CoroutineScope(Dispatchers.Main), SharingStarted.Eagerly, UIState.Loading)
    }

    private var state: UIState<Record> = UIState.Loading

    private var collectorJob: Job? = null

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        collectorJob = CoroutineScope(Dispatchers.Main).launch {
            stateFlow.collectLatest {
                state = it

            }
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        collectorJob?.cancel()
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        collectorJob?.cancel()
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_home)
            val intent = Intent(context, ChronometerService::class.java)
            intent.action = ChronometerService.ACTION_TOGGLE_STATE
            val pendingIntent = PendingIntent.getService(context, ChronometerService.requestCode, intent, ChronometerService.intentFlags)
            views.setOnClickPendingIntent(R.id.btn_toggle, pendingIntent)
            when(val s = state) {
                is UIState.Loading -> {
                    views.setImageViewResource(R.id.btn_toggle, R.drawable.ic_clock_out)
                }
                is UIState.Success -> {
                   when(s.data.state) {
                       MeterState.StateIn -> {
                           views.setImageViewResource(R.id.btn_toggle, R.drawable.ic_clock_out)
                       }
                       MeterState.StateOut -> {
                           views.setImageViewResource(R.id.btn_toggle, R.drawable.ic_clock_in)
                       }
                    }
                }
            }
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}