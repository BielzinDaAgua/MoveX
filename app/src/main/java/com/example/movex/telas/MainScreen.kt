package com.example.movex.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movex.R


@Composable
fun MainScreen(navController: NavController, userId: Int) {
    val progressMonday = 1.0f
    val progressTuesday = 0.75f
    val progressWednesday = 0.45f
    val progressThursday = 0.1f

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, userId = userId)
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
                progress = progressMonday,
                imageRes = R.drawable.strength
            )

            DayTrainingCard(
                day = "Terça-feira",
                exercise = "Peito e Tríceps",
                progress = progressTuesday,
                imageRes = R.drawable.strength
            )

            DayTrainingCard(
                day = "Quarta-feira",
                exercise = "Peito e Tríceps",
                progress = progressWednesday,
                imageRes = R.drawable.strength
            )

            DayTrainingCard(
                day = "Quinta-feira",
                exercise = "Ombros e Trapézio",
                progress = progressThursday,
                imageRes = R.drawable.strength
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, userId: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Ocupa 80% da largura da tela
                .height(70.dp)
                .background(Color.Black, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavigationItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = true,
                onClick = {
                    navController.navigate("main_screen/$userId")
                }
            )
            BottomNavigationItem(
                icon = Icons.Default.Add,
                label = "person",
                isSelected = false,
                onClick = {
                    navController.navigate("personalize_screen/$userId")
                }
            )
            BottomNavigationItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = false,
                onClick = {
                    navController.navigate("user_details_screen/$userId")
                }
            )
        }
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
fun DayTrainingCard(day: String, exercise: String, progress: Float, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = day,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = exercise,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }

                Text(
                    text = "Iniciante",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFFFFA500), shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
}