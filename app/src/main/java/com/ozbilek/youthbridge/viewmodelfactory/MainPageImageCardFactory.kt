package com.ozbilek.youthbridge.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ozbilek.youthbridge.viewmodel.MainPageImageCardViewModel
import com.ozbilek.youthbridge.viewmodel.RegisterPageViewModel

class MainPageImageCardFactory(var application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainPageImageCardViewModel(application) as T
    }
}