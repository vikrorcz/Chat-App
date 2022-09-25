package com.bura.chat.screens.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bura.chat.R
import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.screens.viewmodel.MainViewModel
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.ui.theme.ChatTheme
import com.bura.chat.util.Screen
import org.koin.androidx.compose.getViewModel

//SHOWS ALL CONTACTS + options to create group chat or add contact
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsScreen(navController: NavController) {

    //val viewModel: MainViewModel = viewModel()
    val viewModel = getViewModel<MainViewModel>()
    val state = viewModel.uiState
    val context = LocalContext.current

    val contactList = remember {
        mutableStateListOf<Contact>()
    }

    LaunchedEffect(viewModel, context) {

        contactList.addAll(viewModel.getContactList())

        viewModel.uiResponse.collect { event ->
            when (event) {
                UiResponse.NavigateAddContactScreen -> {
                    navController.navigate(Screen.AddContactScreen.name)
                }

                is UiResponse.DeleteUserFromList -> {
                   contactList.remove(event.contact)
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

                Spacer(modifier = Modifier.height(70.dp))
                CardComposable(
                    -2,
                    viewModel,
                    navController,
                    "Add a new contact",
                    R.drawable.ic_baseline_person_add_24
                )

                Spacer(modifier = Modifier.height(5.dp))

                CardComposable(
                    -1,
                    viewModel,
                    navController,
                    "Add a new group",
                    R.drawable.ic_baseline_group_add_24
                )

                LazyColumn {
                    items(contactList) { //data ->
                        Spacer(modifier = Modifier.height(5.dp))
                        //Modifier.animateItemPlacement() todo animate the list when inserting or removing contact
                        CardComposable(it.id, viewModel,  navController, text = it.username, id = R.drawable.ic_baseline_person_24)
                    }
                }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardComposable(index: Int, viewModel: MainViewModel, navController: NavController, text: String, id: Int){
    var showMenu by rememberSaveable { mutableStateOf(false) }

    Card(modifier = Modifier
        //.width(380.dp)
        .width(LocalConfiguration.current.screenWidthDp.dp - 10.dp)
        .height(60.dp)
        .clickable {

            when (index) {
                -2 -> {
                    viewModel.onEvent(UiEvent.AddContact)
                    println("you clicked $text")
                }

                -1 -> {
                    // TODO: add group
                    println("you clicked $text")
                }

                else -> {
                    navController.navigate("${Screen.ChatScreen.name}/${text}")
                }
            }
        }) {
        Row(modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.height(5.dp))
            ContactImageComposable(id)
            Text(text, Modifier.padding(16.dp), textAlign = TextAlign.Center)


            //for contact cards create a dropdown menu
            if (index >= 0) {
                Spacer(modifier = Modifier.weight(1f))//fill available space

                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.List, "")
                }

                DropdownMenu(
                    offset = DpOffset(x = (-10).dp, y = 0.dp),//offset position to locate menu on the right side
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(text = { Text(text = "Send message") }, onClick = { /*TODO*/})
                    DropdownMenuItem(text = { Text(text = "Call") }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = "Delete contact") }, onClick = {
                        viewModel.onEvent(UiEvent.DeleteUserContact(text))
                    })
                }
            }
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
