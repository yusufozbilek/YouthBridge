package com.ozbilek.youthbridge.view

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ozbilek.youthbridge.viewmodel.AcademyPageViewModel
import com.ozbilek.youthbridge.viewmodelfactory.AcademyPageFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademyPage(navController: NavController,id:String,title:String) {
    val context = LocalContext.current
    val viewModel:AcademyPageViewModel = viewModel(
        factory = AcademyPageFactory(context.applicationContext as Application)
    )
    val currentContentIndex = remember { mutableStateOf(0) }
    val contentList = viewModel.academyContentList.observeAsState(listOf("","",""))
    viewModel.getAcademyById(id)

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title, fontSize = MaterialTheme.typography.displaySmall.fontSize)},
                navigationIcon = { IconButton(onClick = { navController.navigate("MainPage") }) {Icon(Icons.Default.ArrowBack,"Go back")}}
            )
        }
            ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .height(700.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                ) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(
                            text = contentList.value[currentContentIndex.value],
                            textAlign = TextAlign.Center,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            }
                Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    IconButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            if (currentContentIndex.value > 0) {
                                currentContentIndex.value--
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous"
                        )
                    }
                    Text(
                        text = "${currentContentIndex.value+1}/${contentList.value.size}",
                        fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    )
                    IconButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            if (currentContentIndex.value < contentList.value.size - 1) {
                                currentContentIndex.value++
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next"
                        )
                    }
                }
            )
        }
    }

}