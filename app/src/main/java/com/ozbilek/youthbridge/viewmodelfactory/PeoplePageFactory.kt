package com.ozbilek.youthbridge.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ozbilek.youthbridge.viewmodel.PeoplePageViewModel
import com.ozbilek.youthbridge.viewmodel.RegisterPageViewModel

class PeoplePageFactory(var application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PeoplePageViewModel(application) as T
    }
}