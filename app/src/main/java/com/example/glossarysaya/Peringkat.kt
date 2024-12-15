package com.example.glossarysaya

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

class PeringkatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GLOSSARYSAYATheme {
                PeringkatScreen()
            }
        }
    }
}

@Composable
fun PeringkatScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() } // Menambahkan Bottom Navigation Bar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Menambahkan inner padding agar konten tidak tertutup navbar
                .verticalScroll(rememberScrollState()) // Mengaktifkan scrolling
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF673AB7), Color(0xFF9C27B0))
                    )
                )
                .padding(16.dp) // Padding luar untuk estetika
        ) {
            // Header
            Text(
                text = "PERINGKAT",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Peringkat Teratas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                TopRankItem(name = "Elda", score = 7300, imageRes = R.drawable.wajah, rank = 2)
                TopRankItem(name = "Diaz", score = 7900, imageRes = R.drawable.wajah, rank = 1)
                TopRankItem(name = "Fadillah", score = 6800, imageRes = R.drawable.wajah, rank = 3)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Peringkat Lain
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                RankItem(rank = 4, name = "Chandra", score = 6700, imageRes = R.drawable.wajah)
                RankItem(rank = 5, name = "Dina", score = 6300, imageRes = R.drawable.wajah)
                RankItem(rank = 6, name = "Faulah", score = 6050, imageRes = R.drawable.wajah)
                RankItem(rank = 7, name = "Dayinta", score = 5900, imageRes = R.drawable.wajah)
                RankItem(rank = 8, name = "Dwi", score = 5400, imageRes = R.drawable.wajah)
                RankItem(rank = 9, name = "Serlya", score = 5210, imageRes = R.drawable.wajah)
                RankItem(rank = 10, name = "Azkha", score = 5030, imageRes = R.drawable.wajah)
            }
        }
    }
}

@Composable
fun TopRankItem(name: String, score: Int, imageRes: Int, rank: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }
        Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Box(
            modifier = Modifier
                .background(Color(0xFFFF9900), shape = RoundedCornerShape(15.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(text = score.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun RankItem(rank: Int, name: String, score: Int, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFD1C4E9), shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = rank.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = score.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavigationBar() {
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
