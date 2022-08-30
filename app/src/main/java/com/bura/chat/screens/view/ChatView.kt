package com.bura.chat.screens.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.navigation.NavController
import com.bura.chat.util.Screen
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.ui.theme.ChatTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatView(navController: NavController, viewModel: MainViewModel) {

    ChatTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,

        ) {
            //ToolBarComposable(navController)
            //FABComposable()
            Scaffold(topBar = { ToolBarComposable(navController = navController) } ,
                //floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    FABComposable()
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
            SmallTopAppBar(
                title = {
                    Text(text = "Chat")
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.AccountCircle,"")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Green),

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
        }, content = {
        })
}

@Composable
fun ContactItemComposable() {
    Row {
        Column {
            Text(text = "CONTACT", style = typography.headlineMedium)
            Text(text = "VIEW DETAIL", style = typography.titleSmall)
        }
    }
}

@Composable
fun FABComposable() {
    FloatingActionButton(onClick = { } ){
        Icon(Icons.Filled.Add,"")
    }
}
