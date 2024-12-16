package com.example.glossarysaya

import android.content.Context
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glossarysaya.ui.theme.GLOSSARYSAYATheme
import com.google.firebase.database.*

data class UserRank(val name: String, val score: Int, val imageRes: Int = R.drawable.wajah)

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
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userName = sharedPref.getString("user_name", "Unknown") ?: "Unknown" // Ambil nama pengguna dari SharedPreferences
    val userScore = sharedPref.getInt("user_score", 0) // Ambil skor pengguna

    val database = FirebaseDatabase.getInstance().reference.child("users")
    var userRanks by remember { mutableStateOf(listOf<UserRank>()) }

    LaunchedEffect(Unit) {
        // Mengambil data dari Firebase dan mengurutkan berdasarkan poin tertinggi
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ranks = mutableListOf<UserRank>()
                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java) ?: "Unknown"
                    // Mengambil score dari quizResults
                    val score = userSnapshot.child("quizResults").children
                        .mapNotNull { it.child("score").getValue(Int::class.java) }
                        .firstOrNull() ?: 0
                    ranks.add(UserRank(name, score))
                }
                // Mengurutkan peringkat berdasarkan score tertinggi
                ranks.sortedByDescending { it.score }

                // Menambahkan pengguna yang sedang login ke dalam peringkat
                ranks.add(0, UserRank(userName, userScore))

                userRanks = ranks
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF381E72), Color(0xFFFFFFFF))
                    )
                )
                .padding(16.dp)
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

            // Peringkat Teratas (Top 3)
            if (userRanks.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val topRanks = userRanks.take(3) // Mengambil 3 peringkat teratas
                    topRanks.forEachIndexed { index, user ->
                        TopRankItem(
                            name = user.name,
                            score = user.score,
                            imageRes = user.imageRes,
                            rank = index + 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Peringkat Lain (Rank lain setelah top 3)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    userRanks.drop(3).forEachIndexed { index, user ->  // Mengambil data peringkat setelah top 3
                        RankItem(
                            rank = index + 4,
                            name = user.name,
                            score = user.score,
                            imageRes = user.imageRes
                        )
                    }
                }
            } else {
                Text(
                    text = "Memuat data peringkat...",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
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

