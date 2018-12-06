package com.dailymathtest.joy.dailymath

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.dailymathtest.joy.dailymath.common.MathTestHelper
import com.dailymathtest.joy.dailymath.controllers.UnsafeOkHttpClient2
import com.dailymathtest.joy.dailymath.models.Quiz
import com.dailymathtest.joy.dailymath.models.Student
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class QuizItemContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_item_container)
        //Get the intent that is passed in
        val ItemNumber = intent.getIntExtra("ItemNumber", 0).toString()
        findViewById<TextView>(R.id.ItemNumbertextView).apply {
            text = ItemNumber
        }
        val QuizItem = intent.getStringExtra("QuizItem")
        findViewById<TextView>(R.id.quizeItemtextView).apply {
            text = QuizItem
        }
        val choiceA = intent.getIntExtra("choiceA", 0).toString()
        findViewById<Button>(R.id.answer_A_Button).apply {
            text = choiceA
            setOnClickListener(HandleAnswerBtnClick())
        }
        val choiceB = intent.getIntExtra("choiceB", 0).toString()
        findViewById<Button>(R.id.answer_B_Button).apply {
            text = choiceB
            setOnClickListener(HandleAnswerBtnClick())
        }
        val choiceC = intent.getIntExtra("choiceC", 0).toString()
        findViewById<Button>(R.id.answer_C_Button).apply {
            text = choiceC
            setOnClickListener(HandleAnswerBtnClick())
        }
        val choiceD = intent.getIntExtra("choiceD", 0).toString()
        findViewById<Button>(R.id.answer_D_Button).apply {
            text = choiceD
            setOnClickListener(HandleAnswerBtnClick())
        }
    }

    private inner class HandleAnswerBtnClick : View.OnClickListener {
        override fun onClick(view: View) {
            val btn = view as Button
            val value = btn.text.toString()
            val answer = value.toDouble()
            val url = MathTestHelper.ADDRESS + "/Quiz/quizitems"
            val quizItemId = intent.getIntExtra("ItemNumber", 0)
            val jsonstr = "{\"quizItemId\":$quizItemId, \"answer\":$answer}"
            val JSON = MediaType.parse("application/json; charset=utf-8")
            val formBody = RequestBody.create(JSON, jsonstr)
            val request = Request.Builder().url(url).patch(formBody).build()
            //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
            val client = UnsafeOkHttpClient2.getOkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    println("Failure to excute method")
                }

                override fun onResponse(call: Call, response: Response) {
                    println("********************************Reach fetchStudentFromServerAndStore")
                    response.body()?.string()
                    if (response.code() == 200) {
                        //go to the next quiz item
                        goToNextQuizItem(view)
                    }
                }
            })
        }
    }

    fun goToNextQuizItem(view: View) {
        //get the current
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        var ItemNumber = pref.getInt("currentpageNumber", 0)
        if ((ItemNumber ) % 10==0) {
            ItemNumber += 1
            editor.putInt("currentpageNumber", ItemNumber)
            editor.apply()
            //score the quiz
            val quizjson = pref.getString("quizjson","DEFAULT")
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val newQuiz = gson.fromJson(quizjson, Quiz::class.java)
            val url = MathTestHelper.ADDRESS + "/Quiz/"+newQuiz.id.toString()+"/score"
            val request = Request.Builder().url(url).build()
            val client = UnsafeOkHttpClient2.getOkHttpClient()
            //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    println("Failure to excute method")
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body()?.string()
                    val score = (body!!.toDouble()*100).toInt()
                    newQuiz.score = body!!.toDouble()
                    val editor = pref.edit()
                    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                    editor.putString("quizjson",gson.toJson(newQuiz))
                    //get the quiz now
                    val url = MathTestHelper.ADDRESS+"/Quiz/${newQuiz.id.toString()}"
                    val request = Request.Builder().url(url).build()
                    //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
                    val client = UnsafeOkHttpClient2.getOkHttpClient()
                    client.newCall(request).enqueue(object: Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            println("Failure to excute method")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val body = response.body()?.string()
                            val quiz = gson.fromJson(body, Quiz::class.java)
                            //go to the next quiz

                            pref.getString("","DEFAULT")
                            val studentid = pref.getString("studentId","DEFAULT")
                            MathTestHelper(pref).fetchQuizFromServerAndStore(studentid,null)

                            //go to next quiz
                            //val nextNewQuiz = fetchQuizFromServerAndStore
                            Handler(Looper.getMainLooper()).post {
                                val builder =AlertDialog.Builder(this@QuizItemContainerActivity)
                                builder.setTitle("Score")
                                var message = "Your today's quiz score is\n$score\n"
                                quiz.quizItems!!.forEach { quizItem->
                                    message = message + quizItem.toString() +"\n"
                                }
                                builder.setMessage(message)
                                builder.setPositiveButton("OK"){
                                    dialog,whichButton ->
                                    dialog.dismiss()
                                    finish()
                                }

                                val dialog =builder.create()
                                dialog.show()
                            }
                        }
                    })
                }
            })
        } else {
            ItemNumber += 1
            editor.putInt("currentpageNumber", ItemNumber)
            editor.apply()
            val helper = MathTestHelper(pref)
            val quizItemReturn = helper.getQuizItem(ItemNumber )
            intent.putExtra("ItemNumber", ItemNumber)
            intent.putExtra("QuizItem", quizItemReturn.quizItem)
            intent.putExtra("choiceA", quizItemReturn.choices[0])
            intent.putExtra("choiceB", quizItemReturn.choices[1])
            intent.putExtra("choiceC", quizItemReturn.choices[2])
            intent.putExtra("choiceD", quizItemReturn.choices[3])

            Handler(Looper.getMainLooper()).post {
                findViewById<TextView>(R.id.ItemNumbertextView).text = ItemNumber.toString()
                findViewById<TextView>(R.id.quizeItemtextView).text = quizItemReturn.quizItem
                findViewById<Button>(R.id.answer_A_Button).text = quizItemReturn.choices[0].toString()
                findViewById<Button>(R.id.answer_B_Button).text = quizItemReturn.choices[1].toString()
                findViewById<Button>(R.id.answer_C_Button).text = quizItemReturn.choices[2].toString()
                findViewById<Button>(R.id.answer_D_Button).text = quizItemReturn.choices[3].toString()
            }
        }
    }
    fun nextBtnClick(view: View) {
        goToNextQuizItem(view)
    }
    fun scoreBtnClick(view: View) {

        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val quizjson = pref.getString("quizjson","DEFAULT")
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val newQuiz = gson.fromJson(quizjson, Quiz::class.java)


        val url = MathTestHelper.ADDRESS + "/Quiz/"+newQuiz.id.toString()+"/score"
        val request = Request.Builder().url(url).build()
        //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
        val client = UnsafeOkHttpClient2.getOkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("Failure to excute method")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val score = body!!.toDouble()*100
                Handler(Looper.getMainLooper()).post {
                    val builder =AlertDialog.Builder(this@QuizItemContainerActivity)
                    builder.setTitle("Score")
                    builder.setMessage("Your today's quiz score is "+ score.toString());
                }
            }
        })
    }
    fun previousBtnClick(view: View) {
        //get the current
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        var ItemNumber = pref.getInt("currentpageNumber", 0)
        if ((ItemNumber - 1)%10 < 1) {
            //no change
        } else {
            ItemNumber -= 1
            editor.putInt("currentpageNumber", ItemNumber)
            editor.apply()
            val helper = MathTestHelper(pref)
            val quizItemReturn = helper.getQuizItem(ItemNumber - 1)

            intent.putExtra("ItemNumber", ItemNumber)
            intent.putExtra("QuizItem", quizItemReturn.quizItem)
            intent.putExtra("choiceA", quizItemReturn.choices[0])
            intent.putExtra("choiceB", quizItemReturn.choices[1])
            intent.putExtra("choiceC", quizItemReturn.choices[2])
            intent.putExtra("choiceD", quizItemReturn.choices[3])

            //val quizItem = helper.getQuizItem(ItemNumber, pref)
            findViewById<TextView>(R.id.ItemNumbertextView).text = ItemNumber.toString()
            findViewById<TextView>(R.id.quizeItemtextView).text = quizItemReturn.quizItem
            findViewById<Button>(R.id.answer_A_Button).text = quizItemReturn.choices[0].toString()
            findViewById<Button>(R.id.answer_B_Button).text = quizItemReturn.choices[1].toString()
            findViewById<Button>(R.id.answer_C_Button).text = quizItemReturn.choices[2].toString()
            findViewById<Button>(R.id.answer_D_Button).text = quizItemReturn.choices[3].toString()
        }
    }
}
