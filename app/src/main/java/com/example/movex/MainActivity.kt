package com.example.movex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movex.telas.EditUserScreen
import com.example.movex.telas.HomeScreen
import com.example.movex.telas.LoginScreen
import com.example.movex.telas.MainScreen
import com.example.movex.telas.SignUpScreen
import com.example.movex.telas.UserDetailsScreen
import com.example.movex.ui.theme.MoveXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoveXTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen") {
        composable(
            "main_screen/{userId}",
            arguments = listOf(navArgument("userId") { defaultValue = -1 })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            MainScreen(navController = navController, userId = userId)
        }
        composable("login_screen") { LoginScreen(navController) }
        composable("signup_screen") { SignUpScreen(navController) }
        composable("home_screen") { HomeScreen(navController) }
        composable(
            "user_details_screen/{userId}",
            arguments = listOf(navArgument("userId") { defaultValue = -1 })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            UserDetailsScreen(navController = navController, userId = userId)
        }
        composable(
            "edit_user_screen/{userId}",
            arguments = listOf(navArgument("userId") { defaultValue = -1 })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            EditUserScreen(navController = navController, userId = userId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoveXTheme {
        AppNavigation()
    }
}
