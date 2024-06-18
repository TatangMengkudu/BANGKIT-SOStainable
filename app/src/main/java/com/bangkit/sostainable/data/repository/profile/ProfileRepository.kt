package com.bangkit.sostainable.data.repository.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.sostainable.data.remote.api.service.ApiService
import com.bangkit.sostainable.data.remote.response.profile.ProfileMessageResponse
import com.bangkit.sostainable.data.remote.response.profile.ProfileResponse
import com.bangkit.sostainable.data.remote.response.profile.update.UpdateProfileResponse
import com.bangkit.sostainable.data.json.User
import com.bangkit.sostainable.data.utils.Result
import com.google.gson.Gson
import retrofit2.HttpException

class ProfileRepository(
    private val apiService: ApiService
) {
    suspend fun profileUser(): LiveData<Result<ProfileResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val data = apiService.profileUser()
                emit(Result.Success(data))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ProfileMessageResponse::class.java)
                emit(Result.Error(errorResponse.message!!))
            }
        }
    }

    suspend fun updateProfile(user: User): LiveData<Result<UpdateProfileResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val data = apiService.updateProfile(user)
                emit(Result.Success(data))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ProfileMessageResponse::class.java)
                emit(Result.Error(errorResponse.message!!))
            }
        }
    }
}