package com.example.glossarysaya

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
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
import coil.compose.rememberAsyncImagePainter
import com.example.glossarysaya.ui.theme.GLOSSARYSAYATheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ProfilEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GLOSSARYSAYATheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ProfilEditScreen(onBackPressed = { finish() })
                }
            }
        }
    }
}

@Composable
fun ProfilEditScreen(onBackPressed: () -> Unit) {
    var profileImagePath by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            saveImageToLocalStorage(it, context) { filePath ->
                profileImagePath = filePath
                saveProfileImagePathToFirestore(filePath) // Simpan path gambar ke Firestore
            }
        }
    }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3A007D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "PROFIL EDIT",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            /*// Garis di bawah judul "PROFIL"
            Divider(
                color = Color.White, //warna garis
                thickness = 2.dp,
                modifier = Modifier.padding(top = 3.dp, bottom = 6.dp)
            )
             */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(4.dp, Color.White, CircleShape)
                ) {
                    if (profileImagePath != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profileImagePath),
                            contentDescription = "Profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder),
                            contentDescription = "Profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(text = "Ubah Foto", color = Color.Gray)
                }
            }

            ProfilEditCard(
                name = name,
                onNameChange = { name = it },
                dateOfBirth = dateOfBirth,
                onDateOfBirthChange = { dateOfBirth = it },
                gender = gender,
                onGenderChange = { gender = it },
                onDatePickerClick = { datePickerDialog.show() },
                context = context,
                profileImagePath = profileImagePath
            )
        }
    }
}

@Composable
fun ProfilEditCard(
    name: String,
    onNameChange: (String) -> Unit,
    dateOfBirth: String,
    onDateOfBirthChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    onDatePickerClick: () -> Unit,
    context: Context,
    profileImagePath: String?
) {
    Card(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nama") },
                placeholder = { Text("Type here") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = onDateOfBirthChange,
                label = { Text("Tanggal lahir") },
                placeholder = { Text("Select") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = onDatePickerClick) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Select date")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == "Laki-Laki",
                    onClick = { onGenderChange("Laki-Laki") }
                )
                Text(text = "Laki-Laki", modifier = Modifier.padding(end = 16.dp))

                RadioButton(
                    selected = gender == "Perempuan",
                    onClick = { onGenderChange("Perempuan") }
                )
                Text(text = "Perempuan")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { saveUserProfile(name, dateOfBirth, gender, profileImagePath, context) },
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Simpan", color = Color.White)
            }
        }
    }
}

// Function to save the profile image to local storage and return the file path
fun saveImageToLocalStorage(imageUri: Uri, context: Context, onImageSaved: (String) -> Unit) {
    try {
        val file = File(context.filesDir, "profile_${UUID.randomUUID()}.jpg") // Tentukan nama file
        val outputStream = FileOutputStream(file)
        val inputStream = context.contentResolver.openInputStream(imageUri)

        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()

        val imagePath = file.absolutePath // Ambil path gambar

        // Simpan path di Firestore
        onImageSaved(imagePath)
    } catch (e: IOException) {
        Toast.makeText(context, "Gagal menyimpan gambar: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun saveProfileImagePathToFirestore(imagePath: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val userProfileRef = FirebaseFirestore.getInstance().collection("users").document(userId)

    userProfileRef.update("profileImage", imagePath)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ProfilEdit", "Path gambar profil berhasil disimpan di Firestore")
            } else {
                Log.e("ProfilEdit", "Gagal menyimpan path gambar profil di Firestore: ${task.exception?.message}")
            }
        }
}

fun saveUserProfile(name: String, dateOfBirth: String, gender: String, imagePath: String?, context: Context) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

    val userProfile = hashMapOf(
        "name" to name,
        "dateOfBirth" to dateOfBirth,
        "gender" to gender,
        "profileImage" to (imagePath ?: "")
    )

    userRef.set(userProfile, SetOptions.merge()).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Gagal menyimpan profil", Toast.LENGTH_SHORT).show()
        }
    }
}