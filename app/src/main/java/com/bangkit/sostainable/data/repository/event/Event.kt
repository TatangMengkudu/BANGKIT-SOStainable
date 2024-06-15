package com.bangkit.sostainable.data.repository.event

import okhttp3.MultipartBody

data class Event(
    val judulEvent: String,
    val tipeLokasi: String,
    val fileFoto: List<MultipartBody.Part>,
    val deskripsiEvent: String,
    val jamMulai: String,
    val jamSelesai: String,
    val tanggalMulai: String,
    val tanggalSelesai: String,
    val alamat: String,
    val jumlahMinimumVolunteer: Int,
    val jumlahMinimumDonasi: Long,
    val pembuatEvent: String
)