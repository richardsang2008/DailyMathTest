package com.dailymathtest.joy.dailymath.common
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.dailymathtest.joy.dailymath.R
import com.dailymathtest.joy.dailymath.controllers.UnsafeOkHttpClient2
import com.dailymathtest.joy.dailymath.models.Quiz
import com.dailymathtest.joy.dailymath.models.QuizItem
import com.dailymathtest.joy.dailymath.models.QuizItemReturn
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

import java.util.Collections.shuffle
import kotlin.math.roundToInt

class MathTestHelper constructor(pref:SharedPreferences) {
    companion object{
        //val ADDRESS = "http://192.168.29.188/api"
        val ADDRESS = "http://10.0.3.2:3000/api"
    }
    private val pref = pref
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
                  "answer": 0,"https://localh
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
            editor.apply()
            return fakeQuiz
        } else {
            val fakeQuiz = gson.fromJson(quizjson, Quiz::class.java)
            return fakeQuiz
        }
    }
    fun getQuizItemByQuizItemId (quizItemId:Int, quiz:Quiz ) : QuizItem? {
        quiz.quizItems!!.forEach { item->
            if (quizItemId == item.id) {
                return item
            }
        }
        return null
    }
    fun getQuizItem(id: Int): QuizItemReturn {
        //get the quiz from the sharedpreferences
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val jsonstr = pref.getString("quizjson","DEFAULT")
        val fakeQuiz =gson.fromJson(jsonstr,Quiz::class.java)
        val quizItem = getQuizItemByQuizItemId(id,fakeQuiz)
        var opt =" "
        val answers : MutableList<Int> = arrayListOf()
        if (quizItem!!.operator == com.dailymathtest.joy.dailymath.models.Operator.Addition) {
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
        shuffle(answers)
        val quizItemStr = quizItem.leftOperand.roundToInt().toString() + " "+opt+ " "+ quizItem.rightOperand.roundToInt().toString() + " = "
        val ret = QuizItemReturn(quizItemStr,answers)
        return ret
    }
    fun fetchQuizFromServerAndStore(studentId: String, tv:TextView?) {
        //val editor = pref.edit()
        val url = MathTestHelper.ADDRESS+"/Quiz"
        val jsonstr = "{\"studentId\":\"${studentId}\"}"
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val formBody = RequestBody.create(JSON, jsonstr)
        val request = Request.Builder().url(url).post(formBody).build()
        val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
        var newQuiz: Quiz
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("Failure to excute method")
            }
            override fun onResponse(call: Call, response: Response) {
                println("********************************Reach fetchStudentFromServerAndStore")
                val body = response.body()?.string()
                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                newQuiz = gson.fromJson(body, Quiz::class.java)
                val editor = pref.edit()
                editor.putString("quizjson", gson.toJson(newQuiz))
                editor.apply()
                if (tv !=null) {
                    Handler(Looper.getMainLooper()).post {
                        tv.text = "Welcome " + pref.getString("firstName","DEFAULT") + " "+ pref.getString("lastName","DEFAULT")
                    }
                }

            }
        })
    }
}
