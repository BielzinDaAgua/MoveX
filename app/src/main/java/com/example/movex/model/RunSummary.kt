package com.example.movex.model;

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "run_summary")
data class RunSummary(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val summary: String,
    val timestamp: Long
)
