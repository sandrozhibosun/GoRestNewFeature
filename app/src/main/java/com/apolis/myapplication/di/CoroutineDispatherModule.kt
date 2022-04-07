package com.apolis.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CoroutineDispatherModule {
    @Provides
    @DispatcherIO
    fun provideDispatcherIO() = Dispatchers.IO
    @Provides
    @DispatcherDefault
    fun provideDispatcherDefault() = Dispatchers.Default
    @Provides
    @DispatcherMain
    fun provideDispatcherMain() = Dispatchers.Main
    @Provides
    @DispatcherUnconfined
    fun provideDispatcherUnconfined() = Dispatchers.Unconfined

}
@Qualifier
annotation class DispatcherIO
@Qualifier
annotation class DispatcherDefault
@Qualifier
annotation class DispatcherMain
@Qualifier
annotation class DispatcherUnconfined