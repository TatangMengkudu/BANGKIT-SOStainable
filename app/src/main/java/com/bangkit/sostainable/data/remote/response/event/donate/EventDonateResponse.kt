package com.bangkit.sostainable.data.remote.response.event.donate

import com.google.gson.annotations.SerializedName

data class EventDonateResponse(

	@field:SerializedName("listDonasi")
	val listDonasi: List<ListDonasiItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ListDonasiItem(

	@field:SerializedName("id_donasi")
	val idDonasi: String? = null,

	@field:SerializedName("nominal")
	val nominal: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("id_event")
	val idEvent: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
