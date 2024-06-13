package com.bangkit.sostainable.data.remote.response.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("data")
	val data: List<DataItem> = emptyList(),

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

@Entity(tableName = "Events")
data class DataItem(
	@PrimaryKey
	@field:SerializedName("id_event")
	val idEvent: String,

	@field:SerializedName("judul_event")
	val judulEvent: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("pembuat_event")
	val pembuatEvent: String? = null,

	@field:SerializedName("foto_lokasi")
	val fotoLokasi: String? = null
)
