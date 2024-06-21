package com.bangkit.sostainable.data.remote.response.event.detail

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("judul_event")
	val judulEvent: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsiEvent: String? = null,

	@field:SerializedName("jam_mulai")
	val jamMulai: String? = null,

	@field:SerializedName("tanggal_selesai")
	val tanggalSelesai: String? = null,

	@field:SerializedName("jam_selesai")
	val jamSelesai: String? = null,

	@field:SerializedName("tanggal_mulai")
	val tanggalMulai: String? = null,

	@field:SerializedName("jumlah_minimum_volunteer")
	val jumlahMinimumVolunteer: Int? = null,

	@field:SerializedName("id_event")
	val idEvent: String? = null,

	@field:SerializedName("jumlah_minimum_donasi")
	val jumlahMinimumDonasi: Int? = null,

	@field:SerializedName("pembuat_event")
	val pembuatEvent: String? = null,

	@field:SerializedName("foto_lokasi")
	val fotoLokasi: List<String?>? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)
