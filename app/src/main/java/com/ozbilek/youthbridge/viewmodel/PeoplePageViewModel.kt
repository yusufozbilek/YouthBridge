package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ozbilek.youthbridge.entity.Contact
import com.ozbilek.youthbridge.repo.CloudFirestoreRepository
import com.ozbilek.youthbridge.repo.FirebaseAuthRepository

class PeoplePageViewModel(application: Application): AndroidViewModel(application){
    private val fireRepo = CloudFirestoreRepository()
    private val authRepo = FirebaseAuthRepository()
    private val userEmail = authRepo.userMail

    var contactList = MutableLiveData<List<Contact>>()
    var imageMap = MutableLiveData<List<String>>()
    init {
        contactList = MutableLiveData(listOf())
        imageMap = MutableLiveData(listOf())
    }



    fun addProvider(contactType:String){
        fireRepo.addContact(contactType,userEmail!!)
    }
    fun getProviders() {
        fireRepo.getContact(userEmail!!)

        contactList = fireRepo.providerList

    }
    /*
    fun getProviders(){
            fireRepo.getContact(userEmail!!)
            contactList = fireRepo.providerList
            getStandardImage()
    }

     */



}