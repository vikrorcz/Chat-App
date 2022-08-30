package com.bura.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bura.chat.screens.view.*
import com.bura.chat.util.Screen
import com.bura.chat.screens.viewmodel.*
import com.bura.chat.ui.theme.ChatTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatTheme {
                val navController = rememberNavController()

                NavHost(navController, Screen.LoginScreen.name) {//starting destination
                    composable(Screen.LoginScreen.name) {
                        LoginView(navController, mainViewModel)
                    }

                    composable(Screen.RegistrationScreen.name) {
                        RegistrationView(navController, mainViewModel)
                    }

                    composable(Screen.ChatScreen.name) {
                        ChatView(navController, mainViewModel)
                    }

                    composable(Screen.SettingsScreen.name) {
                        SettingsView(navController, mainViewModel)
                    }

                    composable(Screen.ProfileScreen.name) {
                        ProfileView(navController, mainViewModel)
                    }
                    //composable(
                    //    "${Screen.UserScreen.name}/{username}",
                    //    arguments = listOf(navArgument("username") { type = NavType.StringType })
                    //) { backStackEntry ->
                    //    val viewModel = UserViewModel(backStackEntry.arguments?.getString("username") ?: "")
                    //    UserView(navController, viewModel)
                    //}
                }
            }
        }
    }
}
