package com.example.glossarysaya

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.example.glossaryy.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            SignInScreen()
        }
    }

    @Composable
    fun SignInScreen() {
        var username = remember { mutableStateOf("") }
        var password = remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Lapisan pertama: warna putih
        ) {
            // Lapisan kedua: gradasi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(y = 310.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp
                        )
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colorStops = arrayOf(
                                0.04f to Color(0xA8381E72),
                                1.0f to Color(0xFFF497BF)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 0f)
                        )
                    )
                    .align(Alignment.BottomCenter)
            )

            // Lapisan ketiga: Card putih
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(y = 335.dp)
                    .align(Alignment.BottomCenter)
                    .clip(
                        RoundedCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp
                        )
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Judul "Masuk"
                    Text(
                        text = "Masuk",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF3D57A1)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Input username
                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text("Email") },
                        placeholder = { Text("EX : elda@example.com") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Icon"
                            )
                        }
                    )

                    TextButton(
                        onClick = { /* Handle forgot username */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Lupa nama pengguna", color = Color(0xFF3D57A1))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input password
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Sandi") },
                        placeholder = { Text("EX : password123") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Icon"
                            )
                        },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    TextButton(
                        onClick = { /* Handle forgot password */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Lupa sandi", color = Color(0xFF3D57A1))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tombol login
                    Button(
                        onClick = {
                            firebaseAuth.signInWithEmailAndPassword(
                                username.value.trim(),
                                password.value.trim()
                            ).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Ambil data pengguna dari Firestore setelah login
                                    val userId = firebaseAuth.currentUser?.uid
                                    if (userId != null) {
                                        firestore.collection("users").document(userId)
                                            .get()
                                            .addOnSuccessListener { document ->
                                                if (document.exists()) {
                                                    // Data pengguna ditemukan
                                                    val intent = Intent(this@SignInActivity, Home::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                } else {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Data pengguna tidak ditemukan di Firestore",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Gagal mengambil data pengguna: ${exception.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }

                                    /*val intent = Intent(this@SignInActivity, Home::class.java)
                                    startActivity(intent)
                                    finish()*/
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login gagal: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C0CA6)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Masuk", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tombol daftar
                    TextButton(
                        onClick = {
                            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Belum punya akun? Daftar Sekarang",
                            color = Color(0xFF3D57A1)
                        )
                    }
                }
            }

            // Lapisan pertama: Teks "Glossary" dan logo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GLOSSARY",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF3D57A1)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tambahkan logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(90.dp)
                )
            }
        }
    }
}