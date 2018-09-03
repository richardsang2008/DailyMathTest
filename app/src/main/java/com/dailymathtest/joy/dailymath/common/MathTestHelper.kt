package com.dailymathtest.joy.dailymath.common

import android.content.Context
import android.content.SharedPreferences
import com.dailymathtest.joy.dailymath.models.Quiz
import com.dailymathtest.joy.dailymath.models.QuizItemReturn
import com.google.gson.GsonBuilder
import java.util.*
import kotlin.math.roundToInt

class MathTestHelper constructor(pref:SharedPreferences) {
    val pref = pref
    fun createQuiz(): Quiz {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        //val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val quizjson = pref.getString("quizjson","DEFAULT")
        if (quizjson.equals("DEFAULT",ignoreCase = true)) {
            val str = """
            {
              "id": 1,
              "score": 0,
              "quizDate": "2018-09-02T16:57:24.7808525-07:00",
              "student": {
                "firstName": "Joy",
                "midName": "",
                "lastName": "Sang",
                "studentId": "HXbJ6ocCJTnM",
                "email": "richardsang2009@gmail.com",
                "enrollmentDate": "2018-08-30T19:42:13.003691",
                "id": 1
              },
              "quizItems": [
                {
                  "leftOperand": 1419,
                  "rightOperand": 904,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 1
                },
                {
                  "leftOperand": 6448,
                  "rightOperand": 1704,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 2
                },
                {
                  "leftOperand": 7014,
                  "rightOperand": 2627,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 3
                },
                {
                  "leftOperand": 8327,
                  "rightOperand": 3279,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 4
                },
                {
                  "leftOperand": 6908,
                  "rightOperand": 3884,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 5
                },
                {
                  "leftOperand": 8114,
                  "rightOperand": 184,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 6
                },
                {
                  "leftOperand": 5253,
                  "rightOperand": 2812,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 7
                },
                {
                  "leftOperand": 2207,
                  "rightOperand": 1771,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 8
                },
                {
                  "leftOperand": 5788,
                  "rightOperand": 3712,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 9
                },
                {
                  "leftOperand": 2074,
                  "rightOperand": 1232,
                  "operator": 1,
                  "answer": 0,
                  "quizId": 1,
                  "id": 10
                }
              ]
            }

        """.trimIndent()
            val fakeQuiz =gson.fromJson(str, Quiz::class.java)
            val editor =pref.edit()
            editor.putString("quizjson", gson.toJson(fakeQuiz))
            editor.commit()
            return fakeQuiz
        } else {
            val fakeQuiz = gson.fromJson(quizjson, Quiz::class.java)
            return fakeQuiz
        }
    }
    fun getQuizItem(id: Int, pref: SharedPreferences): QuizItemReturn {
        //get the quiz from the sharedpreferences
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val jsonstr = pref.getString("quizjson","DEFAULT")
        val fakeQuiz =gson.fromJson(jsonstr,Quiz::class.java)
        val quizItem =fakeQuiz.quizItems[id]
        var opt =" "
        val answers : MutableList<Int> = arrayListOf()
        val random = Random()
        if (quizItem.operator == com.dailymathtest.joy.dailymath.models.Operator.Addition) {
            opt = "+"
            val numb0 = quizItem.leftOperand.roundToInt() + quizItem.rightOperand.roundToInt()
            val numb1 =quizItem.leftOperand.roundToInt() + quizItem.rightOperand.roundToInt() +(0..100).shuffled().last()
            val numb2 =quizItem.leftOperand.roundToInt() + quizItem.rightOperand.roundToInt() +(0..100).shuffled().last()
            val numb3 = quizItem.leftOperand.roundToInt() + quizItem.rightOperand.roundToInt() +(0..100).shuffled().last()
            answers.add(numb0)
            answers.add(numb1)
            answers.add(numb2)
            answers.add(numb3)

        } else if (quizItem.operator == com.dailymathtest.joy.dailymath.models.Operator.Subtraction) {
            opt ="-"
            val numb0 = quizItem.leftOperand.roundToInt() - quizItem.rightOperand.roundToInt()
            val numb1 =quizItem.leftOperand.roundToInt() - quizItem.rightOperand.roundToInt() +(0..100).shuffled().last()
            val numb2 =quizItem.leftOperand.roundToInt() - quizItem.rightOperand.roundToInt() +(0..100).shuffled().last()
            val numb3 = quizItem.leftOperand.roundToInt() - quizItem.rightOperand.roundToInt() +(0..100).shuffled().last()
            answers.add(numb0)
            answers.add(numb1)
            answers.add(numb2)
            answers.add(numb3)
        }
        val quizItemStr = quizItem.leftOperand.roundToInt().toString() + " "+opt+ " "+ quizItem.rightOperand.roundToInt().toString() + " = "
        val ret = QuizItemReturn(quizItemStr,answers)
        return ret
    }
}
