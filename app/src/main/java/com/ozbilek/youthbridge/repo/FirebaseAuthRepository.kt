package com.ozbilek.youthbridge.repo

import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthRepository {
    private val auth:FirebaseAuth = Firebase.auth
    val userMail = auth.currentUser?.email
    fun registerUserWithEmailAndPassword(email:String,password:String,navController: NavController,route:String):Boolean{
        var success = false
        auth.createUserWithEmailAndPassword(email.trim(),password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)navController.navigate(route).also { success = true }
                success = true
            }.addOnFailureListener {
                success = false
            }
        return success
    }



    fun loginUserWithEmailAndPassword(email:String,password:String,navController: NavController,route:String):Boolean{
        var success = false
        auth.signInWithEmailAndPassword(email.trim(),password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)navController.navigate(route).also { success = true }
                success = true
            }.addOnFailureListener {
                success = false
            }
        return success
    }


    fun signOutCurrentUser(navController: NavController,route:String,backstack:String){
        auth.signOut().also {
            navController.navigate(route){popUpTo(backstack)}
        }
    }

}