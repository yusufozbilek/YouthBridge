package com.ozbilek.youthbridge.view

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.ozbilek.youthbridge.R
import com.ozbilek.youthbridge.viewmodel.LoginPageViewModel
import com.ozbilek.youthbridge.viewmodelfactory.LoginPageFactory

@Composable
fun LoginPage(navController: NavController){
    val context = LocalContext.current
    val viewModel:LoginPageViewModel = viewModel(
        factory = LoginPageFactory(context.applicationContext as Application)
    )
    val email = remember{ mutableStateOf("")}
    val password = remember{mutableStateOf("")}
    Image(
        painter = painterResource(id = R.drawable.loginregisterbackground),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        alpha = 0.4f,
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome",
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = buildAnnotatedString {
                append("New to YouthBridge? ")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Register")
                    addStringAnnotation(tag = "Clickable", annotation = "Register",0,0)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 22.sp,
            modifier = Modifier.clickable(onClick = { navController.navigate("RegisterPage"){popUpTo("LoginPage")} })
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = {email.value = it},
            modifier = Modifier.padding(bottom = 8.dp),
            label = { Text(text = "Email", fontSize = MaterialTheme.typography.titleLarge.fontSize)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            textStyle = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {password.value = it},
            modifier = Modifier.padding(bottom = 8.dp),
            label = { Text(text = "Password", fontSize = MaterialTheme.typography.titleLarge.fontSize)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(),
            singleLine = true,
            textStyle = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 75.dp, end = 75.dp),
            onClick = {
                viewModel.login(email.value,password.value,navController,"MainPage")
            }
        ) {
            Text(text = "Login", fontSize = MaterialTheme.typography.titleLarge.fontSize)
        }
    }
}

@Deprecated("This is used in ViewModel")
private fun login(navController: NavController,auth:FirebaseAuth,email:String,password:String):Boolean{
    var value:Boolean = false
    auth.signInWithEmailAndPassword(email.trim(),password.trim())
        .addOnCompleteListener() {
                task-> if (task.isSuccessful)navController.navigate("MainPage")
        }
    return value
}