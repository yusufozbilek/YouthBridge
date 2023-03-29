package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.ozbilek.youthbridge.repo.FirebaseAuthRepository

class LoginPageViewModel(application: Application): AndroidViewModel(application){
    private val authRepo = FirebaseAuthRepository()
    fun login(email:String, password:String, navController: NavController, route:String){
        authRepo.loginUserWithEmailAndPassword(email,password,navController,route)
    }
}