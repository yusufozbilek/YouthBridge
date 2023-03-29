package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ozbilek.youthbridge.entity.AcademyItem

class AcademyPageViewModel(application: Application):AndroidViewModel(application) {
    private val firestore = Firebase.firestore

    var academyContentList = MutableLiveData<List<String>>()

    init {
        academyContentList = MutableLiveData(listOf("a","b","c"))
    }

    fun getAcademyById(id:String){
        val db = FirebaseFirestore.getInstance()
        val  academyRef = db.collection("Academy")
        academyRef.document(id).get().addOnSuccessListener {docs->
            val content = docs.get("Content") as List<String>
            academyContentList.value = content.toList()
        }
    }



}