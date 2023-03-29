@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.ozbilek.youthbridge.view

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ozbilek.youthbridge.R.drawable
import com.ozbilek.youthbridge.viewmodel.ImageCardViewModel
import com.ozbilek.youthbridge.viewmodel.PeoplePageViewModel
import com.ozbilek.youthbridge.viewmodelfactory.ImageCardFactory
import com.ozbilek.youthbridge.viewmodelfactory.PeoplePageFactory

@Composable
fun PeoplePage(navController: NavController){
    val context = LocalContext.current
    val viewModel:PeoplePageViewModel = viewModel(
        factory = PeoplePageFactory(context.applicationContext as Application)
    )
    val fabIcon = painterResource(id = drawable.baseline_person_add_24)
    val selectedOption = remember { mutableStateOf(0) }
    val options = listOf("NGO officer", "Psychologist", "Student representative")
    val addNewState = remember{ mutableStateOf(false) }
    val contacts = viewModel.contactList.observeAsState(listOf())
    val featureDev= remember { mutableStateOf(false) }
    viewModel.getProviders()
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(text = "Chat", fontSize = 28.sp)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("MainPage") }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { featureDev.value = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More Options")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        addNewState.value = true
                    }
                ) {
                    Icon(painter = fabIcon, contentDescription = "Add New Person")
                }
            }
        )
        {
            if (addNewState.value){
                AlertDialog(
                    title = {
                        Column {
                            Text(
                                text = "Add authorized person",
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                            )
                            Divider(modifier = Modifier.padding(top = 10.dp))
                        }
                    },
                    text = {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Please choose who you want help from",
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = MaterialTheme.typography.titleLarge.fontWeight
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            options.forEachIndexed { optionIndex, option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedOption.value == optionIndex,
                                        onClick = { selectedOption.value = optionIndex }
                                    )
                                    Text(
                                        text = option,
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    },
                    
                    confirmButton = {
                        TextButton(onClick = { Log.w("Firestore", options[selectedOption.value]);viewModel.addProvider(options[selectedOption.value]);viewModel.getProviders();addNewState.value = false }) {
                            Text(
                                text = "Add",
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { addNewState.value = false }) {
                            Text(
                                text = "Cancel",
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                        }
                    },
                    onDismissRequest = { addNewState.value = false }
                )
            }
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

            LazyColumn(modifier = Modifier.padding(it)){
                items(
                    count = contacts.value.size,
                    itemContent = {contactIndex->
                        val name = contacts.value.getOrNull(contactIndex)?.Username
                        val firstLetter = contacts.value.getOrNull(contactIndex)?.Username?.firstOrNull() ?: "X"
                        val type = contacts.value[contactIndex].ProviderType
                        val sharedId = contacts.value[contactIndex].SharedId
                        Log.e("Alert",sharedId)

                        PeopleCard(
                            modifier = Modifier.padding(16.dp),
                            personName = name.toString(),
                            personStat = type,
                            firstLetter = firstLetter.toString(),
                            sharedId = sharedId,
                            context = context,
                            navController
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun PeopleCard(
    modifier:Modifier = Modifier,
    personName:String,
    personStat:String,
    firstLetter:String,
    sharedId:String,
    context:Context,
    navController: NavController
){
    val viewModel:ImageCardViewModel = viewModel(
        factory = ImageCardFactory(context.applicationContext as Application))
    val imgUrlMap = viewModel.imageUrlList.observeAsState(mapOf())
    val painter = rememberImagePainter(
        data = imgUrlMap.value[firstLetter],
        builder = {
            crossfade(true)
            placeholder(drawable.ybbanner)
            error(drawable.ybbanner)
        }
    )
    LaunchedEffect(imgUrlMap) {
        viewModel.getImageUrl(firstLetter)
    }
    SideEffect {
        viewModel.getImageUrl(firstLetter)
    }

    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.Transparent), onClick = {navController.navigate("ChatPage/${sharedId}/${firstLetter}/${personName}/${personStat}")}) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = personName,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = personStat,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

