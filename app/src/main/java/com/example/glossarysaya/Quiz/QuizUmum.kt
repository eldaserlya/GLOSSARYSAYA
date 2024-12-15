package com.example.glossarysaya.Quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizUmum : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppUmum()
        }
    }
}

@Composable
fun QuizAppUmum() {
    val questions = listOf(
        QuestionUmum("Apa nama ibu kota dari negara Prancis?", listOf("Berlin", "Madrid", "Paris", "Roma"), 2),
        QuestionUmum("Apa nama planet yang terdekat dengan Matahari?", listOf("Mars", "Merkurius", "Venus", "Bumi"), 1),
        QuestionUmum("Apa nama hewan khas Australia yang melompat?", listOf("Kanguru", "Koala", "Dingo", "Wombat"), 0),
        QuestionUmum("Siapa penemu telepon?", listOf("Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Michael Faraday"), 0),
        QuestionUmum("Apa nama zat yang memberikan warna hijau pada tumbuhan?", listOf("Melanin", "Klorofil", "Karoten", "Hemoglobin"), 1),
        QuestionUmum("Apa nama gunung tertinggi di dunia?", listOf("Gunung Everest", "Gunung Kilimanjaro", "Gunung Elbrus", "Gunung Denali"), 0),
        QuestionUmum("Siapa pelukis terkenal yang melukis 'Starry Night'?", listOf("Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Claude Monet"), 1),
        QuestionUmum("Apa nama negara dengan jumlah penduduk terbanyak di dunia?", listOf("India", "Amerika Serikat", "Cina", "Indonesia"), 2),
        QuestionUmum("Apa hasil dari 3 x 9?", listOf("27", "28", "29", "30"), 0),
        QuestionUmum("Apa nama tulang terpanjang dalam tubuh manusia?", listOf("Tulang Paha", "Tulang Lengan", "Tulang Rusuk", "Tulang Belakang"), 0),
        QuestionUmum("Apa nama hari libur nasional pada tanggal 1 Januari?", listOf("Hari Pendidikan", "Hari Tahun Baru", "Hari Kemerdekaan", "Hari Kartini"), 1),
        QuestionUmum("Apa nama benda yang digunakan untuk membaca?", listOf("Meja", "Buku", "Pensil", "Lampu"), 1),
        QuestionUmum("Apa nama logam yang biasanya digunakan untuk membuat uang koin?", listOf("Besi", "Perak", "Aluminium", "Tembaga"), 3),
        QuestionUmum("Apa nama alat musik yang dimainkan dengan cara ditiup?", listOf("Gitar", "Piano", "Seruling", "Drum"), 2),
        QuestionUmum("Apa nama samudra terbesar di dunia?", listOf("Samudra Atlantik", "Samudra Pasifik", "Samudra Hindia", "Samudra Arktik"), 1),
        QuestionUmum("Apa nama hewan laut yang memiliki tentakel?", listOf("Ubur-ubur", "Ikan Paus", "Lumba-lumba", "Hiu"), 0),
        QuestionUmum("Siapa penulis buku 'Harry Potter'?", listOf("J.K. Rowling", "Tolkien", "Stephen King", "George R.R. Martin"), 0),
        QuestionUmum("Berapa hasil dari 100 ÷ 4?", listOf("20", "25", "30", "40"), 1),
        QuestionUmum("Apa nama negara di Asia Tenggara yang tidak memiliki garis pantai?", listOf("Thailand", "Laos", "Vietnam", "Kamboja"), 1),
        QuestionUmum("Apa nama benda yang digunakan untuk menyikat gigi?", listOf("Handuk", "Sikat Gigi", "Pisau", "Kaca"), 1),
        QuestionUmum("Apa nama alat yang digunakan untuk memasak nasi?", listOf("Kompor", "Rice Cooker", "Penggorengan", "Oven"), 1),
        QuestionUmum("Apa nama logam yang biasa digunakan pada kabel listrik?", listOf("Aluminium", "Tembaga", "Besi", "Perak"), 1),
        QuestionUmum("Siapa presiden pertama Amerika Serikat?", listOf("Abraham Lincoln", "George Washington", "Thomas Jefferson", "Theodore Roosevelt"), 1),
        QuestionUmum("Apa nama alat untuk mengukur suhu tubuh?", listOf("Termometer", "Barometer", "Anemometer", "Hygrometer"), 0),
        QuestionUmum("Apa nama benda yang bisa menerangi jalan saat gelap?", listOf("Lampu Jalan", "Jam", "Kompor", "Meja"), 0),
        QuestionUmum("Apa nama kendaraan yang berjalan di atas rel?", listOf("Bus", "Sepeda", "Kereta", "Truk"), 2),
        QuestionUmum("Siapa tokoh yang dikenal sebagai Bapak Pendidikan Nasional Indonesia?", listOf("Ki Hajar Dewantara", "Soekarno", "Mohammad Hatta", "R.A. Kartini"), 0),
        QuestionUmum("Apa nama olahraga yang menggunakan raket dan shuttlecock?", listOf("Tenis", "Bulu Tangkis", "Ping Pong", "Hoki"), 1),
        QuestionUmum("Apa nama bahan makanan yang dibuat dari kedelai?", listOf("Tempe", "Roti", "Susu", "Keju"), 0),
        QuestionUmum("Apa nama planet tempat manusia tinggal?", listOf("Mars", "Venus", "Bumi", "Saturnus"), 2),
        QuestionUmum("Apa nama benda yang digunakan untuk melindungi kepala?", listOf("Jas Hujan", "Helm", "Topi", "Selimut"), 1),
        QuestionUmum("Siapa penemu lampu pijar?", listOf("Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Michael Faraday"), 1),
        QuestionUmum("Apa nama festival cahaya terkenal di India?", listOf("Diwali", "Holi", "Songkran", "Loy Krathong"), 0),
        QuestionUmum("Apa nama benda yang digunakan untuk menghapus tulisan?", listOf("Kertas", "Penghapus", "Kaca", "Lampu"), 1),
        QuestionUmum("Apa nama hewan yang bisa hidup di air dan darat?", listOf("Amfibi", "Mamalia", "Reptil", "Serangga"), 0),
        QuestionUmum("Apa warna pelangi yang berada di bagian bawah?", listOf("Merah", "Hijau", "Ungu", "Kuning"), 2),
        QuestionUmum("Apa nama proses perubahan air menjadi uap?", listOf("Menguap", "Mencair", "Mengembun", "Membeku"), 0),
        QuestionUmum("Apa nama planet yang dikenal sebagai 'Planet Cincin'?", listOf("Jupiter", "Uranus", "Saturnus", "Mars"), 2)
    )



    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var answerShown by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(10) }
    val scope = rememberCoroutineScope()

    fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            selectedAnswerIndex = null
            answerShown = false
            timeLeft = 10 // Reset timer
        }
    }

    LaunchedEffect(currentQuestionIndex) {
        // Countdown timer
        while (timeLeft > 0 && !answerShown) {
            delay(1000L) // 1 detik
            timeLeft--
        }
        if (!answerShown) {
            answerShown = true
            delay(1000L) // Tampilkan jawaban benar selama 1 detik sebelum lanjut
            goToNextQuestion()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Jumlah soal yang sudah dikerjakan
        Text(
            text = "Soal ${currentQuestionIndex + 1}/${questions.size}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar untuk countdown detik
        LinearProgressIndicator(
            progress = timeLeft / 10f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Color.Green
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Teks pertanyaan
        Text(text = questions[currentQuestionIndex].question, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))
        questions[currentQuestionIndex].answers.forEachIndexed { index, answer ->
            AnswerButtonUmum(
                answer = answer,
                isSelected = selectedAnswerIndex == index,
                isCorrect = index == questions[currentQuestionIndex].correctAnswerIndex,
                showAnswer = answerShown,
                onClick = {
                    if (!answerShown) {
                        selectedAnswerIndex = index
                        answerShown = true
                        scope.launch {
                            delay(1000L) // Tampilkan jawaban selama 1 detik sebelum lanjut
                            goToNextQuestion()
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnswerButtonUmum(
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

data class QuestionUmum(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

