package com.bangkit.sostainable.data.remote.response.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
