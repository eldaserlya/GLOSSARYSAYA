package com.example.glossarysaya.Quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class SplashStart : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    QuizSplashScreen(onFinish = {
                        // Navigasi ke layar kuis atau logika lainnya setelah hitung mundur selesai
                    })
                }
            }
        }
    }
}

@Composable
fun QuizSplashScreen(onFinish: () -> Unit) {
    var countdown by remember { mutableStateOf(4) } // Mulai dari 4 untuk menampilkan "Ready" lebih dulu

    // Animasi transisi angka (opsional)
    val animatedCount by animateIntAsState(targetValue = countdown)

    LaunchedEffect(key1 = countdown) {
        if (countdown > 0) {
            delay(1000L) // Delay 1 detik
            countdown--
        } else {
            onFinish() // Panggil callback ketika hitung mundur selesai
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.04f to Color(0xA8381E72),
                        1.0f to Color(0xFFF497BF)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (animatedCount > 3) "SIAP!" else "${animatedCount}",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
