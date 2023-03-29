package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ozbilek.youthbridge.entity.WebSourceItem
import com.ozbilek.youthbridge.entity.WebsiteUrlData

class WebContentViewModel(application: Application): AndroidViewModel(application) {
    private val db = Firebase.firestore
    private val storage = Firebase.storage.reference
    var titleImgUrl = MutableLiveData<String>()
    var imgUrl = MutableLiveData<String>()
    //var contentList = MutableLiveData<List<WebsiteUrlData>>()
    var imgUrlList = MutableLiveData<Map<String,String>>()
    var contentList = MutableLiveData<Map<String,List<WebsiteUrlData>>>()
    init {
        titleImgUrl = MutableLiveData()
        imgUrl = MutableLiveData()
        contentList.value = mutableMapOf()
        imgUrlList.value = mutableMapOf()
    }

    fun getImageUrl(imgName:String){
        val ref = storage.child("/WebSourceImage/${imgName}.jpg")
        ref.downloadUrl.addOnSuccessListener { uri ->
            //Log.v("Firebase",uri.toString())
            imgUrl.postValue(uri.toString())
            val newList = (imgUrlList.value ?: mutableMapOf()).toMutableMap()
            newList[imgName] = uri.toString()
            imgUrlList.postValue(newList)
        }.addOnFailureListener {
            Log.e("FirebaseStorageRepo", "Error occured while getting the image URL", it)
        }
    }

    fun getTitleUrl(titleImgName:String){
        val ref = storage.child("/WebSourceImage/${titleImgName}.jpg")
        ref.downloadUrl.addOnSuccessListener { uri ->
            //Log.v("Firebase",uri.toString())
            titleImgUrl.postValue(uri.toString())
            val newList = (imgUrlList.value ?: mutableMapOf()).toMutableMap()
            newList[titleImgName] = uri.toString()
            imgUrlList.postValue(newList)
        }.addOnFailureListener {
            Log.e("FirebaseStorageRepo", "Error occured while getting the image URL", it)
        }
    }

    fun getWebUrls(id:String, title:String) {
        db.collection("Content").document(id).collection("Source").get().addOnSuccessListener { docs->
            val webItems = mutableListOf<WebsiteUrlData>()
            docs.forEach {
                val title = it.getString("Title") ?: ""
                val webUrl = it.getString("Url") ?: ""
                webItems.add(WebsiteUrlData(title, webUrl))
            }

            val currentList = contentList.value ?: emptyMap()
            val updatedList = currentList.toMutableMap()
            updatedList[title] = webItems
            contentList.postValue(updatedList).also { Log.e("Amk", contentList.value.toString()) }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseFirestoreRepo", "Error getting documents: ", exception)
        }
    }
}