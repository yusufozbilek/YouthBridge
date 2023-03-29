package com.ozbilek.youthbridge.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ozbilek.youthbridge.entity.AcademyItem
import com.ozbilek.youthbridge.entity.Contact
import com.ozbilek.youthbridge.entity.WebSourceItem

class CloudFirestoreRepository {
    private val firestore = Firebase.firestore
    var providerList  = MutableLiveData<List<Contact>>()
    var academyList = MutableLiveData<List<AcademyItem>>()
    var webSourceList = MutableLiveData<List<WebSourceItem>>()

    init {
        providerList = MutableLiveData(listOf(Contact("","","","","")))
        academyList = MutableLiveData(listOf(AcademyItem("","","","", listOf())))
        webSourceList = MutableLiveData(listOf(WebSourceItem("","","","")))

    }

    fun registerUserToFirestore(email:String,profilePhoto:String,username:String,isOnline:Boolean,isVerified:Boolean){
        val userRef = firestore.collection("User")
        val userRegister = hashMapOf(
            "Email" to email,
            "ProfilePhoto" to profilePhoto,
            "Username" to username,
            "isOnline" to isOnline,
            "isVerified" to isVerified
        )
        val dullUser = hashMapOf(
            "Email" to "Null",
            "SharedChatId" to "Null",
            "Username" to "Null",
            "isOnline" to false,
            "isVerified" to false
        )

        userRef.add(userRegister)
            .addOnCompleteListener {
                Log.i("Firestore","Register Successful")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Register Error ${it.message}")
            }
        userRef.get().addOnSuccessListener {
            userRef.document(it.documents[0].id).collection("Contacts")
                .add(dullUser).addOnCompleteListener {
                    Log.i("Firestore","DullUser added")
                }.addOnFailureListener {e->
                    Log.e("Firestore","DullUser: ${e.message}")
                }
        }
    }


    fun addContact(contactType:String,userEmail:String){
        val userRef = firestore.collection("User")
        val providerRef = firestore.collection("Provider")
        val chatRef = firestore.collection("Chat")
        val possibleList = mutableListOf<String>()
        var theChosenOneId:String
        var theUserId: String
        var sharedChatId:String
        providerRef.whereEqualTo("ProviderType",contactType).get().addOnSuccessListener {provider->
            provider.forEach{it0->
                possibleList.add(it0.id)
            }
            theChosenOneId = possibleList.random()
            userRef.whereEqualTo("Email",userEmail).get().addOnSuccessListener {users->
                theUserId = users.documents[0].id
                val chatData = hashMapOf(
                    "User" to theUserId,
                    "Provider" to theChosenOneId
                )
                 chatRef.add(chatData).addOnSuccessListener {
                     sharedChatId = it.id
                     providerRef.document(theChosenOneId).get().addOnSuccessListener {
                         val theChosenOne = it
                         val toUser = hashMapOf(
                             "SharedChatId" to sharedChatId,
                             "Username" to theChosenOne!!["ProviderName"],
                             "ProviderId" to theChosenOneId
                         )
                         userRef.document(theUserId).get().addOnSuccessListener { userSnap->
                             val user = userSnap
                             val toProvider = hashMapOf(
                                 "SharedChatId" to sharedChatId,
                                 "ClientUsername" to user!!.get("Username"),
                                 "UserId" to theUserId
                             )
                             userRef.document(theUserId).collection("Contacts").add(toUser).addOnSuccessListener {
                                 providerRef.document(theChosenOneId).collection("Clients").add(toProvider)
                             }
                         }
                     }
                 }
            }
        }
    }

    fun getContact(userEmail: String) {
        val db = FirebaseFirestore.getInstance()
        val providerRef = db.collection("Provider")
        db.collection("User").whereEqualTo("Email", userEmail)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && !documents.isEmpty()) {
                        val id = documents[0].id

                        val contactsRef = db.collection("User").document(id).collection("Contacts")

                        contactsRef.get().addOnSuccessListener { documents ->
                            val providerIdList = mutableListOf<String>()
                            val sharedIdList = mutableListOf<String>()
                            val contactList = mutableListOf<Contact>()

                            for (document in documents) {
                                val providerId = document.getString("ProviderId")
                                if (providerId != null) {
                                    providerIdList.add(providerId)
                                }
                                val sharedId = document.getString("SharedChatId")
                                if (sharedId != null){
                                    sharedIdList.add(sharedId)
                                }
                            }

                            val providerTasks = providerIdList.map { providerId ->
                                providerRef.document(providerId.trim()).get()
                            }

                            Tasks.whenAllSuccess<DocumentSnapshot>(providerTasks).addOnSuccessListener { providerDocuments ->
                                var pos = 0
                                providerDocuments.forEach { providerDocument ->
                                    val name = providerDocument.getString("ProviderName") ?: ""
                                    val email = providerDocument.getString("ProviderMail") ?: ""
                                    val providerType = providerDocument.getString("ProviderType") ?: ""
                                    contactList.add(Contact(name,"",email,providerType,sharedIdList[pos]))
                                    pos += 1
                                }
                                providerList.value = contactList.toList()
                            }
                        }
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.exception)
                }
            }
    }

    fun getAcademy(){
        val db = FirebaseFirestore.getInstance()
        val  academyRef = db.collection("Academy")
        academyRef.get().addOnSuccessListener {
            val academyItem = mutableListOf<AcademyItem>()
            it.documents.forEach {docs->
                val id = docs.id?:""
                val title = docs.getString("Title")?: ""
                val description = docs.getString("Description") ?: ""
                val imgUrl = docs.getString("ImageName") ?: ""
                val content = docs.get("Content") as List<String>
                academyItem.add(AcademyItem(id,title,description,imgUrl,content))
            }
            academyList.value = academyItem.toList()
        }
    }

    fun getWebContent(){
        val db = FirebaseFirestore.getInstance()
        val webContentRef = db.collection("Content")
        webContentRef.get().addOnSuccessListener {
            val webItem = mutableListOf<WebSourceItem>()
            it.documents.forEach {docs->
                val id = docs.id?:""
                val title = docs.getString("Title")?: ""
                val background = docs.getString("SharedBackground")?: ""
                val titleImage = docs.getString("TitleImage")?: ""
                Log.e("Hata",title)
                webItem.add(WebSourceItem(id,title,background,titleImage))
            }
            webSourceList.value = webItem.toList()
        }.addOnFailureListener {
            Log.e("Hata","Bulamadık")
        }
    }


}

