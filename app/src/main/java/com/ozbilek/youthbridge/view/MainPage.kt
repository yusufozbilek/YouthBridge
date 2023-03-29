@file:OptIn(ExperimentalMaterial3Api::class)

package com.ozbilek.youthbridge.view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ozbilek.youthbridge.R
import com.ozbilek.youthbridge.viewmodel.MainPageImageCardViewModel
import com.ozbilek.youthbridge.viewmodel.MainPageViewModel
import com.ozbilek.youthbridge.viewmodel.WebContentViewModel
import com.ozbilek.youthbridge.viewmodelfactory.MainPageFactory
import com.ozbilek.youthbridge.viewmodelfactory.MainPageImageCardFactory
import com.ozbilek.youthbridge.viewmodelfactory.WebContentFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController){
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val viewModel:MainPageViewModel = viewModel(
        factory = MainPageFactory(context.applicationContext as Application)
    )
    val webViewModel:WebContentViewModel = viewModel(
        factory = WebContentFactory(context.applicationContext as Application)
    )
    val chatIcon = painterResource(id = R.drawable.outline_chat_24)
    val rightIcon = painterResource(id = R.drawable.outline_account_balance_24)
    val academyList = viewModel.cardContentList.observeAsState(listOf())
    val webSourceList = viewModel.webContentList.observeAsState(listOf())
    val imgList = webViewModel.imgUrlList.observeAsState(mapOf())
    val webUrlList = webViewModel.contentList.observeAsState(mapOf())

    viewModel.getAcademy()
    viewModel.getWebContent()
    BackHandler(
        onBack = {activity.finish()}
    )

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            userScrollEnabled = !(webSourceList.value.isNullOrEmpty()&&academyList.value.isNullOrEmpty()&&imgList.value.isNullOrEmpty()&&webUrlList.value.isNullOrEmpty())
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Welcome", fontSize = 45.sp)
                    IconButton(onClick = { viewModel.signOut(navController,"LoginPage","MainPage") }) {
                        Icon(painter = painterResource(id = R.drawable.outline_logout_24),"Log Out")
                    }
                }
            }
            item{
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    ElevatedCard(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(16.dp)
                            .weight(1f),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        onClick = {navController.navigate("PeoplePage")}
                    )
                    {
                        Icon(modifier = Modifier.padding(start = 16.dp, top = 16.dp),painter = chatIcon, contentDescription = "")
                        Text(modifier = Modifier.padding(start = 16.dp,bottom = 8.dp),text = "Get Help", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                        Text(modifier = Modifier.padding(start = 16.dp),text = "You can chat with a professional and get help")
                    }
                    //ads
                    ElevatedCard(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(16.dp)
                            .weight(1f),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        onClick = {navController.navigate("RightsPage")}
                    )
                    {
                        Icon(modifier = Modifier.padding(start = 16.dp, top = 16.dp),painter = rightIcon, contentDescription = "")
                        Text(modifier = Modifier.padding(start = 16.dp,bottom = 8.dp),text = "Your Rights", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                        Text(modifier = Modifier.padding(start = 16.dp),text = "Learn what rights you have in your country")
                    }
                }
            }



            val imgModifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
            item {
                Image(
                    painter = painterResource(id = R.drawable.cool_quote),
                    contentDescription = "Quote",
                    contentScale = ContentScale.Fit,
                    modifier = imgModifier
                )
                Divider(modifier = Modifier.padding(16.dp))
                Text(text = "Academy", fontSize = 38.sp)
                Text(text = "Learn how to deal with bullying with short flashcards", fontSize = 20.sp, fontWeight = FontWeight.Light)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    items(
                        count = academyList.value.size,
                        itemContent = {
                            val academyId = academyList.value?.get(it)?.Id!!
                            val academyTitle = academyList.value?.get(it)?.Title!!
                            val academyText = academyList.value?.get(it)?.Description!!
                            val academyImage = academyList.value?.get(it)?.Image!!
                            //val academyContent:ArrayList<String> = (academyList.value?.get(it)?.content as ArrayList<String>?)!!

                            ImageCard(
                                id = academyId,
                                title = academyTitle,
                                description = academyText,
                                imgSource = academyImage,
                                navController = navController,
                                context = context,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .size(300.dp, 300.dp))
                        }
                    )
                }
                Divider(modifier = Modifier.padding(16.dp))
            }
            item {

                Text(text = "Web Sources", fontSize = 38.sp)
                Text(text = "Common web sources for bullying", fontSize = 20.sp, fontWeight = FontWeight.Light)
            }

            items(
                count = webSourceList.value.size,
                itemContent = {

                    val id = webSourceList.value[it].id
                    val title = webSourceList.value[it].Title
                    val titleImage = webSourceList.value[it].TitleImage
                    val imgUrl = webSourceList.value[it].ImageUrl
                    LaunchedEffect(imgList){
                        webViewModel.getTitleUrl(titleImage)
                        webViewModel.getImageUrl(imgUrl)
                    }
                    LaunchedEffect(webUrlList){
                        webViewModel.getWebUrls(id,title)
                    }
                    val painter = rememberImagePainter(
                        data = imgList.value?.get(titleImage),
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.coolbanner)
                            error(R.drawable.coolbanner)
                        }
                    )

                    Image(
                        painter = painter,
                        contentDescription = "Web Content",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        items(count = webUrlList.value[title]?.size?:0){it1->
                            Log.e("Title",title)
                            val webSourceTitle = webUrlList.value[title]?.get(it1)?.title?:""
                            val webSourceLink = webUrlList.value[title]?.get(it1)?.Url?:""



                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {openUrl(webSourceLink, context)}
                            ) {
                                val boxPainter = rememberImagePainter(
                                    data = imgList.value[imgUrl],
                                    builder = {
                                        crossfade(true)
                                        placeholder(R.drawable.coolbanner)
                                        error(R.drawable.coolbanner)
                                    }
                                )

                                Box(modifier = Modifier.height(200.dp)) {
                                    Image(
                                        painter = boxPainter,
                                        contentDescription = "Box",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                    Text(
                                        text = webSourceTitle,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(12.dp)
                                            .width(170.dp)
                                            .height(400.dp)
                                    )
                                }
                            }
                        }
                    }



                }
            )
        }
    }
}




@ExperimentalMaterial3Api
@Composable
fun ImageCard(
    id:String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    imgSource: String,
    context:Context,
    navController: NavController
) {

    val viewModel: MainPageImageCardViewModel = viewModel(
        factory = MainPageImageCardFactory(context.applicationContext as Application)
    )
    val imgUrlMap = viewModel.imageUrlList.observeAsState(mapOf())
    val painter = rememberImagePainter(
        data = imgUrlMap.value[imgSource],
        builder = {
            crossfade(true)
            placeholder(R.drawable.coolbanner)
            error(R.drawable.coolbanner)
        }
    )
    LaunchedEffect(imgUrlMap) {
        viewModel.getImageUrl(imgSource)
    }
    SideEffect {
        viewModel.getImageUrl(imgSource)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.large,
        enabled = id.isNotEmpty(),
        onClick = {navController.navigate("AcademyPage/${id}/${title}")}
    ) {

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .aspectRatio(3f / 2f),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


private fun openUrl(url: String,Context:Context) {
    val webActivity = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    Context.startActivity(webActivity)
}

