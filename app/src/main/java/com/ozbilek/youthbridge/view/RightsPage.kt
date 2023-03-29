package com.ozbilek.youthbridge.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ozbilek.youthbridge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightsPage(navController: NavController){
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Your Rights", fontSize = MaterialTheme.typography.headlineLarge.fontSize)},
                navigationIcon = { IconButton(onClick = {navController.navigate("MainPage"){popUpTo("RightsPage")} }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go to MainPage")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.padding(16.dp,8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer),

                )
            {
                Row {
                    Icon(painter = painterResource(R.drawable.outline_lightbulb_24), contentDescription = "Akp", Modifier.padding(start = 16.dp, top = 24.dp))
                    Text(
                        modifier = Modifier.padding(start = 8.dp,top = 16.dp, end = 8.dp, bottom = 16.dp),
                        text = "Children have universal rights recognized by the United Nations to ensure their safety and well-being.",
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                }

            }

            ExpandableCard(header = "The right to education", description = "Every child has the right to go to school and learn new things. Education is important for your future, and it’s also a lot of fun! ")
            ExpandableCard(header = "The right to health", description = "Every child has the right to be healthy and get the medical care they need. This means going to the doctor when you’re sick, eating healthy foods, and getting enough exercise.")
            ExpandableCard(header = "The right to a safe home", description = "Every child has the right to live in a safe and secure home, where they feel loved and protected. Your home should be a place where you can relax and be yourself.")
            ExpandableCard(header = "The right to healthy food", description = "Every child has the right to eat nutritious food that helps them grow and stay healthy. This means eating fruits and vegetables, drinking milk, and avoiding junk food.")
            ExpandableCard(header = "The right to be with your family", description = "Every child has the right to be with their family, and to feel loved and supported by them. Your family is important, and they will always be there for you.")
            ExpandableCard(header = "The right to express yourself", description = "Every child has the right to express their thoughts and feelings, and to be heard. This means speaking up when you have something to say, and being respectful when others speak.")
            ExpandableCard(header = "The right to play", description = "Every child has the right to play, and to have fun and be creative. This means doing things like drawing, playing with friends, and exploring the world around you.")
            ExpandableCard(header = "The right to be safe", description = "Every child has the right to be protected from things like violence, abuse, neglect, and bullying. This means that adults should do everything they can to keep children safe, and children should know that it’s okay to speak up if they don’t feel safe.")
        }
    }
    
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(
    header: String, // Header
    description: String, // Description
) {
    val expand = remember { mutableStateOf(false) } // Expand State
    val rotationState by animateFloatAsState(if (expand.value) 180f else 0f) // Rotation State
    val stroke = remember { mutableStateOf(1) } // Stroke State
    Card(
        modifier = Modifier
            .animateContentSize( // Animation
                animationSpec = tween(
                    durationMillis = 400, // Animation Speed
                    easing = LinearOutSlowInEasing // Animation Type
                )
            )
            .padding(16.dp, 8.dp),
        onClick = {
            expand.value = !expand.value
            stroke.value = if (expand.value) 2 else 1
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Control the header Alignment over here.
            ) {
                Text(
                    text = header,
                    color = MaterialTheme.colorScheme.onSecondaryContainer, // Header Color
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .weight(.9f)
                        .padding(start = 8.dp)
                )
                IconButton(
                    modifier = Modifier
                        .rotate(rotationState)
                        .weight(.1f),
                    onClick = {
                        expand.value = !expand.value
                        stroke.value = if (expand.value) 2 else 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer, // Icon Color
                        contentDescription = "Drop Down Arrow"
                    )
                }
            }
            if (expand.value) {
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSecondaryContainer, // Description Color
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Start,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }

}