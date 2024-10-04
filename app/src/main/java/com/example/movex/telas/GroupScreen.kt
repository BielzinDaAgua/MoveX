package com.example.movex.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movex.model.Grupo
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.sp
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.Modifier


@Composable
fun GroupScreen(navController: NavController, userId: Int) {
    val grupos = listOf(
        Grupo(id = 1, nome = "Grupo de Corrida", membros = listOf("Alice", "Bob", "Carlos")),
        Grupo(id = 2, nome = "Grupo de Musculação", membros = listOf("Danielle", "Evelyn")),
        Grupo(id = 3, nome = "Grupo de Yoga", membros = listOf("Fernando", "Gabriela"))
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, userId = userId)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
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

            // Lista de grupos
            LazyColumn {
                items(grupos) { grupo ->
                    GroupItem(grupo = grupo) {
                        // Ao clicar, navega para uma nova tela com detalhes do grupo
                        navController.navigate("group_detail_screen/${grupo.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun GroupItem(grupo: Grupo, onClick: () -> Unit) {
    var showMembers by remember { mutableStateOf(false) } // Estado para controlar a exibição da lista de membros

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp), shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = grupo.nome,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp), // Tamanho da fonte aumentado
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botão para exibir os membros do grupo
        Button(
            onClick = { showMembers = !showMembers }, // Alterna a exibição dos membros
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text(
                text = if (showMembers) "Ocultar Membros" else "Ver Membros",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White) // Cor do texto do botão
            )
        }

        // Lista de membros, exibida apenas se showMembers for true
        if (showMembers) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                for (membro in grupo.membros) {
                    Text(
                        text = membro,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 2.dp) // Espaçamento entre os membros
                    )
                }
            }
        }
    }
}
