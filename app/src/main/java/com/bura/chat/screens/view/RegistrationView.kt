package com.bura.chat.screens.view

import android.content.Context
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.util.Screen
import com.bura.chat.util.TextComposable
import com.bura.chat.util.isEmailValid
import com.bura.chat.screens.viewmodel.RegistrationViewModel
import com.bura.chat.ui.theme.ChatTheme
import kotlinx.coroutines.launch

@Composable
fun RegistrationView(navController: NavController, viewModel: RegistrationViewModel) {

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
                EmailComposable(viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                UsernameComposable(viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                PasswordComposable(viewModel = viewModel)
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComposable(viewModel = viewModel, navController = navController)
                Spacer(modifier = Modifier.height(120.dp))
                LoginAccountComposable(viewModel = viewModel, navController)
            }
        }
    }
}


@Composable
private fun UsernameComposable(viewModel: RegistrationViewModel) {
    var username by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = username,
        onValueChange = { username = it },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.username)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
    )
    viewModel.setUsername(username)
}

@Composable
private fun EmailComposable(viewModel: RegistrationViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = email,
        onValueChange = { email = it },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.email)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
    )
    viewModel.setEmail(email)
}


@Composable
private fun PasswordComposable(viewModel: RegistrationViewModel) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        value = password,
        onValueChange = { password = it },
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
    viewModel.setPassword(password)
}

@Composable
private fun ButtonComposable(viewModel: RegistrationViewModel, navController: NavController) {
    val myContext = LocalContext.current

    Button(onClick = {
        register(myContext, viewModel)
    }) {
        Text(text = stringResource(R.string.register))
    }
}


@Composable
private fun LoginAccountComposable(viewModel: RegistrationViewModel, navController: NavController) {
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


private fun register(context: Context, viewModel: RegistrationViewModel){
    if (!viewModel.email.value.isEmailValid()) {
        Toast.makeText(context, context.getString(R.string.invalidemail), Toast.LENGTH_LONG).show()
        return
    }

    if (viewModel.username.value.isEmpty()) {
        Toast.makeText(context, context.getString(R.string.invalidusername), Toast.LENGTH_LONG).show()
        return
    }

    if (viewModel.password.value.length < 5) {
        Toast.makeText(context, context.getString(R.string.invalidpassword), Toast.LENGTH_LONG).show()
        return
    }

    viewModel.registerAccount()

    viewModel.viewModelScope.launch {
        viewModel.message.collect { toastMessage ->
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }
    }
}

