package com.bura.chat.screens.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
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
fun ChatScreen(navController: NavController, title: String,) {

    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val state = viewModel.uiState

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {

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
            ToolBarComposable(title, state, viewModel, navController)

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment  =  Alignment.CenterHorizontally,
            ) {
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarComposable(title: String, state: UiState, viewModel: MainViewModel,navController: NavController) {
    Scaffold(

        bottomBar = {
            BottomAppBar(

                containerColor = MaterialTheme.colorScheme.primaryContainer,

                actions = {
                    SendMessageComposable(viewModel = viewModel, state = state)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        viewModel.sendMessage(viewModel.uiState.message)
                    }) {
                        Icon(Icons.Default.Send, "")
                    }
                }
            )
        },

        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = title)

                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ContactsScreen.name) }) {
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
private fun SendMessageComposable(viewModel: MainViewModel,state: UiState) {
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = state.message,
        onValueChange = { viewModel.onEvent(UiEvent.MessageChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.sendmessage)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
    )
}
