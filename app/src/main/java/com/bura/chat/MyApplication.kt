package com.bura.chat

import android.app.Application
import com.bura.chat.di.myModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(myModule)
        }
    }
}