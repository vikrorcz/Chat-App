package com.bura.chat.screens.recentchat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.bura.chat.util.UiResponse
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen
import org.koin.androidx.compose.getViewModel


//SHOWS RECENT CONTACTS
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentChatScreen(navController: NavController) {

    //val viewModel: MainViewModel = viewModel()
    val viewModel = getViewModel<RecentChatViewModel>()
    //val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.NavigateContactScreen -> {
                    navController.navigate(Screen.ContactsScreen.name)
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

            Scaffold(topBar = { ToolBarComposable(navController = navController) } ,
                floatingActionButton = {
                    FABComposable(onEvent = { viewModel.onEvent(RecentChatEvent.Contacts) })
                }, content = { } )

        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarComposable(navController: NavController) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chat")
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.AccountCircle,"")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),

                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.Menu, "")
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "Profile") }, onClick = { navController.navigate(
                            Screen.ProfileScreen.name) })
                        DropdownMenuItem(text = { Text(text = "Settings") }, onClick = { navController.navigate(
                            Screen.SettingsScreen.name) })
                        DropdownMenuItem(text = { Text(text = "Privacy policy") }, onClick = { /*TODO*/ })
                        DropdownMenuItem(text = { Text(text = "About") }, onClick = { /*TODO*/ })
                    }
                }
            )
        }, content = { }
    )
}
/*
@Composable
private fun ContactItemComposable() {
    Row {
        Column {
            Text(text = "CONTACT", style = typography.headlineMedium)
            Text(text = "VIEW DETAIL", style = typography.titleSmall)
        }
    }
}*/

@Composable
private fun FABComposable(onEvent: () -> Unit) {
    FloatingActionButton(onClick = {
        onEvent()
    } ){
        Icon(Icons.Filled.Message,"")
    }
}


