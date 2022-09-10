package com.bura.chat.screens.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.screens.viewmodel.ui.SearchedUser
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val state = viewModel.uiState

    val visible = remember {
        mutableStateOf(false)
    }

    val user = remember {
        mutableStateOf(SearchedUser())
    }

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                is UiResponse.SearchUser -> {
                    user.value = SearchedUser(username = event.user.username, email = event.user.email)
                    visible.value = true
                }

                UiResponse.UserNotFound -> {
                    Toast.makeText(context, com.bura.chat.R.string.usernotfound, Toast.LENGTH_LONG).show()
                }

                UiResponse.ContactAlreadyAdded -> {
                    Toast.makeText(context, com.bura.chat.R.string.contactalreadyadded, Toast.LENGTH_LONG).show()
                }

                UiResponse.ContactSuccessfullyAdded -> {
                    Toast.makeText(context, com.bura.chat.R.string.contactsuccessfullyadded, Toast.LENGTH_LONG).show()
                }

                UiResponse.CannotAddYourself -> {
                    Toast.makeText(context, com.bura.chat.R.string.cannotaddyourself, Toast.LENGTH_LONG).show()
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
            ToolBarComposable(viewModel, state,navController)
            Spacer(modifier = Modifier.height(70.dp))
            SearchedUserComposable(viewModel, visible.value, user.value)
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarComposable(viewModel: MainViewModel, state: UiState, navController: NavController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    SearchComposable(viewModel = viewModel, state = state)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(
                        Screen.ContactsScreen.name) }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),

                actions = {
                    IconButton(onClick = {
                        viewModel.onEvent(UiEvent.SearchUser) }) {
                        Icon(Icons.Default.Search, "")
                    }
                }
            )
        }, content = {
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchComposable(viewModel: MainViewModel, state: UiState) {
    val focusManager = LocalFocusManager.current

    TextField(
        singleLine = true,
        value = state.addNewContact,
        onValueChange = { viewModel.onEvent(UiEvent.AddContactChanged(it)) },
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(com.bura.chat.R.string.search)) },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
    )
}

@Composable
private fun SearchedUserComposable(viewModel: MainViewModel, visible: Boolean, user: SearchedUser) {
    AnimatedVisibility(visible = visible) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment  =  Alignment.CenterHorizontally,
        ) {
            ProfileImageComposable(user)
            Spacer(modifier = Modifier.height(20.dp))
            UserInfoComposable(viewModel, user)
        }
    }
}

@Composable
private fun ProfileImageComposable(user: SearchedUser) {
    val image: Painter = painterResource(id = com.bura.chat.R.drawable.ic_launcher_background)
    Image(painter = image,contentDescription = "",
        modifier = Modifier
            .clip(CircleShape)
            .clickable { println("clicked") }

    )
}

@Composable
private fun UserInfoComposable(viewModel: MainViewModel,user: SearchedUser) {
    Text("Username: ${user.username}")
    Spacer(modifier = Modifier.height(20.dp))
    Text("Email: ${user.email}")
    Spacer(modifier = Modifier.height(20.dp))
    Button(onClick = {
        viewModel.onEvent(UiEvent.AddUserContact(user))
    }) {
        Text(text = "Add contact")
    }
}


