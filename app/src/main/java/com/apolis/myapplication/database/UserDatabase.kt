package com.apolis.myapplication.database

import android.service.autofill.UserData
import androidx.room.Database
import androidx.room.RoomDatabase
import com.apolis.myapplication.metadata.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
}