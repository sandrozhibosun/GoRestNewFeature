package com.apolis.myapplication.viewmodel

import androidx.annotation.CallSuper
import com.apolis.myapplication.MainCoroutineRule
import com.apolis.myapplication.repository.NetworkRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest{

    private val netWorkRepository:NetworkRepository = mockk<NetworkRepository>(relaxed = true)

    private lateinit var subject: MainViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        coEvery { netWorkRepository.tryUpdateUserListCache() } just Runs
        subject = MainViewModel(
            netWorkRepository
        )
    }
    @Test
    fun givenRelaxMock_whenInitializedViewModel_returnNull(){

        runBlocking {
            subject.refreshUserCache()
        }
        coVerify(exactly = 1) {  netWorkRepository.tryUpdateUserListCache()  }
    }


}