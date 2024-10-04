package com.example.movex.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun TrainingDetailScreen(navController: NavController, day: String) {
    val viewModel: TrainingViewModel = viewModel()
    var completedExercises by remember { mutableStateOf(mutableListOf<String>()) }
    val backExercises = listOf("Barra Fixa", "Peck Deck") // Puxado removido
    val bicepExercises = listOf("Rosca Direta", "Rosca Scott") // Rosca Martelo removido

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Treino para $day", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Exercícios para Costas:", style = MaterialTheme.typography.headlineMedium)

        for (exercise in backExercises) {
            ExerciseCard(exercise = exercise, completedExercises = completedExercises) {
                if (exercise !in completedExercises) {
                    completedExercises.add(exercise)
                    viewModel.incrementProgress(day)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Exercícios para Bíceps:", style = MaterialTheme.typography.headlineMedium)

        for (exercise in bicepExercises) {
            ExerciseCard(exercise = exercise, completedExercises = completedExercises) {
                if (exercise !in completedExercises) {
                    completedExercises.add(exercise)
                    viewModel.incrementProgress(day)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)) // Cor laranja
        ) {
            Text(text = "Voltar", color = Color.White)
        }
    }
}

@Composable
fun ExerciseCard(exercise: String, completedExercises: List<String>, onComplete: () -> Unit) {
    val isCompleted = exercise in completedExercises

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, Color(0xFFFFA500)) // Cor laranja para a borda
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = exercise, style = MaterialTheme.typography.bodyLarge)

            Button(
                onClick = {
                    if (!isCompleted) {
                        onComplete()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)) // Cor laranja
            ) {
                Text(text = if (isCompleted) "Feito" else "Marcar como Feito", color = Color.White)
            }
        }
    }
}
