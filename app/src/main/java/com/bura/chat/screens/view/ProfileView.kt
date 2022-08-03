package com.bura.chat.screens.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.screens.viewmodel.LoginViewModel
import com.bura.chat.screens.viewmodel.ProfileViewModel
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.screens.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileView (navController: NavController, viewModel: ProfileViewModel) {
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
                LogoutButtonComposable(navController = navController, viewModel = viewModel)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolBarComposable(navController: NavController) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Profile")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(
                        Screen.ChatScreen.name) }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Green),

                actions = {

                }
            )
        }, content = {
        })
}

@Composable
private fun ProfileImageComposable() {
    //IconButton(onClick = {  }) {
    //    Icon(Icons.Default.AccountCircle, "",
    //        modifier = Modifier.size(128.dp))
    //}
    val image: Painter = painterResource(id = R.drawable.ic_launcher_background)
    Image(painter = image,contentDescription = "",
    modifier = Modifier
        .clip(CircleShape)
        .clickable { println("clicked") })

}

@Composable
private fun LogoutButtonComposable(navController: NavController ,viewModel: ProfileViewModel) {
    val myContext = LocalContext.current

    Button(onClick = {
        val userPreferences = UserPreferences(myContext)
        userPreferences.setPref(UserPreferences.Prefs.rememberme, false)
        navController.navigate(Screen.LoginScreen.name)

    }) {
        Text(text = "Logout")
    }
}

