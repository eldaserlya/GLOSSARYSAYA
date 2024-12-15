package com.example.glossarysaya.Quiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizSMP : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppSMP(onBackPressed = { finish() })
        }
    }
}

@Composable
fun QuizAppSMP(onBackPressed: () -> Unit) {
    val questions = listOf(
        QuestionSMP("Seekor kambing memiliki berat badan 30 kg. Jika beratnya bertambah 15%, berapakah beratnya sekarang?", listOf("33 kg", "34.5 kg", "35 kg", "36 kg"), 1),
        QuestionSMP("Siapakah penulis novel 'Laskar Pelangi'?", listOf("Andrea Hirata", "Tere Liye", "Pramoedya Ananta Toer", "Habiburrahman El Shirazy"), 0),
        QuestionSMP("Apa rumus kimia dari garam dapur?", listOf("H2O", "CO2", "NaCl", "KCl"), 2),
        QuestionSMP("Kerajaan Hindu pertama di Indonesia adalah?", listOf("Sriwijaya", "Kutai", "Majapahit", "Tarumanegara"), 1),
        QuestionSMP("Manakah dari berikut ini yang bukan merupakan jenis energi terbarukan?", listOf("Angin", "Surya", "Minyak Bumi", "Air"), 2),
        QuestionSMP("Berapa banyak sisi yang dimiliki oleh prisma segitiga?", listOf("5", "6", "7", "8"), 1),
        QuestionSMP("Siapakah tokoh yang dikenal sebagai 'Bapak Pramuka Indonesia'?", listOf("Soedirman", "Ir. Soekarno", "Sri Sultan Hamengkubuwono IX", "Ki Hajar Dewantara"), 2),
        QuestionSMP("Apa hasil dari 12 x 8 ÷ 4 + 6?", listOf("18", "24", "36", "42"), 1),
        QuestionSMP("Choose the correct word: 'She ---- her project before the deadline.'", listOf("complete", "completed", "completes", "completing"), 1),
        QuestionSMP("Which country is known as the 'Land of the Rising Sun'?", listOf("China", "Japan", "South Korea", "India"), 1),
        QuestionSMP("Berikut ini adalah planet yang memiliki satelit alami bernama 'Titan'. Planet apakah itu?", listOf("Mars", "Saturnus", "Jupiter", "Neptunus"), 1),
        QuestionSMP("Siapakah penemu telepon?", listOf("Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Michael Faraday"), 0),
        QuestionSMP("Pancasila pertama kali dirumuskan dalam sidang apa?", listOf("Sidang BPUPKI", "Sidang PPKI", "Sidang KNIP", "Sidang Konstituante"), 0),
        QuestionSMP("Berapa banyak rusuk yang dimiliki oleh kubus?", listOf("6", "8", "10", "12"), 3),
        QuestionSMP("Apa kepanjangan dari WHO?", listOf("World Health Organization", "World Human Organization", "World Hope Organization", "World Help Organization"), 0),
        QuestionSMP("Dalam permainan badminton, berapa poin yang dibutuhkan untuk memenangkan satu game?", listOf("15", "21", "25", "30"), 1),
        QuestionSMP("Berikut ini adalah contoh bahan bakar fosil, kecuali?", listOf("Minyak Bumi", "Batubara", "Gas Alam", "Angin"), 3),
        QuestionSMP("Siapakah tokoh yang dikenal sebagai 'Bapak Pendidikan Nasional'?", listOf("Soekarno", "Mohammad Hatta", "Ki Hajar Dewantara", "Kartini"), 2),
        QuestionSMP("Choose the correct form: 'They ---- to the park every weekend.'", listOf("go", "went", "going", "goes"), 0),
        QuestionSMP("Apa nama samudra terbesar di dunia?", listOf("Samudra Hindia", "Samudra Atlantik", "Samudra Pasifik", "Samudra Arktik"), 2)
    )


    val randomQuestions = remember { questions.shuffled().take(5) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var answerShown by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(10) }
    var score by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }
    var incorrectAnswers by remember { mutableStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun goToNextQuestion() {
        if (currentQuestionIndex < randomQuestions.size - 1) {
            currentQuestionIndex++
            selectedAnswerIndex = null
            answerShown = false
            timeLeft = 10 // Reset timer
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
                            painter = painterResource(id = R.drawable.arrow_back), // Pastikan ada ikon panah di res/drawable
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.logo), // Pastikan file logo ada di res/drawable
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Soal ${currentQuestionIndex + 1}/${randomQuestions.size}",
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
                    text = randomQuestions[currentQuestionIndex].question,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                randomQuestions[currentQuestionIndex].answers.forEachIndexed { index, answer ->
                    AnswerButtonSD(
                        answer = answer,
                        isSelected = selectedAnswerIndex == index,
                        isCorrect = index == randomQuestions[currentQuestionIndex].correctAnswerIndex,
                        showAnswer = answerShown,
                        onClick = {
                            if (!answerShown) {
                                selectedAnswerIndex = index
                                answerShown = true
                                if (index == randomQuestions[currentQuestionIndex].correctAnswerIndex) {
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
@Composable
fun ResultScreenSMP(score: Int, correctAnswers: Int, incorrectAnswers: Int) {
    val context = LocalContext.current
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
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Benar", fontSize = 18.sp, color = Color.White)
                    Text("$correctAnswers", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Box(
                modifier = Modifier
                    .background(Color.Red, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Salah", fontSize = 18.sp, color = Color.White)
                    Text("$incorrectAnswers", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val intent = Intent(context, QuizSMA::class.java)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }) {
            Text("Level Selanjutnya")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val intent = Intent(context, QuizHistoryActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Lihat Halaman Peringkat")
        }
    }
}

@Composable
fun AnswerButtonSMP(
    answer: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showAnswer && isCorrect -> Color.Green
        showAnswer && !isCorrect && isSelected -> Color.Red
        else -> Color.LightGray
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        enabled = !showAnswer
    ) {
        Text(text = answer, fontSize = 16.sp, color = Color.White)
    }
}

data class QuestionSMP(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

