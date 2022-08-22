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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.data.UserPreferences
import com.bura.chat.util.Screen
import com.bura.chat.util.TextComposable
import com.bura.chat.screens.viewmodel.LoginViewModel
import com.bura.chat.ui.theme.ChatTheme
import kotlinx.coroutines.launch

@Composable
fun LoginView(navController: NavController, viewModel: LoginViewModel) {

    viewModel.userPreferences = UserPreferences(LocalContext.current)


    val context = LocalContext.current

    /*
    if (viewModel.userPreferences.getBooleanPref(UserPreferences.Prefs.rememberme)) {
        LaunchedEffect(Unit) {

            //navController.navigate(Screen.ChatScreen.name)
            viewModel.autoLoginAccount(navController)

            viewModel.viewModelScope.launch {
                viewModel.message.collect { toastMessage ->
                    Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
     */
    if (viewModel.userPreferences.getBooleanPref(UserPreferences.Prefs.rememberme)) {
        //Launched effect fixes screen flicker
        LaunchedEffect(Unit) {
            viewModel.autoLoginAccount(navController)

            viewModel.viewModelScope.launch {
                viewModel.message.collect { toastMessage ->
                    Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
                }
            }
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
                UsernameComposable(viewModel = viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                PasswordComposable(viewModel = viewModel)
                Spacer(modifier = Modifier.height(40.dp))
                KeepMeLoggedInComposable(viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                ButtonComposable(viewModel = viewModel, navController = navController)
                Spacer(modifier = Modifier.height(120.dp))
                CreateAccountComposable(viewModel = viewModel, navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernameComposable(viewModel: LoginViewModel) {
    var username by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = username,
        onValueChange = { username = it },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.usernameoremail)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
    )
    viewModel.setUsername(username)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordComposable(viewModel: LoginViewModel) {
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
private fun ButtonComposable(viewModel: LoginViewModel, navController: NavController) {
    val myContext = LocalContext.current

    Button(onClick = {
        login(myContext, viewModel, navController)
    }) {
        Text(text = "Login")
    }
}

private fun login(context: Context, viewModel: LoginViewModel, navController: NavController) {
    if (viewModel.username.value.isEmpty()) {
        Toast.makeText(context, context.getString(R.string.invalidusername), Toast.LENGTH_LONG).show()
        return
    }

    viewModel.loginAccount(navController)

    viewModel.viewModelScope.launch {
        viewModel.message.collect { toastMessage ->
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }
    }
}


@Composable
private fun CreateAccountComposable(viewModel: LoginViewModel, navController: NavController) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KeepMeLoggedInComposable(viewModel: LoginViewModel) {
    val myContext = LocalContext.current

    val checkedState = remember { mutableStateOf(false) }

    Row {
        Checkbox(
            checked = checkedState.value,
            modifier = Modifier,
            onCheckedChange = { checkedState.value = it },
        )

        Text(text = "Keep me logged in", Modifier.padding(12.dp))
    }

    //userPreferences.edit { prefs -> prefs[Keys.HIDE_VISITED] = hideVisited }
   //viewModel.keepMeLoggedIn(userPreferences, checkedState.value)
    viewModel.setRememberMe(checkedState.value)

}
