package com.example.prakt22_2.Data

import androidx.room.*

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: DataEntity)

    @Query("SELECT * FROM data_table")
    suspend fun getAllData(): List<DataEntity>

    @Update
    fun updateData(data: DataEntity)

    @Delete
    suspend fun deleteData(data: DataEntity)
}