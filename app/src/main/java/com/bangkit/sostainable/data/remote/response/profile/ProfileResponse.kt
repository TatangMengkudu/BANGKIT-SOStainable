package com.bangkit.sostainable.data.remote.response.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("foto_profil")
	val fotoProfil: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("no_rekening")
	val noRekening: String? = null,

	@field:SerializedName("nama_bank")
	val namaBank: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("no_telepon")
	val noTelepon: String? = null
)
