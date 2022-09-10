package com.bura.chat.screens.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
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
import com.bura.chat.util.TextComposable

@Composable
fun RegistrationScreen(navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.UsernameError -> {
                    Toast.makeText(context, R.string.invalidusername, Toast.LENGTH_LONG).show()
                }

                UiResponse.PasswordError -> {
                    Toast.makeText(context, R.string.invalidpassword, Toast.LENGTH_LONG).show()
                }

                UiResponse.EmailError -> {
                    Toast.makeText(context, R.string.invalidemail, Toast.LENGTH_LONG).show()
                }

                UiResponse.RegistrationSuccess -> {
                    Toast.makeText(context, R.string.registersuccess, Toast.LENGTH_LONG).show()
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
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment  =  Alignment.CenterHorizontally,
            ) {
                TextComposable(text = stringResource(R.string.registration))
                Spacer(modifier = Modifier.height(20.dp))
                EmailComposable(state,viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                UsernameComposable(state,viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                PasswordComposable(state,viewModel = viewModel)
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComposable(onEvent = { viewModel.onEvent(UiEvent.Register) }, navController = navController)
                Spacer(modifier = Modifier.height(120.dp))
                LoginAccountComposable(viewModel, navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameComposable(state: UiState, viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = state.registerUsername,
        onValueChange = { viewModel.onEvent(UiEvent.RegisterUsernameChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.username)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailComposable(state: UiState,viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = state.registerEmail,
        onValueChange = { viewModel.onEvent(UiEvent.RegisterEmailChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.email)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordComposable(state: UiState,viewModel: MainViewModel) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.registerPassword,
        onValueChange = { viewModel.onEvent(UiEvent.RegisterPasswordChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.password)) },
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
private fun ButtonComposable(onEvent: () -> Unit, navController: NavController) {
    Button(onClick = {
        onEvent()
    }) {
        Text(text = stringResource(R.string.register))
    }
}


@Composable
private fun LoginAccountComposable(viewModel: MainViewModel, navController: NavController) {
    ClickableText(
        style = TextStyle(
            color = Color.Blue,
        ),
        text = AnnotatedString(stringResource(R.string.alreadyhaveacc)),
        onClick = {
            navController.navigate(Screen.LoginScreen.name)
        }
    )
}
