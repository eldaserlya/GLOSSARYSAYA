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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizSD : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppSD(onBackPressed = { finish() })
        }
    }
}

@Composable
fun QuizAppSD(onBackPressed: () -> Unit) {
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
    var countdownValue by remember { mutableStateOf(3) }
    var readyText by remember { mutableStateOf("READY!!!") }
    val scope = rememberCoroutineScope()

    // Mengambil data soal dari Firebase
    fun fetchQuestions() {
        val database = FirebaseDatabase.getInstance().getReference("quizQuestions")
        database.child("level1").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedQuestions = mutableListOf<QuizQuestion>()
                for (data in snapshot.children) {
                    val question = data.getValue(QuizQuestion::class.java)
                    question?.let { fetchedQuestions.add(it) }
                }
                // Ambil 10 soal acak dari soal yang ada
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

    // Menggunakan QuizSplashScreen untuk countdown
    QuizSplashScreen(onFinish = {
        isQuizStarted = true
    })


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
            ResultScreen(score, correctAnswers, incorrectAnswers)
        } else {
            if (!isQuizStarted || questions.isEmpty()) {
                // Tampilkan countdown dan READY!!! sebelum kuis dimulai
                QuizSplashScreen(onFinish = {
                    isQuizStarted = true
                })
                //CountdownScreen(readyText)
            } else {
                // Setelah countdown selesai, tampilkan soal kuis
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
                        AnswerButtonSD(
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
                                    scope.launch {
                                        delay(1000L)
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
fun CountdownScreen(readyText: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9C27B0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = readyText,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ResultScreen(score: Int, correctAnswers: Int, incorrectAnswers: Int) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

// Simpan data level (misalnya, level 1)
    editor.putInt("level1_score", score)
    //editor.putInt("level1_points", score) // Misalnya, kita anggap poin = skor
    editor.putInt("level1_correct_answers", correctAnswers)
    editor.putInt("level1_incorrect_answers", incorrectAnswers)
    editor.apply()

// Menghitung akurasi
    val accuracy = if (correctAnswers + incorrectAnswers > 0) {
        (correctAnswers.toFloat() / (correctAnswers + incorrectAnswers)) * 100
    } else {
        0f
    }

// Simpan akurasi (persentase kebenaran)
    editor.putFloat("level1_accuracy", accuracy)
    editor.apply()

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

        Button(
            onClick = {
                // Simpan skor ke SharedPreferences
                val sharedPref = context.getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE)
                val currentPoints = sharedPref.getInt("userPoints", 0) // Ambil poin sebelumnya
                sharedPref.edit().putInt("userPoints", currentPoints + score).apply()

                // Arahkan ke layar Riwayat
                context.startActivity(Intent(context, QuizHistoryActivity::class.java))
            }
        ) {
            Text("Go to History")
        }
    }
}

@Composable
fun AnswerButtonSD(
    answer: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        //isSelected && showAnswer -> if (isCorrect) Color.Green else Color.Red
        showAnswer && isCorrect -> Color.Green // Jawaban benar menjadi hijau
        showAnswer && !isCorrect -> Color.Red  // Jawaban salah menjadi merah
        //isSelected -> Color.LightGray  // Pilihan yang dipilih memberi warna abu-abu
        else -> Color.White
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