package com.bangkit.sostainable.data.local.room.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class JoinEvent(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_event")
    var idEvent: String,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "startDate")
    var startDate: String,
    @ColumnInfo(name = "endDate")
    var endDate: String,
    @ColumnInfo(name = "address")
    var address: String
): Parcelable