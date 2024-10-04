package com.example.movex.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movex.model.RunSummary

@Dao
interface RunSummaryDao {
    @Insert
    suspend fun insert(runSummary: RunSummary)

    @Query("SELECT * FROM run_summary ORDER BY timestamp DESC")
    suspend fun getAllSummaries(): List<RunSummary>

    @Delete
    suspend fun delete(runSummary: RunSummary)
}
