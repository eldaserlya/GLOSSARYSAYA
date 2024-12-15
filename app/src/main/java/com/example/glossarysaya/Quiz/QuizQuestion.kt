package com.example.glossarysaya.Quiz

data class QuizQuestion(
    val id: String = "",
    val question: String = "",
    val options: List<String> = listOf(),
    val correctAnswer: String = "",
    val level: Int = 1
)