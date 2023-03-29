package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ImageCardViewModel(application: Application):AndroidViewModel(application){
    private val storage = Firebase.storage.reference
    var imageUrl = MutableLiveData<String>()
    var imageUrlList = MutableLiveData<Map<String,String>>()
    init {
        imageUrl = MutableLiveData()
        imageUrl.value = ""
        imageUrlList.value = mutableMapOf()
    }
    fun getImageUrl(imageName: String) {
        val storageRef = storage.child("ProfilePhotos/StandardUserPhotos/${imageName.uppercase()}.png")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Log.v("Firebase",uri.toString())
            imageUrl.postValue(uri.toString())
            val newList = (imageUrlList.value ?: mutableMapOf()).toMutableMap()
            newList[imageName] = uri.toString()
            imageUrlList.postValue(newList)
        }.addOnFailureListener {
            Log.e("FirebaseStorageRepo", "Error occured while getting the image URL", it)
        }
    }
}