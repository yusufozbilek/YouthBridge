package com.ozbilek.youthbridge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ozbilek.youthbridge.entity.ChatMessage
import java.util.*

class ChatPageViewModel(application: Application,id:String): AndroidViewModel(application) {
    private val storage = Firebase.storage.reference
    private val messagesCollection = FirebaseFirestore.getInstance().collection("Chat").document(id).collection("Chat")
    private val _messages = MutableLiveData<List<ChatMessage>>()
    var messages: MutableLiveData<List<ChatMessage>> = _messages
    var imageUrl = MutableLiveData<String>()
    init {
        imageUrl = MutableLiveData()
        messagesCollection.orderBy("Timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("info", "Listen failed.", error)
                    return@addSnapshotListener
                }

                value?.let {
                    val messages = mutableListOf<ChatMessage>()
                    for (doc in it) {
                        Log.e("Control",doc.getString("Text")?:"Cuuuuuu")
                        val message = ChatMessage(
                            doc.getString("Text") ?: "",
                            doc.getString("Sender") ?: "",
                            doc.getDate("Timestamp") ?: Date(0)
                        )
                        messages.add(message)
                    }
                    _messages.postValue(messages.reversed())
                }
            }
    }
    fun fetchMessages() {
        messagesCollection.orderBy("Timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val messagesList = mutableListOf<ChatMessage>()
                for (document in documents) {
                    val chatText = document.getString("Text") ?: ""
                    val sender = document.getString("Sender") ?: ""
                    val timestamp = document.getTimestamp("Timestamp")?.toDate() ?: Date(0)
                    val chatMessage = ChatMessage(chatText, sender, timestamp)
                    messagesList.add(chatMessage)
                }
                _messages.postValue(messagesList.reversed())
            }
            .addOnFailureListener { exception ->
                Log.w("info", "Error getting documents: ", exception)
            }
    }



    fun sendMessage(text: String,sender:String) {
        val newMessage = hashMapOf(
            "Text" to text,
            "Sender" to sender,
            "Timestamp" to FieldValue.serverTimestamp()
        )

        messagesCollection.add(newMessage)
            .addOnSuccessListener { documentReference ->
                Log.d("Info", "DocumentSnapshot successfully written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Info", "Error writing document", e)
            }
    }


    fun getImageUrl(imageName: String) {
        Log.e("image",imageName)
        val storageRef = storage.child("ProfilePhotos/StandardUserPhotos/${imageName.uppercase()}.png")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            imageUrl.postValue(uri.toString())
        }.addOnFailureListener {
            Log.e("FirebaseStorageRepo", "Error occured while getting the image URL", it)
        }
    }
}