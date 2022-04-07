package com.apolis.myapplication.di

import android.content.Context
import androidx.room.Room
import com.apolis.myapplication.database.UserDao
import com.apolis.myapplication.database.UserDatabase
import com.apolis.myapplication.utils.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext applicationContext: Context):UserDatabase{
        return Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            Const.USER_DATABASE
        ).build()
    }

    @Provides
    fun provideUserDao(database : UserDatabase): UserDao {
        return database.userDao()
    }

}