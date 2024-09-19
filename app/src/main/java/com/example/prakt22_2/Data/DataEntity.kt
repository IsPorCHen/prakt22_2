package com.example.prakt22_2.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val abilities: Array<String>,
    val imageBytes: ByteArray? = null
)

