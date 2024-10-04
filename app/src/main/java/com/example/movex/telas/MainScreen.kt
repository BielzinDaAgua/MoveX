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
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MainScreen(navController: NavController, userId: Int) {
    val viewModel: TrainingViewModel = viewModel()
    val progressMonday = viewModel.getProgress("Segunda-feira")

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
                progress = progressMonday.toInt(),
                imageRes = R.drawable.strength,
                onIncrement = { viewModel.incrementProgress("Segunda-feira") },
                onNavigateToDetails = { navController.navigate("training_detail_screen/Segunda-feira") }
            )

//            DayTrainingCard(
//                day = "Terça-feira",
//                exercise = "Peito e Tríceps",
//                progress = progressMonday.toInt(),
//                imageRes = R.drawable.strength,
//                onIncrement = { viewModel.incrementProgress("Terça-feira") },
//                onNavigateToDetails = { navController.navigate("training_detail_screen/Segunda-feira") }
//            )
//
//            DayTrainingCard(
//                day = "Quarta-feira",
//                exercise = "Pernas e Ombro",
//                progress = progressMonday.toInt(),
//                imageRes = R.drawable.strength,
//                onIncrement = { viewModel.incrementProgress("Quarta-feira") },
//                onNavigateToDetails = { navController.navigate("training_detail_screen/Segunda-feira") }
//            )
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
            BottomNavigationItem(
                icon = Icons.Default.LocationOn,
                label = "Profile",
                isSelected = false,
                onClick = {
                    navController.navigate("running_screen/$userId")
                }
            )

            BottomNavigationItem(
                icon = Icons.Default.Group,
                label = "Profile",
                isSelected = false,
                onClick = {
                    navController.navigate("group_screen/$userId")
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
fun DayTrainingCard(
    day: String,
    exercise: String,
    progress: Int,
    imageRes: Int,
    onIncrement: () -> Unit,
    onNavigateToDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
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
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.weight(1f)) {
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
                        .clickable {
                            onIncrement() // Chama o incremento ao clicar
                            onNavigateToDetails()
                        } // Navegação ao clicar
                )
            }

            // Barra de Progresso
            LinearProgressIndicator(
                progress = progress / 100f, // Converte o progresso para fração
                modifier = Modifier.fillMaxWidth()
            )

            // Texto de Progresso
            Text(
                text = "$progress% concluído",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}