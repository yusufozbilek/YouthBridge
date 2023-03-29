package com.ozbilek.youthbridge.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SplashPage(navController: NavController){
    val auth = Firebase.auth
    val current = auth.currentUser
    if (current !=null){
        navController.navigate("MainPage")
    }else{
        navController.navigate("RegisterPage")
    }
}