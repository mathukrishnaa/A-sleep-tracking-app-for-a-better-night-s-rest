package com.example.projectone

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background  // Import this
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectone.ui.theme.ProjectOneTheme
import java.util.*

class TrackActivity : ComponentActivity() {

    private lateinit var databaseHelper: TimeLogDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseHelper = TimeLogDatabaseHelper(this)
        setContent {
            ProjectOneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val timeLogs = databaseHelper.getTimeLogs()
                    Log.d("Sandeep", timeLogs.toString())
                    ListListScopeSample(timeLogs)
                }
            }
        }
    }
}

@Composable
fun ListListScopeSample(timeLogs: List<TimeLogDatabaseHelper.TimeLog>) {
    val imageModifier = Modifier
    Image(
        painterResource(id = R.drawable.sleeptracking),
        contentScale = ContentScale.FillHeight,
        contentDescription = "",
        modifier = imageModifier.alpha(0.3F)
    )

    // Title
    Text(
        text = "Sleep Tracking",
        modifier = Modifier
            .padding(top = 16.dp, start = 106.dp),
        color =  Color(0xFF0288D1),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(30.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Space between cards
    ) {
        items(timeLogs) { timeLog: TimeLogDatabaseHelper.TimeLog ->  // Specify the type here
            TimeLogCard(timeLog)
        }
    }
}

@Composable
fun TimeLogCard(timeLog: TimeLogDatabaseHelper.TimeLog) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp,
        backgroundColor = Color(0xFFBBDEFB),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Sleep Duration (Start and End time difference)
            val sleepDuration = timeLog.endTime?.let { timeLog.endTime - timeLog.startTime } ?: 0L
            val formattedSleepTime = formatDuration(sleepDuration)

            // Sleep Time before Start (with improved format)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0xFF0288D1), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Sleep Time: $formattedSleepTime",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Start time
            Text(
                text = "Start Time:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                fontSize = 14.sp
            )
            Text(
                text = formatDateTime(timeLog.startTime),
                fontSize = 16.sp,
                color = Color(0xFF0D47A1),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // End time
            Text(
                text = "End Time:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                fontSize = 14.sp
            )
            Text(
                text = timeLog.endTime?.let { formatDateTime(it) } ?: "Not available",
                fontSize = 16.sp,
                color = Color(0xFF0D47A1),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Format Duration (Time difference between start and end)
private fun formatDuration(durationMillis: Long): String {
    val hours = (durationMillis / (1000 * 60 * 60)) % 24
    val minutes = (durationMillis / (1000 * 60)) % 60
    val seconds = (durationMillis / 1000) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

// Format Date and Time
private fun formatDateTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm a", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
