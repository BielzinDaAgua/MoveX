package com.example.movex.telas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movex.data.RunDatabase
import com.example.movex.model.RunSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RunHistoryScreen(navController: NavHostController, userId: Int) {
    val context = LocalContext.current
    val runDatabase = RunDatabase.getDatabase(context)

    var runSummaries by remember { mutableStateOf(listOf<RunSummary>()) }

    fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    suspend fun deleteRunSummary(runSummary: RunSummary) {
        runDatabase.runSummaryDao().delete(runSummary)
        runSummaries = runDatabase.runSummaryDao().getAllSummaries()
    }

    LaunchedEffect(Unit) {
        runSummaries = runDatabase.runSummaryDao().getAllSummaries()
        runSummaries.forEach { summary ->
            Log.d("RunHistoryScreen", "Timestamp: ${summary.timestamp}")
        }
    }

    val coroutineScope = remember { CoroutineScope(Dispatchers.IO) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = { navController.navigate("running_screen/$userId") },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA500)
            )
        ) {
            Text("Voltar")
        }

        LazyColumn {
            items(runSummaries) { runSummary ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(500.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(alignment = Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Corrida do dia:", color = Color.Black)
                            Text(text = formatTimestamp(runSummary.timestamp), color = Color.Black)
                            Text(
                                text = runSummary.summary,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(100.dp))

                        IconButton(onClick = {
                            coroutineScope.launch {
                                deleteRunSummary(runSummary)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}
