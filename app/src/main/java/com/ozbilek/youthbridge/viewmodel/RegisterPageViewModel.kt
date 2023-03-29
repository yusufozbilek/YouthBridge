package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.ozbilek.youthbridge.repo.CloudFirestoreRepository
import com.ozbilek.youthbridge.repo.FirebaseAuthRepository


class RegisterPageViewModel(application: Application): AndroidViewModel(application) {
    private val authRepo = FirebaseAuthRepository()
    private val fireRepo = CloudFirestoreRepository()
    fun register(username:String,email:String,password:String,navController: NavController,route:String){
        authRepo.registerUserWithEmailAndPassword(email,password,navController,route)
            .apply {
                Log.i("Firestore","Registered")
                fireRepo.registerUserToFirestore(email, profilePhoto = "",username, isOnline = true, isVerified = true)
                Log.i("Firestore","Repository works fine")
            }
    }
}