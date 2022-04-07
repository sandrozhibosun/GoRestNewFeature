package com.apolis.myapplication.metadata

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apolis.myapplication.utils.Const

@Entity(tableName = Const.USER_TABLE)
data class User(
    val email: String,
    val gender: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String
)