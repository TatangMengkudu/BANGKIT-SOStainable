package com.bangkit.sostainable.data.remote.response.event

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class 	DataItem(

	@field:SerializedName("judul_event")
	val judulEvent: String? = null,

	@field:SerializedName("deskripsi_event")
	val deskripsiEvent: String? = null,

	@field:SerializedName("id_event")
	val idEvent: String? = null,

	@field:SerializedName("pembuat_event")
	val pembuatEvent: String? = null,

	@field:SerializedName("foto_lokasi")
	val fotoLokasi: String? = null
)
