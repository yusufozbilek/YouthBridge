package com.ozbilek.youthbridge.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ozbilek.youthbridge.viewmodel.ImageCardViewModel
import com.ozbilek.youthbridge.viewmodel.WebContentViewModel

class WebContentFactory(var application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WebContentViewModel(application) as T
    }
}