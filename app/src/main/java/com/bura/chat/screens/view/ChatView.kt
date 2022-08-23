package com.bura.chat.screens.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.bura.chat.util.Screen
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.ui.theme.ChatTheme

@Composable
fun ChatView(navController: NavController, viewModel: MainViewModel) {
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
               Text("Username: ")
            }
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

