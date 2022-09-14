package com.bura.chat.screens.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen

@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.NavigateLoginScreen -> {
                    navController.navigate(Screen.LoginScreen.name)
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
            ToolBarComposable(navController)

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment  =  Alignment.CenterHorizontally,
            ) {
                ProfileImageComposable()
                LogoutButtonComposable(onEvent = { viewModel.onEvent(UiEvent.Logout) })
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarComposable(navController: NavController) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Profile")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(
                        Screen.RecentChatScreen.name) }) {
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

@Composable
private fun ProfileImageComposable() {
    val image: Painter = painterResource(id = R.drawable.ic_launcher_background)
    Image(painter = image,contentDescription = "",
    modifier = Modifier
        .clip(CircleShape)
        .clickable { println("clicked") })
}

@Composable
private fun LogoutButtonComposable(onEvent: () -> Unit) {
    Button(onClick = {
        onEvent()
        //val userPreferences = UserPreferences(myContext)
        //userPreferences.setPref(UserPreferences.Prefs.rememberme, false)
        //navController.navigate(Screen.LoginScreen.name)
    }) {
        Text(text = "Logout")
    }
}

