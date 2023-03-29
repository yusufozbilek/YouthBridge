package com.ozbilek.youthbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.YouthBridgeTheme
import com.ozbilek.youthbridge.view.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YouthBridgeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavBall()
                }
            }
        }
    }

}


@Composable
fun NavBall(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "SplashPage" ){
        composable("SplashPage"){
            SplashPage(navController = navController)
        }
        composable("RegisterPage"){
            RegisterPage(navController = navController)
        }
        composable("LoginPage"){
            LoginPage(navController = navController)
        }
        composable("MainPage"){
            MainPage(navController = navController)
        }
        composable("PeoplePage"){
            PeoplePage(navController = navController)
        }
        composable(
            "ChatPage/{sharedChatId}/{firstLetter}/{username}/{usertype}",
            arguments = listOf(
                navArgument("sharedChatId"){type = NavType.StringType},
                navArgument("firstLetter"){type = NavType.StringType} ,
                navArgument("username"){type = NavType.StringType},
                navArgument("usertype"){type = NavType.StringType}
            )
        ){
            val sharedChatId = it.arguments?.getString("sharedChatId")!!
            val firstLetter = it.arguments?.getString("firstLetter")!!
            val username = it.arguments?.getString("username")!!
            val usertype = it.arguments?.getString("usertype")!!
            ChatPage(navController = navController,sharedChatId,firstLetter,username,usertype)
        }
        composable("RightsPage"){
            RightsPage(navController = navController)
        }
        composable(
            route = "AcademyPage/{id}/{title}",
            arguments = listOf(
                navArgument("id"){type = NavType.StringType},
                navArgument("title"){type = NavType.StringType}
            )
        ){
            val id = it.arguments?.getString("id")!!
            val title = it.arguments?.getString("title")!!
            AcademyPage(navController,id,title)
        }
    }
}