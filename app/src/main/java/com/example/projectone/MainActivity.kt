package com.example.projectone

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.projectone.ui.theme.ProjectOneTheme
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var databaseHelper: TimeLogDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = TimeLogDatabaseHelper(this)
        databaseHelper.deleteAllData()
        setContent {
            ProjectOneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyScreen(this, databaseHelper)
                }
            }
        }
    }
}

@Composable
fun MyScreen(context: Context, databaseHelper: TimeLogDatabaseHelper) {
    var startTime by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    val imageModifier = Modifier
    Image(
        painterResource(id = R.drawable.sleeptracking),
        contentScale = ContentScale.FillHeight,
        contentDescription = "",
        modifier = imageModifier.alpha(0.7F)

    )


    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {

                elapsedTime = System.currentTimeMillis() - startTime
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sleep Tracker",
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        if (!isRunning) {
            Button(
                onClick = {
                    startTime = System.currentTimeMillis()
                    isRunning = true
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Start", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = {
                    val endTime = System.currentTimeMillis()
                    isRunning = false
                    databaseHelper.addTimeLog(endTime, startTime)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Stop", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display elapsed time as HH:mm:ss format
        Text(
            text = formatTime(elapsedTime),
            fontSize = 32.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                context.startActivity(
                    Intent(context, TrackActivity::class.java)
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Track Sleep", fontSize = 18.sp)
        }
    }
}

private fun formatTime(timeInMillis: Long): String {
    val hours = (timeInMillis / (1000 * 60 * 60)) % 24
    val minutes = (timeInMillis / (1000 * 60)) % 60
    val seconds = (timeInMillis / 1000) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun startTrackActivity(context: Context) {
    val intent = Intent(context, TrackActivity::class.java)
    ContextCompat.startActivity(context, intent, null)
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentTime = System.currentTimeMillis()
    return dateFormat.format(Date(currentTime))
}
