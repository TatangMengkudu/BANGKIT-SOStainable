package com.bangkit.sostainable.data.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.sostainable.data.local.datastore.UserPreference
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.remote.api.service.ApiService
import com.bangkit.sostainable.data.remote.response.auth.AuthResponse
import com.bangkit.sostainable.data.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository (
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun register(user: User): LiveData<Result<AuthResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val data = apiService.register(
                    user.username!!,
                    user.email!!,
                    user.tanggalLahir!!,
                    user.noRekening!!,
                    user.namaBank!!,
                    user.password!!
                )
                emit(Result.Success(data))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AuthResponse::class.java)
                emit(Result.Error(errorResponse.message!!))
            }
        }
    }

    suspend fun login(user: User) : LiveData<Result<AuthResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val data = apiService.login(
                    user.username!!,
                    user.password!!,
                )
                emit(Result.Success(data))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AuthResponse::class.java)
                emit(Result.Error(errorResponse.message!!))
            }
        }
    }

    // TODO: Logout


    // TODO: Session login disimpan ke datastore
    suspend fun saveSession(token: LoginSession) {
        userPreference.saveSession(token)
    }

    fun getSession() : Flow<LoginSession> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreference)
            }.also { instance = it }
    }
}