package com.bangkit.sostainable.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sostainable.data.injection.InjectionProfile
import com.bangkit.sostainable.data.repository.profile.ProfileRepository
import com.bangkit.sostainable.view.main.profile.ProfileViewModel

class ProfileModelFactory(
    private val profileRepository: ProfileRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ProfileModelFactory(InjectionProfile.provideRepository(context))
    }
}