package com.bura.chat.screens.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen

//SHOWS ALL CONTACTS + options to create group chat or add contact
@Composable
fun ContactsScreen(navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.NavigateAddContactScreen -> {
                    navController.navigate(Screen.AddContactScreen.name)
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

            ToolBarComposable(navController = navController)

            Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //items(1) {
                    Spacer(modifier = Modifier.height(70.dp))
                    CardComposable(
                        onNewContact = { viewModel.onEvent(UiEvent.AddContact) },
                        "Add a new contact", R.drawable.ic_baseline_person_add_24)

                    Spacer(modifier = Modifier.height(5.dp))

                    CardComposable(
                        onNewContact = { viewModel.onEvent(UiEvent.AddContact) },
                        "Add a new group", R.drawable.ic_baseline_group_add_24)
                //}
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
                    Text(text = "Contacts")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(
                        Screen.ChatScreen.name) }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),

                actions = {

                }
            )
        }, content = { })
}


@Composable
private fun CardComposable(onNewContact: () -> Unit,text: String, id: Int){
    Card(modifier = Modifier
        .width(380.dp)
        .height(60.dp)
        .clickable {
            println("you clicked $text")
            val clickedItem = text
            when (text) {
                "Add a new contact" -> {
                    onNewContact()
                //viewModel.onEvent(UiEvent.AddContact)
                }

                "Add a new group" -> {

                }
            }
        }) {
        Row(modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.height(5.dp))
            ContactImageComposable(id)
            Text(text, Modifier.padding(16.dp), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ContactImageComposable(id: Int) {
    val image = painterResource(id = id)
    Image(painter = image,
        contentDescription = "",
        alignment = Alignment.BottomEnd,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .clip(CircleShape)
            .size(40.dp))
}
