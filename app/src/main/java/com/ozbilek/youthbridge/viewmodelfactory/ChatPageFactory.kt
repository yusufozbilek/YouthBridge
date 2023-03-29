package com.ozbilek.youthbridge.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ozbilek.youthbridge.viewmodel.ChatPageViewModel
import com.ozbilek.youthbridge.viewmodel.LoginPageViewModel

class ChatPageFactory(var application: Application,private val id:String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatPageViewModel(application, id) as T
    }
}