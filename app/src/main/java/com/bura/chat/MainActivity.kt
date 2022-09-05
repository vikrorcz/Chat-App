package com.bura.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bura.chat.screens.screen.*
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
                        LoginScreen(navController)
                    }

                    composable(Screen.RegistrationScreen.name) {
                        RegistrationScreen(navController)
                    }

                    composable(Screen.ChatScreen.name) {
                        ChatScreen(navController)
                    }

                    composable(Screen.SettingsScreen.name) {
                        SettingsScreen(navController)
                    }

                    composable(Screen.ProfileScreen.name) {
                        ProfileScreen(navController)
                    }

                    composable(Screen.ContactsScreen.name) {
                        ContactsScreen(navController)
                    }

                    composable(Screen.AddContactScreen.name) {
                        AddContactScreen(navController)
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
