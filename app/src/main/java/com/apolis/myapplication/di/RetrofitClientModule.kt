package com.apolis.myapplication.di

import com.apolis.myapplication.network.GoRestService
import com.apolis.myapplication.utils.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitClientModule {
    @Provides
    @Singleton
    fun retrofitClient(): GoRestService = Retrofit
        .Builder()
        .baseUrl(Const.BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoRestService::class.java)
}