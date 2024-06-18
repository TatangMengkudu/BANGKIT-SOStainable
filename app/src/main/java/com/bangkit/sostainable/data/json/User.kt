package com.bangkit.sostainable.data.json

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val tanggalLahir: String? = null,
    val nomorRekening: String? = null,
    val namaBank: String? = null,
    val nomorTelp: String? = null,
    val alamat: String? = null,
    val password: String? = null
): Parcelable
