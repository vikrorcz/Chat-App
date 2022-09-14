package com.bura.chat.screens.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen

@Composable
fun SettingsScreen(navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.ConnectionFail -> {
                    Toast.makeText(context, R.string.connectionfail, Toast.LENGTH_LONG).show()
                }

                UiResponse.ChangePasswordSuccess -> {
                    Toast.makeText(context, R.string.changepasswordsuccess, Toast.LENGTH_LONG).show()
                }

                UiResponse.ChangePasswordFail -> {
                    Toast.makeText(context, R.string.changepasswordfail, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
            //required, otherwise it wouldn't collect the state on next occasion
            viewModel.uiResponse.emit(UiResponse.Null)
        }
    }

    ChatTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,

            ) {
            ToolBarComposable(navController)

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment  =  Alignment.CenterHorizontally,
            ) {

                Text(text = "Change password")
                Spacer(modifier = Modifier.height(20.dp))
                CurrentPasswordComposable(state, viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                NewPasswordComposable(state, viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                ChangePasswordButtonComposable(onEvent = { viewModel.onEvent(UiEvent.ChangePassword)} )
            }

        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarComposable(navController: NavController) {
    //var showMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(
                        Screen.RecentChatScreen.name) }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),

                actions = {

                }
            )
        }, content = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentPasswordComposable(state: UiState,viewModel: MainViewModel) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.settingsCurrentPassword,
        onValueChange = { viewModel.onEvent(UiEvent.CurrentPasswordChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text("Current password") },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),

        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, "")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewPasswordComposable(state: UiState, viewModel: MainViewModel) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.settingsNewPassword,
        onValueChange = { viewModel.onEvent(UiEvent.NewPasswordChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text("New password") },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),

        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, "")
            }
        }
    )
}

@Composable
private fun ChangePasswordButtonComposable(onEvent: () -> Unit) {
    Button(onClick = {
        onEvent()
    }) {
        Text(text = "Change password")
    }
}

