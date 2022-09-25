package com.bura.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bura.chat.screens.addcontact.AddContactScreen
import com.bura.chat.screens.chat.ChatScreen
import com.bura.chat.screens.contacts.ContactsScreen
import com.bura.chat.screens.login.LoginScreen
import com.bura.chat.screens.profile.ProfileScreen
import com.bura.chat.screens.recentchat.RecentChatScreen
import com.bura.chat.screens.register.RegistrationScreen
import com.bura.chat.screens.settings.SettingsScreen
import com.bura.chat.util.Screen
import com.bura.chat.ui.theme.ChatTheme

class MainActivity : ComponentActivity() {

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
}
