package com.example.movex.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movex.R
import com.example.movex.model.Usuario
import com.example.movex.model.UsuarioDAO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val usuarioDAO = UsuarioDAO()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.movex_png),
            contentDescription = "MoveX Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Senha", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            visualTransformation = PasswordVisualTransformation()
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

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (password == confirmPassword) {
                    scope.launch {
                        val novoUsuario = Usuario(
                            id = null,
                            nome = username,
                            email = email,
                            senha = password
                        )

                        try {
                            usuarioDAO.adicionarUsuario(novoUsuario)
                            navController.navigate("login_screen")
                        } catch (e: Exception) {
                            errorMessage = "Erro ao criar a conta: ${e.message}"
                        }
                    }
                } else {
                    errorMessage = "As senhas não coincidem"
                }
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Cadastrar")
        }

        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Já tem uma conta?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            TextButton(
                onClick = {
                    navController.navigate("login_screen")
                },
            ) {
                Text(
                    text = "Faça login aqui!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
