package com.bangkit.sostainable.data.json

import okhttp3.MultipartBody

data class ReportJson(
    val kendala: String,
    val jumlah_volunteer: Int,
    val id_event: String,
    val fileFoto: List<MultipartBody.Part>,
)