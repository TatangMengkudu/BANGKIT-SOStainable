package com.bangkit.sostainable.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bookmark")
data class Bookmark (
    @PrimaryKey(autoGenerate = false)
    @field:ColumnInfo(name = "id_event")
    var idEvent: String = "",

    @field:ColumnInfo(name = "foto_lokasi")
    var fotoLokasi: String? = null,

    @field:ColumnInfo(name = "judul_event")
    var judulEvent: String? = null,

    @field:ColumnInfo(name = "tanggal_mulai")
    var tanggalMulai: String? = null,

    @field:ColumnInfo(name = "tanggal_selesai")
    var tanggalSelesai: String? = null,

    @field:ColumnInfo(name = "alamat")
    var alamat: String? = null,
)