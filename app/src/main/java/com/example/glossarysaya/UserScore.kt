package com.example.glossarysaya

data class UserScore(
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val accuracy: Float = 0f
)
