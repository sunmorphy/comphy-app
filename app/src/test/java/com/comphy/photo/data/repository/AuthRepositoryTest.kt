package com.comphy.photo.data.repository

import app.cash.turbine.test
import com.comphy.photo.MainCoroutinesRule
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.auth.AuthBody
import com.comphy.photo.utils.MockUtils.mockAuthResponseData
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class AuthRepositoryTest {

    private val apiService = mock<ApiService>()
    private lateinit var authRepository: AuthRepository

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        authRepository = AuthRepository(apiService, Dispatchers.IO)
    }

    @Test
    fun userLogin() = runBlocking {
        val mockAuthResponseData = mockAuthResponseData()
        val authBody = AuthBody(username = "mockUsername", password = "mockPassword")

        whenever(apiService.userLogin(authBody)).thenReturn(
            ApiResponse.of { Response.success(mockAuthResponseData) }
        )

        authRepository.userLogin(authBody, {}, {}).test(2.toDuration(DurationUnit.SECONDS)) {
            val expectedAuthData = awaitItem()

            assertEquals(mockAuthResponseData.accessToken, expectedAuthData.accessToken)
            assertEquals(mockAuthResponseData.refreshToken, expectedAuthData.refreshToken)
            assertEquals(mockAuthResponseData.userId, expectedAuthData.userId)
            awaitComplete()
        }

        verify(apiService, times(1)).userLogin(authBody)
        verifyNoMoreInteractions(apiService)
    }
}