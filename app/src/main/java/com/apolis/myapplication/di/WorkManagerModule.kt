package com.apolis.myapplication.di

import android.content.Context
import androidx.work.WorkManager
import com.apolis.myapplication.workManager.MyForeGroundWorkerDependencies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WorkManagerModule {
    @Singleton
    @Provides
    fun provideForeGroundWork(): MyForeGroundWorkerDependencies{
        return  MyForeGroundWorkerDependencies()
    }
    @Singleton
    @Provides
    fun provideWorkerManagerInstance(@ApplicationContext appContext:Context):WorkManager{
        return WorkManager.getInstance(appContext)
    }

}