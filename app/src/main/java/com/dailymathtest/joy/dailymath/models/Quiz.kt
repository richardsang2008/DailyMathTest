package com.dailymathtest.joy.dailymath.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.util.Date

class Quiz {
    var id: Int = 0
        private set
    var score: Double = 0.toDouble()
        set
    var quizDate: Date? = null
        private set
    var student: Student? = null
        private set
    var quizItems: List<QuizItem>? = null
        private set

    companion object {
        fun fromJsonStr(jsonStr: String): Quiz? {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val quiz = Quiz()
            try {
                val q = gson.fromJson(jsonStr, Quiz::class.java)
                quiz.quizItems = q.quizItems
                quiz.student = q.student
                quiz.score = q.score
                quiz.id = q.id
                quiz.quizDate = q.quizDate
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

            return quiz
        }
    }
}
