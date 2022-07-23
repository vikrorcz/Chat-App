package com.bura.chat

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bura.chat.screens.util.Screen
import com.bura.chat.screens.view.ChatView
import com.bura.chat.screens.view.LoginView
import com.bura.chat.screens.view.RegistrationView
import com.bura.chat.screens.viewmodel.ChatViewModel
import com.bura.chat.screens.viewmodel.LoginViewModel
import com.bura.chat.screens.viewmodel.RegistrationViewModel
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
