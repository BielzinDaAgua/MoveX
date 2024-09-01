package com.example.movex.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movex.model.Usuario
import com.example.movex.model.UsuarioDAO
import kotlinx.coroutines.launch

@Composable
fun EditUserScreen(navController: NavController, userId: Int) {
    var usuario by remember { mutableStateOf<Usuario?>(null) }
    val usuarioDAO = UsuarioDAO()
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }


    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        usuario = usuarioDAO.obterUsuarioPorId(userId)
        usuario?.let {
            nome = it.nome ?: ""
            email = it.email ?: ""
            senha = it.senha ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Editar Informações",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )


        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
                    errorMessage = "Todos os campos são obrigatórios."
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorMessage = "Formato de e-mail inválido."
                } else {
                    scope.launch {
                        try {
                            usuario?.let {
                                usuarioDAO.atualizarUsuario(it.copy(nome = nome, email = email, senha = senha))
                                navController.navigate("main_screen/$userId")
                            }
                        } catch (e: Exception) {
                            errorMessage = "Erro ao atualizar usuário: ${e.message}"
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Salvar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Cancelar")
        }
    }
}
