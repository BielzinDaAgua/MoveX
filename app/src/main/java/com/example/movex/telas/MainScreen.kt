package com.example.movex.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun MainScreen(navController: NavController, userId: Int) {
    val progressMonday = 1.0f
    val progressTuesday = 0.75f
    val progressWednesday = 0.45f
    val progressThursday = 0.1f

    Scaffold(
        bottomBar = {
            BottomAppBar(navController = navController, userId = userId)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Bem-vindo ao MoveX 🔥",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Realize seu treino",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            DayTrainingCard(
                day = "Segunda-feira",
                exercise = "Costas e Bíceps",
                progress = progressMonday
            )

            DayTrainingCard(
                day = "Terça-feira",
                exercise = "Peito e Tríceps",
                progress = progressTuesday
            )

            DayTrainingCard(
                day = "Quarta-feira",
                exercise = "Peito e Tríceps",
                progress = progressWednesday
            )

            DayTrainingCard(
                day = "Quinta-feira",
                exercise = "Ombros e Trapézio",
                progress = progressThursday
            )
        }
    }
}

@Composable
fun BottomAppBar(navController: NavController,  userId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f) // Ocupa 70% da largura da tela
            .height(70.dp)
            .background(Color.Black, shape = MaterialTheme.shapes.medium)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavigationItem(
            icon = Icons.Default.Home,
            label = "Home",
            isSelected = true, // Defina como true se estiver na tela Home
            onClick = {
                navController.navigate("main_screen/$userId")
            }
        )
        BottomNavigationItem(
            icon = Icons.Default.Add,
            label = "person",
            isSelected = false, // Defina como true se estiver na tela de Adicionar
            onClick = {
                navController.navigate("personalize_screen/$userId")
            }
        )
        BottomNavigationItem(
            icon = Icons.Default.Person,
            label = "Profile",
            isSelected = false, // Defina como true se estiver na tela de Perfil
            onClick = {
                navController.navigate("user_details_screen/$userId")
            }
        )
    }
}

@Composable
fun BottomNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFFF9800) else Color.Transparent

    Column(
        modifier = Modifier
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = Color.White)
    }
}


@Composable
fun DayTrainingCard(day: String, exercise: String, progress: Float) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = day, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = exercise, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(
                text = "Iniciante",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black,
                modifier = Modifier
                    .background(Color.Gray)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color.LightGray),
            color = Color(0xFFFFA500) // Cor laranja
        )
    }
}
