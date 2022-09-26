package com.bura.chat.di

import com.bura.chat.data.room.contacts.ContactDatabase
import com.bura.chat.data.room.messages.MessageDatabase
import com.bura.chat.net.RestClient
import com.bura.chat.repository.ContactRepository
import com.bura.chat.repository.MessageRepository
import com.bura.chat.repository.ServerRepository
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.screens.addcontact.AddContactViewModel
import com.bura.chat.screens.chat.ChatViewModel
import com.bura.chat.screens.contacts.ContactsViewModel
import com.bura.chat.screens.login.LoginViewModel
import com.bura.chat.screens.profile.ProfileViewModel
import com.bura.chat.screens.recentchat.RecentChatViewModel
import com.bura.chat.screens.register.RegisterViewModel
import com.bura.chat.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {

    single {
        UserPrefsRepository(get())
    }

    single {
        ContactRepository(get())
    }

    single {
        MessageRepository(get())
    }

    single {
        ServerRepository(get())
    }

    single {
        ContactDatabase.getInstance(androidContext()).contactDao()
    }

    single {
        MessageDatabase.getInstance(androidContext()).messageDao()
    }

    single {
        RestClient.getInstance(get())
    }

    viewModel {
        LoginViewModel(get(), get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        AddContactViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        ProfileViewModel(get())
    }

    viewModel {
        ContactsViewModel(get(), get())
    }

    viewModel {
        RecentChatViewModel()
    }

    viewModel {
        ChatViewModel(get(), get())
    }
}