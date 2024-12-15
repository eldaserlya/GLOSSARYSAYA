package com.example.glossarysaya.Quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glossarysaya.QuizHistoryActivity
import com.example.glossarysaya.R
import com.example.glossarysaya.UserScore
import com.google.api.ResourceDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizSMA : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppSMA(onBackPressed = { finish() })
        }
    }
}

@Composable
fun QuizAppSMA(onBackPressed: () -> Unit) {
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var answerShown by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(10) }
    var score by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var isQuizStarted by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope() // Menyimpan coroutineScope

    // Setelah kuis selesai di ResultScreen
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    // Simpan skor dan poin untuk setiap level
    editor.putInt("level3_score", score)  // Misalnya, untuk level 1
    editor.putInt("level3_points", score * 10) // Poin bisa berupa skor * faktor tertentu
    editor.putInt("level3_correct_answers", correctAnswers)
    editor.putInt("level3_incorrect_answers", incorrectAnswers)
    editor.apply()

    // Mengambil data soal dari Firebase
    fun fetchQuestions() {
        val database = FirebaseDatabase.getInstance().getReference("quizQuestions")
        database.child("level3").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedQuestions = mutableListOf<QuizQuestion>()
                for (data in snapshot.children) {
                    val question = data.getValue(QuizQuestion::class.java)
                    question?.let { fetchedQuestions.add(it) }
                }
                questions = fetchedQuestions.shuffled().take(10)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }

    // Memanggil fungsi fetch soal saat pertama kali Compose composable dipanggil
    LaunchedEffect(Unit) {
        fetchQuestions()
    }

    // Fungsi untuk melanjutkan soal berikutnya
    fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            selectedAnswerIndex = null
            answerShown = false
            timeLeft = 10
        } else {
            isQuizFinished = true
        }
    }

    // Timer countdown untuk soal
    LaunchedEffect(currentQuestionIndex) {
        while (timeLeft > 0 && !answerShown) {
            delay(1000L)
            timeLeft--
        }
        if (!answerShown) {
            answerShown = true
            incorrectAnswers++
            delay(1000L)
            goToNextQuestion()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4A148C), Color(0xFFCE93D8)),
                )
            )
    ) {
        if (isQuizFinished) {
            ResultScreenSMA(score, correctAnswers, incorrectAnswers)
        } else {
            if (!isQuizStarted || questions.isEmpty()) {
                // Tampilkan countdown dan READY!!! sebelum kuis dimulai
                QuizSplashScreen(onFinish = {
                    isQuizStarted = true
                })
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
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
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Soal ${currentQuestionIndex + 1}/${questions.size}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = timeLeft / 10f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color.Green
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = questions[currentQuestionIndex].question,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    questions[currentQuestionIndex].options.forEachIndexed { index, answer ->
                        AnswerButtonSMA(
                            answer = answer,
                            isSelected = selectedAnswerIndex == index,
                            isCorrect = answer == questions[currentQuestionIndex].correctAnswer,
                            showAnswer = answerShown,
                            onClick = {
                                if (!answerShown) {
                                    selectedAnswerIndex = index
                                    answerShown = true
                                    if (answer == questions[currentQuestionIndex].correctAnswer) {
                                        score += 10
                                        correctAnswers++
                                    } else {
                                        incorrectAnswers++
                                    }

                                    // Menggunakan coroutineScope untuk menunggu selama 3 detik
                                    coroutineScope.launch {
                                        delay(2000L)  // Delay untuk menunjukkan jawaban
                                        goToNextQuestion()
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}




@Composable
fun ResultScreenSMA(score: Int, correctAnswers: Int, incorrectAnswers: Int) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    // Menghitung akurasi
    val accuracy = if (correctAnswers + incorrectAnswers > 0) {
        (correctAnswers.toFloat() / (correctAnswers + incorrectAnswers)) * 100
    } else {
        0f
    }

    // Simpan data ke Firebase
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val database = FirebaseDatabase.getInstance().getReference("users").child(userId ?: "unknown")
    val userScore = UserScore(score, correctAnswers, incorrectAnswers, accuracy)

    // Menyimpan data ke Firebase
    database.child("quizResults").push().setValue(userScore)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Skor berhasil disimpan ke Firebase")
            } else {
                Log.e("Firebase", "Gagal menyimpan skor", task.exception)
            }
        }

    // Layout untuk hasil
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9C27B0)) // Background ungu
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GLOSSARY",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Box untuk menampilkan skor
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFD1C4E9), shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$score",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menampilkan jumlah jawaban benar dan salah
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Green, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Correct: $correctAnswers",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Box(
                modifier = Modifier
                    .background(Color.Red, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Incorrect: $incorrectAnswers",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menampilkan akurasi
        Text(
            text = "Accuracy: ${accuracy.toInt()}%",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol untuk lanjut ke level berikutnya atau mengulang soal
        if (accuracy >= 70) {
            // Tombol untuk lanjut ke level berikutnya
            Button(
                onClick = {
                    // Logika untuk lanjut ke level berikutnya (misalnya ke level2)
                    context.startActivity(Intent(context, QuizUmum::class.java)) // Menggunakan QuizSD untuk level 2
                }
            ) {
                Text("Next Level")
            }
        } else {
            // Tombol untuk mengulang soal
            Button(
                onClick = {
                    // Logika untuk mengulang soal (kembali ke level yang sama)
                    context.startActivity(Intent(context, QuizSMA::class.java)) // Mengulang kuis
                }
            ) {
                Text("Ulangi Kuiz")
            }
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, QuizHistoryActivity::class.java)) // Menggunakan QuizSD untuk level 2
            }
        ) {
            Text("Riwayat Peringkat")
        }
    }
}


@Composable
fun AnswerButtonSMA(
    answer: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit
) {
    // Tentukan warna latar belakang berdasarkan status
    val backgroundColor = when {
        showAnswer && isCorrect -> Color.Green // Jawaban benar berwarna hijau
        showAnswer && !isCorrect -> Color.Red  // Jawaban salah berwarna merah
        isSelected -> Color.LightGray          // Pilihan yang dipilih berwarna abu-abu
        else -> Color.White                   // Warna default putih
    }

    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = answer,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}
