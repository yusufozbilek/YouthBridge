package com.ozbilek.youthbridge.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ozbilek.youthbridge.viewmodel.FirstTimePageViewModel
import com.ozbilek.youthbridge.viewmodel.RegisterPageViewModel

class RegisterPageFactory(var application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterPageViewModel(application) as T
    }
}