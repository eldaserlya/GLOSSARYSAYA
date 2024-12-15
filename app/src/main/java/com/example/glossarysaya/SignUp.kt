package com.example.glossarysaya

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.glossarysaya.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore // Referensi ke Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance() // Inisialisasi Firestore

        setContent {
            SignUpScreen()
        }
    }

    @Composable
    fun SignUpScreen() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var dob by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var gender by remember { mutableStateOf("") }

        val isFormValid = username.isNotBlank() &&
                password.isNotBlank() &&
                name.isNotBlank() &&
                dob.isNotBlank() &&
                email.isNotBlank() &&
                gender.isNotBlank()

        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dob = "$dayOfMonth-${month + 1}-$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Lapisan pertama: Gradasi warna
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        brush = Brush.linearGradient(
                            colorStops = arrayOf(
                                0.04f to Color(0xA8381E72), // 22% warna ungu
                                1.0f to Color(0xFFF497BF)  // 100% warna pink
                            ),
                            start = Offset(0f, 0f), // Titik awal (kiri)
                            end = Offset(1000f, 0f) // Titik akhir (kanan)
                        )
                    )
            )

            // Lapisan kedua: Card putih dengan ujung atas melengkung
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 30.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                ),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Daftar",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF000000),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input username
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nama Pengguna") },
                        placeholder = { Text("EX : elda") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Icon"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Sandi") },
                        placeholder = { Text("EX : 12345678") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Icon"
                            )
                        },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input nama
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama") },
                        placeholder = { Text("EX : Elda Serlya") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input tanggal lahir
                    OutlinedTextField(
                        value = dob,
                        onValueChange = { dob = it },
                        label = { Text("Tanggal lahir") },
                        placeholder = { Text("EX : 01-01-2000") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Calendar Icon")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Jenis kelamin
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Jenis Kelamin",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp) // Jarak antara teks dan radio buttons
                        )

                        // Pilihan Laki-Laki dan Perempuan
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = gender == "Laki-Laki",
                                onClick = { gender = "Laki-Laki" }
                            )
                            Text("Laki-Laki")
                            Spacer(modifier = Modifier.width(16.dp)) // Jarak antara dua pilihan
                            RadioButton(
                                selected = gender == "Perempuan",
                                onClick = { gender = "Perempuan" }
                            )
                            Text("Perempuan")
                        }
                    }

                    // Input email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("dayy@gmail.com") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions.Default
                    )

                    Spacer(modifier = Modifier.height(110.dp))

                    Button(
                        onClick = {
                            if (!isFormValid) {
                                Toast.makeText(
                                    context,
                                    "Harap isi semua field terlebih dahulu!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Daftarkan user ke Firebase Authentication
                                FirebaseAuth.getInstance()
                                    .createUserWithEmailAndPassword(email.trim(), password.trim())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Dapatkan ID user yang baru dibuat dari Firebase Authentication
                                            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@addOnCompleteListener

                                            // Simpan data tambahan user ke Firestore
                                            val user = User(
                                                username = username,
                                                password = password,
                                                name = name,
                                                dob = dob,
                                                email = email,
                                                gender = gender
                                            )

                                            firestore.collection("users").document(userId)
                                                .set(user)
                                                .addOnCompleteListener { dbTask ->
                                                    if (dbTask.isSuccessful) {
                                                        // Notifikasi dan navigasi ke halaman login
                                                        Toast.makeText(
                                                            context,
                                                            "Sign Up Successful!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                                                        finish()
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Data Save Failed: ${dbTask.exception?.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        } else {
                                            // Tampilkan error jika pendaftaran gagal
                                            Toast.makeText(
                                                context,
                                                "Sign Up Failed: ${task.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(bottom = 10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF3C0CA6)),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Daftar", color = Color.White)
                    }
                }
            }
        }
    }
}

