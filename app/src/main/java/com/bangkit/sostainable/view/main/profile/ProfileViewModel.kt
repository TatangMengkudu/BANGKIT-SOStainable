package com.bangkit.sostainable.view.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.remote.response.profile.ProfileResponse
import com.bangkit.sostainable.data.remote.response.profile.update.UpdateProfileResponse
import com.bangkit.sostainable.data.repository.auth.User
import com.bangkit.sostainable.data.repository.profile.ProfileRepository
import com.bangkit.sostainable.data.utils.Result

class ProfileViewModel(
    private val profileRepository: ProfileRepository
): ViewModel() {
    suspend fun profileUser(): LiveData<Result<ProfileResponse>> {
        return profileRepository.profileUser()
    }

    suspend fun updateProfile(user: User): LiveData<Result<UpdateProfileResponse>> {
        return profileRepository.updateProfile(user)
    }
}