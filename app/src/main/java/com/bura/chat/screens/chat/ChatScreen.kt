package com.bura.chat.screens.screen.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.data.room.messages.Message
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen
import org.koin.androidx.compose.getViewModel

@Composable
fun ChatScreen(navController: NavController, title: String) {

    //val viewModel: MainViewModel = viewModel()
    val viewModel = getViewModel<MainViewModel>()
    val context = LocalContext.current
    val state = viewModel.uiState

    val listState = rememberLazyListState()

    val messageList = remember {
        mutableStateListOf<Message>()
    }

    LaunchedEffect(viewModel, context) {

        messageList.addAll(viewModel.getMessageListFromSender(title))
        listState.scrollToItem(messageList.size)

        viewModel.uiResponse.collect { event ->
            when (event) {
                is UiResponse.AddMessageToList -> {
                    messageList.add(event.message)
                    listState.animateScrollToItem(messageList.size)
                }
                else -> {}
            }
            //required, otherwise it wouldn't collect the state on next occasion
            viewModel.uiResponse.emit(UiResponse.Null)
        }
    }

    viewModel.connectToChat()

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
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp)
                        .height(LocalConfiguration.current.screenHeightDp.dp - 140.dp)
                        .offset(0.dp, (-7).dp)
                ) {
                    items(messageList) { //data ->
                        Spacer(modifier = Modifier.height(5.dp))
                        CardComposable(text = it.message, sender = it.sendingUser, user = viewModel.getCurrentUser())
                    }
                }
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
                        viewModel.onEvent(UiEvent.SendMessage(title))
                    }) {
                        Icon(Icons.Default.Send, "")
                    }
                }
            )
        },

        topBar = {
            TopAppBar(
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


@Composable
private fun CardComposable(text: String, sender: String, user: String) {
    //offset messages sent by the user to the right
    var contentAlignment = Alignment.CenterStart
    var background = MaterialTheme.colorScheme.primaryContainer
    if (sender == user) {
        contentAlignment = Alignment.CenterEnd
        background = com.bura.chat.ui.theme.OwnMessageColor
    }
    Box(
        contentAlignment = contentAlignment,
        modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .wrapContentWidth(),
            colors = CardDefaults.cardColors(
                containerColor = background,
            ),
        ) {
            Row(modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text, Modifier.padding(16.dp), textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}