package com.ozbilek.youthbridge.view

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ozbilek.youthbridge.R
import com.ozbilek.youthbridge.viewmodel.ChatPageViewModel
import com.ozbilek.youthbridge.viewmodelfactory.ChatPageFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(navController: NavController,sharedChat:String,firstLetter:String,userName:String,userStat:String){
    val context = LocalContext.current
    val viewModel:ChatPageViewModel = viewModel(
        factory = ChatPageFactory(context.applicationContext as Application,sharedChat)
    )
    val imgUrl = viewModel.imageUrl.observeAsState()
    val messages = viewModel.messages.observeAsState(listOf())
    val text = remember{ mutableStateOf("") }
    val featureDev= remember { mutableStateOf(false) }


    val painter = rememberImagePainter(
        data = imgUrl.value,
        builder = {
            crossfade(true)
            placeholder(R.drawable.ybbanner)
            error(R.drawable.ybbanner)
        }
    )
    LaunchedEffect(imgUrl) {
        viewModel.getImageUrl(firstLetter)
        viewModel.fetchMessages()
    }
    SideEffect {
        viewModel.getImageUrl(firstLetter)
        viewModel.fetchMessages()
    }



    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                     SmallTopAppBar(
                         scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                         navigationIcon = { IconButton(onClick = { navController.navigate("PeoplePage") }) { Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")}},
                         actions = { IconButton(onClick = { featureDev.value = true }) {Icon(Icons.Filled.MoreVert, contentDescription = "More Options")}},
                         title = {
                             Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                 Image(
                                     painter = painter,
                                     contentDescription = null,
                                     modifier = Modifier
                                         .size(54.dp)
                                         .clip(CircleShape),
                                     contentScale = ContentScale.Crop,
                                 )
                                 Column {
                                     Text(modifier = Modifier.padding(top = 16.dp, start = 8.dp),text = userName)
                                    Text(modifier = Modifier.padding(start = 8.dp),text = userStat, fontSize = 20.sp, fontWeight = FontWeight.Light)
                                 }
                             }
                         }
                     )
            },
            bottomBar = {
                BottomAppBar(modifier = Modifier.fillMaxWidth(), containerColor = Color.Transparent) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = text.value,
                            onValueChange = { text.value = it },
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            placeholder = { Text(text = "Enter Message") }
                        )
                        FloatingActionButton(
                            onClick = { viewModel.sendMessage(text.value, sender = "User"); text.value = "" },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Outlined.Send, contentDescription = "")
                        }
                    }
                }
            }
        ) 
        {
            if (featureDev.value){
                AlertDialog(
                    onDismissRequest = { featureDev.value = false },
                    title = { Text("In Development Feature", fontSize = MaterialTheme.typography.headlineMedium.fontSize) },
                    text = { Text("This content is under development.",fontSize = MaterialTheme.typography.titleMedium.fontSize) },
                    confirmButton = {
                        TextButton(onClick = { featureDev.value = false }) {
                            Text("Okay")
                        }
                    }
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
            ){
                items(
                    messages.value.size,
                    itemContent = {it1->
                        val message = messages.value?.get(it1)
                        val chatText = message?.ChatText ?: "asd"
                        val sender = message?.Sender ?:"User"
                        MessageCard(user = sender, text = chatText)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageCard(user:String,text:String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = if (user == "User") { Alignment.End } else  Alignment.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            shape = RoundedCornerShape(16.dp)

        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = text,
                fontSize = 24.sp
            )
        }

    }
}

