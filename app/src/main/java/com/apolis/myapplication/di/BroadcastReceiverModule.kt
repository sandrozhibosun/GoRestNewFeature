package com.apolis.myapplication.di

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.apolis.myapplication.broadcastReceiver.MyBroadcastReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@InstallIn(ActivityComponent::class)
@Module
class BroadcastReceiverModule {
    @Provides
    @ActivityScoped
    fun getContextRegisterBroadcastReceiver() = MyBroadcastReceiver()
    @Provides
    @ActivityScoped
    fun getTargetIntentFilter() = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        .apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }

}