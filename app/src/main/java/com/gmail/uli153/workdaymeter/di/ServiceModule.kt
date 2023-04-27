/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gmail.uli153.workdaymeter.MainActivity
import com.gmail.uli153.workdaymeter.service.ChronometerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

//    @ServiceScoped
//    @Provides
//    fun provideMainActivityPendingIntent(
//        @ApplicationContext app: Context
//    ) = PendingIntent.getActivity(app, 0, Intent(app, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
//
//    @ServiceScoped
//    @Provides
//    fun provideBaseNotificationBuilder(
//        @ApplicationContext app: Context,
//        pendingIntent: PendingIntent
//    ) = NotificationCompat.Builder(app, ChronometerService.NOTIFICATION_CHANNEL_ID)
//        .setAutoCancel(false)
//        .setOngoing(true)
//        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
//        .setContentTitle("Running App")
//        .setContentText("00:00:00")
//        .setContentIntent(pendingIntent)
}