package com.bura.chat.screens.login

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
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.util.UiResponse
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen
import com.bura.chat.util.TextComposable
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel = getViewModel<LoginViewModel>()
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.UsernameError -> {
                    Toast.makeText(context, R.string.invalidusername, Toast.LENGTH_LONG).show()
                }

                UiResponse.ConnectionFail -> {
                    Toast.makeText(context, R.string.connectionfail, Toast.LENGTH_LONG).show()
                }

                UiResponse.TokenExpired -> {
                    Toast.makeText(context, R.string.tokenexpired, Toast.LENGTH_LONG).show()
                }

                UiResponse.LoginSuccess -> {
                    navController.navigate(Screen.RecentChatScreen.name)
                }

                UiResponse.InvalidCredentials -> {
                    Toast.makeText(context, R.string.invalidcredentials, Toast.LENGTH_LONG).show()
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

                TextComposable(text = stringResource(R.string.login))
                Spacer(modifier = Modifier.height(20.dp))
                UsernameComposable(viewModel, state)
                Spacer(modifier = Modifier.height(20.dp))
                PasswordComposable(viewModel, state)
                Spacer(modifier = Modifier.height(40.dp))
                KeepMeLoggedInComposable(viewModel, state)
                Spacer(modifier = Modifier.height(20.dp))
                ButtonComposable(onEvent = { viewModel.onEvent(LoginEvent.Login)  }, navController = navController)
                Spacer(modifier = Modifier.height(120.dp))
                CreateAccountComposable(navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameComposable(viewModel: LoginViewModel,state: LoginState) {
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = state.loginUsername,
        onValueChange = { viewModel.onEvent(LoginEvent.LoginUsernameChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.usernameoremail)) },
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
private fun PasswordComposable(viewModel: LoginViewModel,state: LoginState) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.loginPassword,
        onValueChange = { viewModel.onEvent(LoginEvent.LoginPasswordChanged(it)) },
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
        Text(text = "Login")
    }
}

@Composable
private fun CreateAccountComposable(navController: NavController) {
    ClickableText(
        style = TextStyle(
            color = Color.Blue,
        ),
        text = AnnotatedString(stringResource(R.string.create)),
        onClick = {
            navController.navigate(Screen.RegistrationScreen.name)
        }
    )
}

@Composable
private fun KeepMeLoggedInComposable(viewModel: LoginViewModel, state: LoginState) {
    Row {
        Checkbox(
            checked = state.rememberMe,
            modifier = Modifier,
            onCheckedChange = {
                viewModel.onEvent(LoginEvent.RememberMeChanged(it))},
        )
        Text(text = "Keep me logged in", Modifier.padding(12.dp))
    }
}
