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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatTheme {
                val navController = rememberNavController()

                NavHost(navController, Screen.LoginScreen.name) {//starting destination
                    composable(Screen.LoginScreen.name) {
                        val loginViewModel: LoginViewModel by viewModels()
                        LoginView(navController, loginViewModel)
                    }

                    composable(Screen.RegistrationScreen.name) {
                        val registrationViewModel: RegistrationViewModel by viewModels()
                        RegistrationView(navController, registrationViewModel)
                    }

                    composable(Screen.ChatScreen.name) {
                        val chatViewModel: ChatViewModel by viewModels()
                        ChatView(navController, chatViewModel)
                    }

                    composable(Screen.SettingsScreen.name) {
                        val settingsViewModel: SettingsViewModel by viewModels()
                        SettingsView(navController, settingsViewModel)
                    }

                    composable(Screen.ProfileScreen.name) {
                        val profileViewModel: ProfileViewModel by viewModels()
                        ProfileView(navController, profileViewModel)
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
