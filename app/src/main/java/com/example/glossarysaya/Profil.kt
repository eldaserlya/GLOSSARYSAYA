package com.example.glossarysaya

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glossarysaya.ui.theme.GLOSSARYSAYATheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class ProfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GLOSSARYSAYATheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF3A007D), // Dark purple
                    Color.White
                )
            )),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PROFIL",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        ProfileCard()
        ProfilBottomNavigationBar()
    }
}

@Composable
fun ProfileCard() {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val firestore = FirebaseFirestore.getInstance()

    val userProfile = remember { mutableStateOf<UserProfile?>(null) }

    LaunchedEffect(Unit) {
        firestore.collection("users").document(userId ?: "")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(UserProfile::class.java)
                    userProfile.value = user
                } else {
                    // Handle jika data tidak ditemukan
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        IconButton(
            onClick = {
                val intent = Intent(context, ProfilEditActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Edit Profile",
                tint = Color.Black,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wajah),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            userProfile.value?.let { user ->
                ProfileInfoBox(text = user.name ?: "Tidak diketahui")
                ProfileInfoBox(text = user.birthDate ?: "Tidak diketahui")
                ProfileInfoBox(text = user.gender ?: "Tidak diketahui")
                ProfileInfoBox(text = user.email ?: "Tidak diketahui")
                ProfileInfoBox(text = "${user.points} Poin")
            } ?: run {
                Text(text = "Memuat...", color = Color.Gray)
            }

            Divider(
                color = Color(0xFF6A1B9A),
                thickness = 2.dp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileInfoBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}

@Composable
fun ProfilBottomNavigationBar() {
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
            IconButton(onClick = { context.startActivity(Intent(context, Home::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home"
                )
            }
            IconButton(onClick = { context.startActivity(Intent(context, PeringkatActivity::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.tropy),
                    contentDescription = "Trophy"
                )
            }
            IconButton(onClick = { context.startActivity(Intent(context, QuizHistoryActivity::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = "Bookmark"
                )
            }
            IconButton(onClick = { context.startActivity(Intent(context, ProfilActivity::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "User"
                )
            }
        }
    }
}

data class UserProfile(
    val name: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val email: String? = null,
    val points: Int = 0
)
