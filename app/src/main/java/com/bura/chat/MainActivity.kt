package com.bura.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bura.chat.net.websocket.ChatMessage
import com.bura.chat.screens.screen.*
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.util.Screen
import com.bura.chat.ui.theme.ChatTheme

class MainActivity : ComponentActivity() {

    // TODO: verify correct -> move to loginviewmodel
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: verify correct -> move to loginviewmodel
        mainViewModel.connectToChat()

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

                    composable(Screen.RecentChatScreen.name) {
                        RecentChatScreen(navController)
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

                    composable(
                        "${Screen.ChatScreen.name}/{title}",
                        arguments = listOf(navArgument("title") { type = NavType.StringType })
                    ) { backStackEntry ->
                        ChatScreen(navController, backStackEntry.arguments?.getString("title") ?: "", )
                    }
                }
            }
        }
    }


    // TODO: verify correct
    override fun onDestroy() {
        mainViewModel.webSocket.close(1000, "End of session")
        super.onDestroy()
    }
}
