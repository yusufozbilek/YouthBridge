package com.ozbilek.youthbridge.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ozbilek.youthbridge.viewmodel.ImageCardViewModel

class ImageCardFactory(var application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImageCardViewModel(application) as T
    }
}