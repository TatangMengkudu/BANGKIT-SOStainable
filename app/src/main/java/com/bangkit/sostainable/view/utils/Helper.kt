package com.bangkit.sostainable.view.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
fun dateFormat(created: String): String {
    val instant = Instant.parse(created)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        .withZone(ZoneId.of(TimeZone.getDefault().id))
    return formatter.format(instant)
}