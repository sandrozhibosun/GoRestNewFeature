package com.apolis.myapplication.network

import com.apolis.myapplication.metadata.User
import com.apolis.myapplication.metadata.UserResponse
import com.apolis.myapplication.utils.Const
import retrofit2.http.GET
import java.util.concurrent.Flow

interface GoRestService {

    @GET(Const.ENDPOINT)
    suspend fun getUserFromApi(): UserResponse

}