package com.apolis.myapplication.repository

import android.util.Log
import com.apolis.myapplication.MainCoroutineRule
import com.apolis.myapplication.database.UserDao
import com.apolis.myapplication.metadata.Meta
import com.apolis.myapplication.metadata.Pagination
import com.apolis.myapplication.metadata.User
import com.apolis.myapplication.metadata.UserResponse
import com.apolis.myapplication.network.GoRestService
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NetworkRepositoryTest() {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    /*
    or
    @ExperimentalCoroutinesApi
val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

@ExperimentalCoroutinesApi
@Before
fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)
}

@ExperimentalCoroutinesApi
@After
fun tearDownDispatcher() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
}
    */

    private val userDao = mockk<UserDao>()
    private val goRestService = mockk<GoRestService>()
    private lateinit var user: User
    private lateinit var userList: List<User>
    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()


    private lateinit var subject: NetworkRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        subject = NetworkRepository(
            goRestService = goRestService,
            userDao = userDao,
            dispatcherIO = testDispatcher
        )
        user = User(
            email = "",
            gender = "",
            id = 1,
            name = "",
            status = ""
        )
        userList = listOf( User(
            email = "",
            gender = "",
            id = 1,
            name = "",
            status = ""
        ))

    }

    @ExperimentalCoroutinesApi
    @Test
    fun networkRepository_requestDataFromRemote() {
        coEvery { subject.tryUpdateUserListCache() } just Runs


        runTest {
            subject.tryUpdateUserListCache()
        }
        coVerify(exactly = 1) { goRestService.getUserFromApi() }
    }

    @Test
    fun networkRepository_getUserFromRoom_withValid_User() {
        coEvery { subject.tryUpdateUserListCache() } just Runs
        coEvery { subject.userList } returns flowOf(listOf(user))
        runBlocking {

            subject.tryUpdateUserListCache()

            coVerify { goRestService.getUserFromApi() }

            val firstUser = subject.userList.first().size

            assertEquals(1, firstUser)
        }

    }

    @Test
    fun networkRepository_tryUpdateCache_andDataMatched() {
        coEvery { subject.tryUpdateUserListCache() } just Runs
        coEvery { goRestService.getUserFromApi() } returns UserResponse(
            1,
            listOf(user),
            Meta(
                Pagination(1, 1, 1, 1)
            )
        )

        runBlocking {
            subject.tryUpdateUserListCache()
        }

        coVerifyOrder {
            goRestService.getUserFromApi()
            userDao.addAllUsers(listOf(user))
        }
    }

    @Test
    fun networkRepository_tryUpdateCache_CaptureUserSlot() {
        coEvery { subject.tryUpdateUserListCache() } just Runs
        coEvery { goRestService.getUserFromApi() } returns UserResponse(
            1,
            userList,
            Meta(
                Pagination(1, 1, 1, 1)
            )
        )

        val slot = slot<List<User>>()
        coEvery { userDao.addAllUsers(capture(slot)) } returns Unit

        runBlocking {
            subject.tryUpdateUserListCache()
        }
        coVerifyOrder {
            goRestService.getUserFromApi()
        }
        assertEquals(slot.captured, listOf(user))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun networkRepository_trySomethingWith_Coroutine() {


        runTest {
            subject.doSomethingWithCoroutine()
        }
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify {
            goRestService.getUserFromApi()
        }

    }



}