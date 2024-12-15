package com.example.glossarysaya

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glossarysaya.ui.theme.GLOSSARYSAYATheme

class QuizHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GLOSSARYSAYATheme  {
                QuizHistoryScreen()
            }
        }
    }
}

@Composable
fun QuizHistoryScreen() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE)

    // Ambil data level 1, 2, 3, 4 dari SharedPreferences
    val level1Data = getLevelData(sharedPref, 1)
    val level2Data = getLevelData(sharedPref, 2)
    val level3Data = getLevelData(sharedPref, 3)
    val level4Data = getLevelData(sharedPref, 4)

    // Hitung total skor
    val totalScore = level1Data.score + level2Data.score + level3Data.score + level4Data.score

    // Hitung total poin (jumlah dari poin tiap level)
    val totalPoints = (level1Data.points + level2Data.points + level3Data.points + level4Data.points) / 10

    // Simpan total skor ke SharedPreferences jika perlu
    sharedPref.edit().putInt("total_score", totalScore).apply()

    Scaffold(
        bottomBar = { HistoryBottomNavigationBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF381E72), Color(0xFFFFFFFF))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "RIWAYAT KUIS",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Menampilkan item untuk level 1
                QuizItem(
                    title = "Level 1",
                    score = level1Data.score,
                    points = level1Data.points,
                    accuracy = level1Data.accuracy.toInt(),
                    imageRes = R.drawable.level1,
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan item untuk level 2
                QuizItem(
                    title = "Level 2",
                    score = level2Data.score,
                    points = level2Data.points,
                    accuracy = level2Data.accuracy.toInt(),
                    imageRes = R.drawable.level2
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan item untuk level 3
                QuizItem(
                    title = "Level 3",
                    score = level3Data.score,
                    points = level3Data.points,
                    accuracy = level3Data.accuracy.toInt(),
                    imageRes = R.drawable.level3
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan item untuk level 4
                QuizItem(
                    title = "Level 4",
                    score = level4Data.score,
                    points = level4Data.points,
                    accuracy = level4Data.accuracy.toInt(),
                    imageRes = R.drawable.level4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan Total Skor dan Poin
                Text(
                    text = "Total Point: $totalPoints",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


fun getLevelData(sharedPref: SharedPreferences, level: Int): LevelData {
    val correctAnswers = sharedPref.getInt("level${level}_correct_answers", 0)
    val incorrectAnswers = sharedPref.getInt("level${level}_incorrect_answers", 0)
    val score = sharedPref.getInt("level${level}_score", 0)
    val points = sharedPref.getInt("level${level}_points", 0)

    val accuracy = if (correctAnswers + incorrectAnswers > 0) {
        (correctAnswers.toFloat() / (correctAnswers + incorrectAnswers)) * 100
    } else 0f

    return LevelData(score, points, accuracy)
}


data class LevelData(val score: Int, val points: Int, val accuracy: Float)


@Composable
fun QuizItem(title: String, score: Int, points: Int, accuracy: Int, imageRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Blue, shape = RoundedCornerShape(20.dp))
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(Color(0xFFB6A4FF), shape = RoundedCornerShape(10.dp))
                    .padding(6.dp)
            ) {
                Column {
                    Text(
                        text = "Score : $score",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Akurasi", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color(0xFFBDBDBD), shape = RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    //.width((accuracy * 3).dp)
                    .fillMaxWidth(accuracy / 100f)
                    .background(Color(0xFF673AB7), shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "$accuracy%",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun HistoryBottomNavigationBar() {
    val context = LocalContext.current

    BottomAppBar(
        containerColor = Color(0xFF3C0CA6),
        contentColor = Color.White,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val intent = Intent(context, Home::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home"
                )
            }
            IconButton(onClick = {
                val intent = Intent(context, PeringkatActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.tropy),
                    contentDescription = "Trophy"
                )
            }
            IconButton(onClick = {
                val intent = Intent(context, QuizHistoryActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = "Bookmark"
                )
            }
            IconButton(onClick = {
                val intent = Intent(context, ProfilActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "User"
                )
            }
        }
    }
}
