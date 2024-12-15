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

class QuizSMA : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppSMA()
        }
    }
}

@Composable
fun QuizAppSMA() {
    val questions = listOf(
        QuestionSMA("Seekor kambing memiliki berat badan 30 kg. Jika beratnya bertambah 15%, berapakah beratnya sekarang?", listOf("33 kg", "34.5 kg", "35 kg", "36 kg"), 1),
        QuestionSMA("Siapakah penulis novel 'Laskar Pelangi'?", listOf("Andrea Hirata", "Tere Liye", "Pramoedya Ananta Toer", "Habiburrahman El Shirazy"), 0),
        QuestionSMA("Apa rumus kimia dari gas karbondioksida?", listOf("CO", "CO2", "O2", "H2O"), 1),
        QuestionSMA("Manakah planet yang dikenal sebagai 'Planet Merah'?", listOf("Mars", "Jupiter", "Venus", "Saturnus"), 0),
        QuestionSMA("Hukum Newton II menyatakan bahwa gaya berbanding lurus dengan apa?", listOf("Massa", "Kecepatan", "Percepatan", "Energi"), 2),
        QuestionSMA("Jika sebuah benda bermassa 5 kg dipercepat sebesar 2 m/s², berapa gaya yang bekerja pada benda tersebut?", listOf("5 N", "10 N", "15 N", "20 N"), 1),
        QuestionSMA("Di Indonesia, pertempuran Surabaya terjadi pada tahun berapa?", listOf("1942", "1945", "1948", "1950"), 1),
        QuestionSMA("Apakah jenis senyawa yang berfungsi sebagai penyusun utama dinding sel tumbuhan?", listOf("Protein", "Lemak", "Selulosa", "Glukosa"), 2),
        QuestionSMA("Berapa banyak proton dalam atom karbon (C)?", listOf("6", "8", "12", "14"), 0),
        QuestionSMA("Choose the correct word: 'She ---- to the library every week.'", listOf("goes", "go", "went", "gone"), 0),
        QuestionSMA("Siapakah tokoh yang disebut sebagai Bapak Ekonomi Indonesia?", listOf("Mohammad Hatta", "Soekarno", "BJ Habibie", "Sri Sultan HB IX"), 0),
        QuestionSMA("Apa kepanjangan dari APEC?", listOf("Asia-Pacific Economic Cooperation", "Asian-Pacific Economic Coalition", "Asia-Pacific Environmental Council", "Asia-Pacific Energy Corporation"), 0),
        QuestionSMA("Hasil dari integral ∫x dx adalah?", listOf("x²", "x²/2", "x² + C", "x²/2 + C"), 3),
        QuestionSMA("Satuan dari energi dalam sistem SI adalah?", listOf("Newton", "Pascal", "Joule", "Watt"), 2),
        QuestionSMA("Di manakah sidang BPUPKI pertama kali dilaksanakan?", listOf("Jakarta", "Bandung", "Surabaya", "Yogyakarta"), 0),
        QuestionSMA("Apa nama senyawa organik yang menjadi penyusun utama DNA?", listOf("Lipida", "Asam nukleat", "Karbohidrat", "Protein"), 1),
        QuestionSMA("Siapakah pelukis terkenal dari Indonesia yang dikenal dengan gaya ekspresionis?", listOf("Affandi", "Raden Saleh", "Basuki Abdullah", "Sudjojono"), 0),
        QuestionSMA("Choose the correct sentence: 'I ---- my homework before you arrived.'", listOf("complete", "completed", "had completed", "completing"), 2),
        QuestionSMA("Apa nama proses perubahan gas menjadi cair dalam siklus air?", listOf("Evaporasi", "Kondensasi", "Presipitasi", "Infiltrasi"), 1),
        QuestionSMA("Dalam ilmu ekonomi, inflasi adalah?", listOf("Penurunan nilai mata uang", "Kenaikan harga secara umum dan terus-menerus", "Kenaikan produksi barang", "Keseimbangan nilai tukar"), 1),
        QuestionSMA("Berapa panjang gelombang cahaya tampak dalam nanometer (nm)?", listOf("300-400 nm", "400-700 nm", "700-900 nm", "1000-1200 nm"), 1),
        QuestionSMA("Apakah fungsi utama mitokondria dalam sel?", listOf("Sintesis protein", "Respirasi seluler", "Penyimpanan energi", "Sintesis lipid"), 1),
        QuestionSMA("Pada tabel periodik, kelompok 1A dikenal sebagai?", listOf("Halogen", "Gas Mulia", "Alkali", "Alkaline Earth"), 2),
        QuestionSMA("Apa yang dimaksud dengan demokrasi langsung?", listOf("Rakyat memilih wakilnya untuk memerintah", "Rakyat langsung menentukan kebijakan negara", "Pemimpin negara dipilih oleh partai", "Kebijakan diambil oleh lembaga perwakilan rakyat"), 1),
        QuestionSMA("Choose the synonym of 'abundant':", listOf("Plentiful", "Scarce", "Small", "Rare"), 0),
        QuestionSMA("Di antara pilihan berikut, mana yang merupakan senyawa hidrokarbon jenuh?", listOf("CH4", "C2H4", "C2H2", "C6H6"), 0),
        QuestionSMA("Hasil dari turunan fungsi f(x) = 3x² adalah?", listOf("3x", "6x", "9x", "x²"), 1),
        QuestionSMA("Pada sel tumbuhan, organel apakah yang bertanggung jawab untuk fotosintesis?", listOf("Mitokondria", "Ribosom", "Kloroplas", "Nukleus"), 2),
        QuestionSMA("Jika 2 mol gas ideal berada pada tekanan 1 atm dan suhu 273 K, berapakah volumenya? (R = 0.082 L·atm/mol·K)", listOf("22.4 L", "44.8 L", "11.2 L", "33.6 L"), 1)
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
            AnswerButtonSMA(
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
fun AnswerButtonSMA(
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

data class QuestionSMA(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

