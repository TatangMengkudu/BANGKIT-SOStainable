package com.bangkit.sostainable.data.remote.response.event

import com.google.gson.annotations.SerializedName

data class EventMessageResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
