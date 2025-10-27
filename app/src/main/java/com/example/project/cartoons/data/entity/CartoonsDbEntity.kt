package com.example.project.cartoons.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CartoonsDbEntity (
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "firstSeenIn") val firstSeenIn: String?,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
)