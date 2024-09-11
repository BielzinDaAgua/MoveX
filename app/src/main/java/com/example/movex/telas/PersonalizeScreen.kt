package com.example.movex.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movex.R

@Composable
fun PersonalizeScreen(navController: NavController, userId: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bem vindo ao MoveX 🔥",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Personalizar o seu treino",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Escolha seu tipo de treino",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Exemplo de como adicionar opções de treino
            WorkoutOption(
                workoutName = "Treino Força",
                imageRes = R.drawable.strength
            )
            WorkoutOption(
                workoutName = "Treino Força",
                imageRes = R.drawable.strength
            )
        }

        BottomNavigationBar(navController, userId)
    }
}

@Composable
fun WorkoutOption(workoutName: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = workoutName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { /* Ação de personalização */ },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF5722))
            ) {
                Text(text = "Personalizar")
            }
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
}
