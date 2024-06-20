package com.bangkit.sostainable.data.remote.response.event

import com.google.gson.annotations.SerializedName

data class MyEventResponse(

	@field:SerializedName("listEvent")
	val listEvent: List<ListEventItem>,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ListEventItem(

	@field:SerializedName("judul_event")
	val judulEvent: String? = null,

	@field:SerializedName("jam_mulai")
	val jamMulai: String? = null,

	@field:SerializedName("level")
	val level: Double? = null,

	@field:SerializedName("jam_selesai")
	val jamSelesai: String? = null,

	@field:SerializedName("foto_lokasi")
	val fotoLokasi: List<String?>? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("tanggal_selesai")
	val tanggalSelesai: String? = null,

	@field:SerializedName("tanggal_mulai")
	val tanggalMulai: String? = null,

	@field:SerializedName("jumlah_minimum_volunteer")
	val jumlahMinimumVolunteer: Int? = null,

	@field:SerializedName("id_event")
	val idEvent: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("jumlah_minimum_donasi")
	val jumlahMinimumDonasi: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
