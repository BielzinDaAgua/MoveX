package com.example.movex.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movex.model.Grupo
import androidx.compose.foundation.lazy.items

@Composable
fun GroupScreen(navController: NavController, userId: Int) {
    val grupos = listOf(
        Grupo(id = 1, nome = "Grupo de Corrida"),
        Grupo(id = 2, nome = "Grupo de Musculação"),
        Grupo(id = 3, nome = "Grupo de Yoga")
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Grupos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    navController.navigate("create_group_screen/$userId")
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Criar Grupo")
            }

            LazyColumn {
                items(grupos) { grupo ->
                    GroupItem(grupo = grupo) {
                        navController.navigate("group_detail_screen/${grupo.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun GroupItem(grupo: Grupo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = grupo.nome,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
}
