package com.apolis.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.apolis.myapplication.metadata.User
import com.apolis.myapplication.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) :ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
    get() = _loading

    init {
        refreshUserCache()
    }

    val userListFlow: LiveData<List<User>> = networkRepository.userList.cancellable()
        .onCompletion { cause ->
    Log.d("MainViewModel","Flow completed with Exception")
    }.catch { value ->
        Log.d("MainViewModel","Flow Exception catched: ${value.message}")
    }.onEach { _loading.postValue(false) }.asLiveData()

    fun refreshUserCache(){
        viewModelScope.launch(
//            CoroutineExceptionHandler { _, e ->
////            Log.d("MainViewModel","exception in MainViewModelScope: ${e.message}")
//        }
        ) {
            _loading.postValue(true)
            networkRepository.tryUpdateUserListCache()
        }
    }


}