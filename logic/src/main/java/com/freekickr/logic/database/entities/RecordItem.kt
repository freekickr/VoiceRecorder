package com.freekickr.logic.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records_table")
data class RecordItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var name: String = "",
    var filePath: String = "",
    var length: Long = 0L,
    var time: Long = 0L
)