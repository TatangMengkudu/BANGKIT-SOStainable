package com.bangkit.sostainable.data.remote.response.profile

import com.google.gson.annotations.SerializedName

data class ProfileMessageResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
