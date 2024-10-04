package com.example.movex.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.movex.model.Usuario
import com.example.movex.model.UsuarioDAO
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(navController: NavController, userId: Int) {
    var usuario by remember { mutableStateOf<Usuario?>(null) }
    val usuarioDAO = UsuarioDAO()
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        usuario = usuarioDAO.obterUsuarioPorId(userId)
    }

    if (usuario == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberImagePainter(usuario?.fotoUrl),
                    contentDescription = "Foto do perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    alignment = Alignment.Center
                )

                Text(
                    text = "Detalhes da Conta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        navController.navigate("")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Editar o seu nome")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Editar a sua email")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("edit_user_screen/${usuario?.id}")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Editar Informações")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Excluir Conta")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(text = "Confirmar Exclusão")
                        },
                        text = {
                            Text("Tem certeza que deseja excluir sua conta? Esta ação é irreversível.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        usuario?.id?.let {
                                            usuarioDAO.removerUsuario(it.toLong())
                                            navController.navigate("home_screen")
                                        }
                                    }
                                }
                            ) {
                                Text("Confirmar", color = Color.Red)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}
