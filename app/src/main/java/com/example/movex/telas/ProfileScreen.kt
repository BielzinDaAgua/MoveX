package com.example.movex.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter // Se você estiver usando o Coil para carregar imagens
import com.example.movex.model.Usuario
import com.example.movex.model.UsuarioDAO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userId: Int) {
    var usuario by remember { mutableStateOf<Usuario?>(null) }
    val usuarioDAO = UsuarioDAO()
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        usuario = usuarioDAO.obterUsuarioPorId(userId)
    }

    if (usuario == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Perfil do Usuário") },
                    actions = {
                        IconButton(onClick = { navController.navigate("edit_user_screen/${usuario?.id}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(usuario?.fotoUrl),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Nome: ${usuario?.nome}",
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Text(
                    text = "Email: ${usuario?.email}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* Adicione ação para logout ou outra funcionalidade */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Logout")
                }
            }
        }
    }
}
