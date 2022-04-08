package com.apolis.myapplication.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.apolis.myapplication.metadata.User
import com.apolis.myapplication.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading
    private val _textInputSorted = MutableLiveData<String>()
    val textInputSorted: LiveData<String>
        get() = _textInputSorted

    init {
        refreshUserCache()
    }

    val userListFlow: LiveData<List<User>> = networkRepository.userList.cancellable()
        .onCompletion { cause ->
            Log.d("MainViewModel", "Flow completed with Exception")
        }.catch { value ->
            Log.d("MainViewModel", "Flow Exception catched: ${value.message}")
        }.onEach { _loading.postValue(false) }.asLiveData()

    fun refreshUserCache() {
        viewModelScope.launch(
//            CoroutineExceptionHandler { _, e ->
////            Log.d("MainViewModel","exception in MainViewModelScope: ${e.message}")
//        }
        ) {
            _loading.postValue(true)
            networkRepository.tryUpdateUserListCache()
        }
    }

    fun onTextInputChanged(textInput: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _textInputSorted.postValue(sortStringWithVowel(string = textInput)!!)
            }
        }
    }


    private fun sortStringWithVowel(string: String): String {
        var vowelMap = sortedMapOf(
            'a' to 0,
            'e' to 0,
            'i' to 0,
            'o' to 0,
            'u' to 0
        )
        var currentString = string
        for (i in string.indices) {
            val char = string[i]
            if (vowelMap.containsKey(char)) {
                vowelMap[char] = vowelMap[char]?.plus(1) ?: 0
                currentString = currentString.replaceFirst(char, ' ', true)
            }
        }
        currentString.filterNot { it.isWhitespace() }
        val prefix = StringBuilder()
        vowelMap.forEach { (char, int) ->
            var i = int
            while (i != 0) {
                prefix.append(char)
                i -= 1
            }
            vowelMap[char] = 0
        }
        prefix.append(currentString)

        return String(prefix)
    }

}