package com.bangkit.sostainable.data.local.room.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class HistoryDonateEvent (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_event")
    var idEvent: String,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "Date")
    var Date: String,
    @ColumnInfo(name = "nominal")
    var nominal: String,
    @ColumnInfo(name = "payment")
    var payment: String
    ): Parcelable