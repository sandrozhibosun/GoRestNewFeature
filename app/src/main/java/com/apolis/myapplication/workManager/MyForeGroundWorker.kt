package com.apolis.myapplication.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.apolis.myapplication.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltWorker
class MyForeGroundWorker @AssistedInject constructor(@Assisted appContext: Context,
                                           @Assisted workParameter:WorkerParameters)
    :CoroutineWorker(appContext,workParameter){

    //notification for foreground
    @RequiresApi(Build.VERSION_CODES.M)
    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)

    private val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Important background job")

    //work job, could implement here as TODO by different functionality
    override suspend fun doWork(): Result {
        Log.d(TAG, "Start job")

        //create notification channel
        createNotificationChannel()
        //build notification when do work
        val notification = notificationBuilder.build()
        //set foregroundInfo for coroutineWork with ID and notification, that's how to set a foreground service
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)

        for (i in 0..100) {
            // we need it to get progress in UI, workDataOf to set data with key-value pair
                //then set as Progress
            setProgress(workDataOf(ARG_PROGRESS to i))
            // update the notification progress
            showProgress(i)
            delay(DELAY_DURATION)
        }

        Log.d(TAG, "Finish job")
        return Result.success()
    }

    //setProgress in notifications
    private suspend fun showProgress(progress: Int) {
        val notification = notificationBuilder
            .setProgress(100, progress, false)
            .build()
        //and then update it by using setForeground
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                notificationManager?.createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        }
    }

    companion object {

        const val TAG = "ForegroundWorker"
        const val NOTIFICATION_ID = 42
        const val CHANNEL_ID = "Job progress"
        const val ARG_PROGRESS = "Progress"
        private const val DELAY_DURATION = 100L // ms
    }

}