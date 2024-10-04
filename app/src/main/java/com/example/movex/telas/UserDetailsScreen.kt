package com.example.movex.telas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.movex.R
import com.example.movex.model.Usuario
import com.example.movex.model.UsuarioDAO
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(navController: NavController, userId: Int) {
    var usuario by remember { mutableStateOf<Usuario?>(null) }
    val usuarioDAO = UsuarioDAO()
    val scope = rememberCoroutineScope()

    // Variável para armazenar a URI da foto de perfil (local ou de rede)
    var profilePhotoUri by remember { mutableStateOf<String?>("default_profile_picture") }
    var isUploading by remember { mutableStateOf(false) } // Para gerenciar o estado de upload
    var senha by remember { mutableStateOf("") } // Para gerenciar a senha do usuário
    var senhaVisivel by remember { mutableStateOf(false) } // Controlar visibilidade da senha


    // Launcher para selecionar uma imagem da galeria
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                profilePhotoUri = it.toString() // Armazena a URI da foto selecionada
            }
        }
    )

    LaunchedEffect(userId) {
        usuario = usuarioDAO.obterUsuarioPorId(userId)
        profilePhotoUri = usuario?.fotoUrl ?: "default_profile_picture" // Foto padrão se não tiver uma
        senha = usuario?.senha ?: "" // Carregar a senha atual do usuário
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
                // Exibir foto de perfil e permitir seleção de uma nova
                Image(
                    painter = rememberImagePainter(profilePhotoUri),
                    contentDescription = "Foto do perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                            // Abrir seletor de imagens
                            launcher.launch("image/*")
                        },
                    alignment = Alignment.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Exibir nome do usuário
                TextField(
                    value = usuario?.nome ?: "",
                    onValueChange = { novoNome ->
                        usuario = usuario?.copy(nome = novoNome)
                    },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Exibir email do usuário
                TextField(
                    value = usuario?.email ?: "",
                    onValueChange = { novoEmail ->
                        usuario = usuario?.copy(email = novoEmail)
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para alterar a senha
                TextField(
                    value = senha,
                    onValueChange = { novaSenha ->
                        senha = novaSenha
                    },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(
                                imageVector = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Mostrar barra de progresso durante o upload
                if (isUploading) {
                    CircularProgressIndicator()
                }

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                usuario?.let {
                                    isUploading = true // Iniciar o estado de upload

                                    // Verificar se a foto foi alterada
                                    val fotoUrl = if (profilePhotoUri != it.fotoUrl) {
                                        // Se a imagem foi alterada, fazer upload
                                        profilePhotoUri?.let { uri ->
                                            uploadImageToStorage(uri)
                                        } ?: "default_profile_picture"
                                    } else {
                                        // Se a imagem não foi alterada, manter a URL atual
                                        it.fotoUrl
                                    }

                                    // Atualizar o objeto usuário com a nova foto e a senha (se foram modificadas)
                                    val usuarioAtualizado = it.copy(fotoUrl = fotoUrl, senha = senha)

                                    // Atualizar os dados do usuário no Firestore
                                    usuarioDAO.atualizarUsuario(usuarioAtualizado)

                                    isUploading = false // Finalizar o upload

                                    navController.popBackStack() // Voltar após salvar
                                }
                            } catch (e: Exception) {
                                e.printStackTrace() // Logar o erro para facilitar o debug
                                isUploading = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Salvar Alterações")
                }


                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

