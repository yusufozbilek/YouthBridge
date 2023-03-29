package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ozbilek.youthbridge.entity.AcademyItem
import com.ozbilek.youthbridge.entity.WebSourceItem
import com.ozbilek.youthbridge.repo.CloudFirestoreRepository
import com.ozbilek.youthbridge.repo.FirebaseAuthRepository

class MainPageViewModel(application: Application): AndroidViewModel(application) {
    private val authRepo = FirebaseAuthRepository()
    private val firestore = CloudFirestoreRepository()
    var cardContentList = MutableLiveData<List<AcademyItem>>()
    var webContentList = MutableLiveData<List<WebSourceItem>>()

    init {
        cardContentList = MutableLiveData()
        webContentList = MutableLiveData()
        getAcademy()
        getWebContent()
    }
    fun signOut(navController: NavController, route:String, backstack:String){
        authRepo.signOutCurrentUser(navController,route,backstack)
    }

    fun getAcademy(){
        firestore.getAcademy()
        cardContentList = firestore.academyList
    }

    fun getWebContent(){
        firestore.getWebContent()
        webContentList = firestore.webSourceList
    }
}