package com.example.mediapp.ui.theme

//import androidx.navigation.compose.composable
//import com.example.mediapp.BottomMenuContent
//import com.example.mediapp.Feature
//import com.example.mediapp.standardQuadFromTo

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mediapp.*
import com.example.mediapp.R
import com.example.mediapp.ui.theme.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Log
import java.util.*


@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.medi) }
    var isPlaying by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(DeepBlue)
        .fillMaxSize()
    ){
        Column {
            GreetingSection()
            ChipSection(chips = listOf("Yasin", "Qaf", "Kahf", "Fatiha", "Falaq", "Kafirun"))
            CurrentMeditation()
            FeatureSection(features = listOf(
                Feature(
                    title = "Morning Dua",
                    R.drawable.ic_headphone,
                    BlueViolet1,
                    BlueViolet2,
                    BlueViolet3
                ),
                Feature(
                    title = "Evening Dua",
                    R.drawable.ic_headphone,
                    OrangeYellow1,
                    OrangeYellow2,
                    OrangeYellow3
                ),
                Feature(
                    title = "Tips for beginners",
                    R.drawable.ic_videocam,
                    LightGreen1,
                    LightGreen2,
                    LightGreen3
                ),
                Feature(
                    title = "Quran",
                    R.drawable.ic_headphone,
                    Beige1,
                    Beige2,
                    Beige3
                )
            ),
                onFeatureClick = { feature ->
                    if (feature.title == "Tips for beginners") {
                        val rawResourceId = R.raw.medd
                        val rawResourceUri = RawResourceDataSource.buildRawResourceUri(rawResourceId)
                        Log.d("HomeScreen", "Navigating to video URI: $rawResourceUri")
                        navController.navigate("video/${Uri.encode(rawResourceUri.toString())}")
                    }
                    if (feature.title == "Morning Dua") {
                        if (isPlaying) {
                            mediaPlayer.pause()
                            mediaPlayer.seekTo(0)
                        } else {
                            mediaPlayer.start()
                        }
                        isPlaying = !isPlaying
                    }
                    if (feature.title == "Quran") {
                        Log.d("ProfileScreen", "Navigating to profile screen")
                        navController.navigate("Profile")
                    }
                })
        }
        BottomMenu(items = listOf(
            BottomMenuContent("Home", R.drawable.ic_home),
            BottomMenuContent("Meditate", R.drawable.ic_bubble),
            BottomMenuContent("Sleep", R.drawable.ic_moon),
            BottomMenuContent("Music", R.drawable.ic_music),
            BottomMenuContent("Profile", R.drawable.ic_profile),
        ), modifier = Modifier.align(Alignment.BottomCenter),
            onItemClick = { screen ->
                if(screen.route == "Profile"){
                    Log.d("ProfileScreen", "Navigating to profile screen")
                    //navController.navigate("Profile")
                    navController.navigate(Screen.Profile.route)
                }
                if(screen.route == "Music"){
                    Log.d("ProfileScreen", "Navigating to music screen")
                    navController.navigate(Screen.Music.route)
                }
            })
    }
}

@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    onItemClick: (Screen) -> Unit,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = AquaBlue,
    initialSelectedItemIndex: Int = 0
) {
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(DeepBlue)
            .padding(7.dp)
    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                item = item,
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor,
//                modifier = Modifier
//                    .clickable { val screen = when (item.title) {
//                        "Home" -> Screen.Home
//                        "Meditate" -> Screen.Meditate
//                        "Sleep" -> Screen.Sleep
//                        "Music" -> Screen.Music
//                        "Profile" -> Screen.Profile
//                        else -> Screen.Home // Default case
//                    }
//                        onItemClick(screen) }
//            ) {
//                selectedItemIndex = index
//            }
                onItemClick = {
                    selectedItemIndex = index
                    val screen = when (item.title) {
                        "Home" -> Screen.Home
                        "Meditate" -> Screen.Meditate
                        "Sleep" -> Screen.Sleep
                        "Music" -> Screen.Music
                        "Profile" -> Screen.Profile
                        else -> Screen.Home // Default case
                    }
                    onItemClick(screen)
                }
            )
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = AquaBlue,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent)
//                .background(if (isSelected) activeHighlightColor else Color.Transparent)
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if (isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = item.title,
            color = if(isSelected) activeTextColor else inactiveTextColor
        )
    }
}

@Composable
fun GreetingSection(
    name: String = "Akidah"
) {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 0..11 -> "صباح الخير"
        in 12..17 -> "مساء الخير"
        else -> "مساء الخير"
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Assalamu Aleikum, $name",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun ChipSection(
    chips: List<String>
) {
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow {
        items(chips.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                    .clickable {
                        selectedChipIndex = it
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedChipIndex == it) ButtonBlue
                        else DarkerButtonBlue
                    )
                    .padding(15.dp)
            ) {
                Text(text = chips[it], color = TextWhite)
            }
        }
    }
}

@Composable
fun CurrentMeditation(
    color: Color = LightRed
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.medi) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Just Listen",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Tulia and listen, just vibe. Haina haraka",
                style = MaterialTheme.typography.bodyMedium,
                color = TextWhite
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ButtonBlue)
                .padding(10.dp)
                .clickable {
                    if (isPlaying) {
                        mediaPlayer.pause()
                        mediaPlayer.seekTo(0)
                    } else {
                        mediaPlayer.start()
                    }
                    isPlaying = !isPlaying
                }
        ) {
            Icon(
                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
    LaunchedEffect(mediaPlayer) {
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)//because its experimental, the LazyVerticalGrid
@Composable
fun FeatureSection(features : List<Feature>, onFeatureClick: (Feature) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Features",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(15.dp)
        )
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ){
            items(features.size) {
                FeatureItem(feature = features[it], onFeatureClick = onFeatureClick)
            }
        }
    }
}

@Composable
fun FeatureItem(feature: Feature, onFeatureClick: (Feature) -> Unit) {
    BoxWithConstraints(modifier = Modifier
        .padding(7.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(10.dp))
        .background(feature.darkColor)
        .clickable { onFeatureClick(feature) }
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        // Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = feature.mediumColor
            )
            drawPath(
                path = lightColoredPath,
                color = feature.lightColor
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.headlineMedium,
                lineHeight = 26.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Icon(
                painter = painterResource(id = feature.iconId),
                contentDescription = feature.title,
                tint = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                text = "Start",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
//                    .clickable {
//                        // Handle the click
//                    }
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ButtonBlue)
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )
        }
    }
}

@Composable
fun VideoPlayerScreen(videoUri: Uri) {
    // Background with rounded corners and shadow
    var isPlaying by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .shadow(8.dp, RoundedCornerShape(16.dp))
    ) {
        // VideoPlayer component
        VideoPlayer(videoUri)

        // Title and Description
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(topEnd = 16.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "Example video",
                //style = MaterialTheme.typography.h6.copy(color = Color.White)
                style = MaterialTheme.typography.headlineMedium.copy(color= Color.White)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Implementing a video for the multimedia.",
                //style = MaterialTheme.typography.body2.copy(color = Color.White)
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            IconButton(onClick = {
                // Handle full screen action
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic__fullscreen_24),
                    contentDescription = "Fullscreen",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                // Handle volume toggle action
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_volume),
                    contentDescription = "Volume",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun VideoPlayer(videoUri: Uri) {
    // Video player implementation here
    // For example, using ExoPlayer
    Box(modifier = Modifier
        .fillMaxSize()
        .aspectRatio(16 / 9f))
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = SimpleExoPlayer.Builder(context).build().apply  {
                    setMediaItem(MediaItem.fromUri(videoUri))
                    prepare()
                    playWhenReady = true
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            //.aspectRatio(16 / 9f)
            .clip(RoundedCornerShape(10.dp))
    )
}
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Music.route) { MusicScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable("Profile") { ProfileScreen(navController) }
        composable(Screen.Video.route, arguments = listOf(navArgument("uri")
            { type = NavType.StringType })) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri") ?: ""
            val uri = Uri.parse(uriString)
            VideoPlayerScreen(uri)
        }
    }
}

