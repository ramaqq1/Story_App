package com.ramaqq.storyapp_submission1.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

// i berfungsi untuk menyimpan informasi tentang halaman terbaru yang diminta dari server
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
