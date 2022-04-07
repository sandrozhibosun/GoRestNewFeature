package com.apolis.myapplication.repository

import android.content.IntentFilter
import android.util.Log
import com.apolis.myapplication.broadcastReceiver.MyBroadcastReceiver
import com.apolis.myapplication.database.UserDao
import com.apolis.myapplication.database.UserDatabase
import com.apolis.myapplication.di.DispatcherDefault
import com.apolis.myapplication.di.DispatcherIO
import com.apolis.myapplication.metadata.User
import com.apolis.myapplication.network.GoRestService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkRepository @Inject constructor(
    private val goRestService: GoRestService,
    private val userDao: UserDao,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) {
//    val userList: Flow<List<User>> = flow {
//            val userList = goRestService.getUserFromApi().userList
//            emit(userList)
//    }   .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
//        .catch { value -> Log.d("Logoutput", "flow error: $value") }
//        .flowOn(dispatcherIO)

    val userList:Flow<List<User>>
    get() = userDao.getAll()

    private suspend fun fetchUserListToCache(){
        try {
            val userList = goRestService.getUserFromApi().userList
            userDao.addAllUsers(userList)
        } catch (e:Throwable){
//            Log.d("NetworkRepository","exception when fetch network data ${e.message}")
        }
    }

    suspend fun tryUpdateUserListCache(){
        fetchUserListToCache()
    }

    fun doSomethingWithBroadcastReceiver(){

    }

    fun doSomethingWithCoroutine(){
       CoroutineScope(dispatcherIO).launch {
           try {

                   val userResponse  = goRestService.getUserFromApi().userList
                   userDao.addAllUsers( userResponse)

           }
           catch (e: Throwable){

           }
       }
    }

}