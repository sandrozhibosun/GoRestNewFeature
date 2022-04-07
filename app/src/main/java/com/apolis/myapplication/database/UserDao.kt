package com.apolis.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apolis.myapplication.metadata.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllUsers(users:List<User>)

    @Query("SELECT * FROM USERS  ORDER BY id DESC")
    fun getAll(): Flow<List<User>>

}
