/**
 * Created by Ulises on 28/4/23.
 */
package com.gmail.uli153.workdaymeter.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.service.ChronometerService

class StateWidgetProvider: AppWidgetProvider() {

    companion object {
        fun updateViews(context: Context, manager: AppWidgetManager, state: UIState<Record>) {
            val views = RemoteViews(context.packageName, R.layout.widget_home)
            val intent = Intent(context, ChronometerService::class.java)
            intent.action = ChronometerService.ACTION_TOGGLE_STATE
            val pendingIntent = PendingIntent.getService(context, ChronometerService.requestCode, intent, ChronometerService.intentFlags)
            views.setOnClickPendingIntent(R.id.btn_toggle, pendingIntent)
            when(state) {
                is UIState.Loading -> {
                    views.setImageViewResource(R.id.btn_toggle, R.drawable.ic_clock_out)
                }
                is UIState.Success -> {
                    when(state.data.state) {
                        MeterState.StateIn -> {
                            views.setImageViewResource(R.id.btn_toggle, R.drawable.ic_clock_out)
                        }
                        MeterState.StateOut -> {
                            views.setImageViewResource(R.id.btn_toggle, R.drawable.ic_clock_in)
                        }
                    }
                }
            }
            val componentName = ComponentName(context, StateWidgetProvider::class.java)
            manager.updateAppWidget(componentName, views)
        }
    }
}