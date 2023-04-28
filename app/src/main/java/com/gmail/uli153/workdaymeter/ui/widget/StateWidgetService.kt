/**
 * Created by Ulises on 28/4/23.
 */
package com.gmail.uli153.workdaymeter.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.gmail.uli153.workdaymeter.domain.use_cases.GetStateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

