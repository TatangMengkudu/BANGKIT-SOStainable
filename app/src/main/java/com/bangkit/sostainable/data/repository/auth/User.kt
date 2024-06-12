package com.bangkit.sostainable.data.repository.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String? = null,
    val email: String? = null,
    val tanggalLahir: String? = null,
    val nomorRekening: String? = null,
    val namaBank: String? = null,
    val password: String? = null,
): Parcelable
