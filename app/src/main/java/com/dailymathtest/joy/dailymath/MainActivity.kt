@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.dailymathtest.joy.dailymath
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.dailymathtest.joy.dailymath.common.MathTestHelper
import com.google.android.gms.common.AccountPicker
import com.dailymathtest.joy.dailymath.controllers.UnsafeOkHttpClient2
import com.dailymathtest.joy.dailymath.models.Quiz
import com.dailymathtest.joy.dailymath.models.Student
import com.google.android.gms.security.ProviderInstaller
import com.google.gson.GsonBuilder
import okhttp3.*

import java.io.IOException


class MainActivity : AppCompatActivity() {

    fun fetchQuizFromServerAndStore(studentId: String) {
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        val url = MathTestHelper.ADDRESS+"/Quiz"
        val jsonstr = "{\"studentId\":\"${studentId}\"}"
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val formBody = RequestBody.create(JSON, jsonstr)
        val request = Request.Builder().url(url).post(formBody).build()
        val client = UnsafeOkHttpClient2.getOkHttpClient()
        //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
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
                goMainActivity(pref)
            }
        })
    }
    fun fetchStudentAndQuizFromServerAndStore(username: String) {
        val url = MathTestHelper.ADDRESS+"/Student/byEmail?email="+username
        val request = Request.Builder().url(url).build()
        //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
        val client = UnsafeOkHttpClient2.getOkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("********************************Reach fetchStudentFromServerAndStore")
                val body = response.body()?.string()
                val student = Student.fromJsonStr(body)
                val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
                val editor = pref.edit()
                editor.putString("studentId", student.studentId)
                editor.putString("firstName", student.firstName)
                editor.putString("lastName", student.lastName)
                editor.apply()
                println("************studentid "+student.studentId)
                val url = MathTestHelper.ADDRESS+"/Quiz"
                val jsonstr = "{\"studentId\":\"${student.studentId}\"}"
                val JSON = MediaType.parse("application/json; charset=utf-8")
                val formBody = RequestBody.create(JSON, jsonstr)
                val request = Request.Builder().url(url).post(formBody).build()
                val client = UnsafeOkHttpClient2.getOkHttpClient()
                //val client = UnsafeOkHttpClient2.getUnsafeOkHttpClient()
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
                        goMainActivity(pref)
                    }
                })
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("Failure to excute method")
            }
        })
    }
    @SuppressLint("SetTextI18n")
    fun goMainActivity(pref: SharedPreferences) {

        Handler(Looper.getMainLooper()).post {
            val t = findViewById<TextView>(R.id.helloTextView)
            t.text = "Welcome " + pref.getString("firstName","DEFAULT") + " "+ pref.getString("lastName","DEFAULT")
        }
    }
    fun clearSharedPreference(view:View) {
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

    fun addButtonClick(view: View) {
        try {
            val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE)
            val editor = pref.edit()
            val quizjson = pref.getString("quizjson","DEFAULT")
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val newQuiz = gson.fromJson(quizjson, Quiz::class.java)
            val quizItemId =newQuiz.quizItems!![0].id
            editor.putInt("currentpageNumber", quizItemId)
            editor.apply()
            val intent = Intent(this, QuizItemContainerActivity::class.java).apply {
                putExtra("ItemNumber", quizItemId)
                val helper = MathTestHelper(pref)
                val quizItemReturn = helper.getQuizItem(quizItemId)
                putExtra("QuizItem", quizItemReturn.quizItem)
                putExtra("choiceA", quizItemReturn.choices[0])
                putExtra("choiceB", quizItemReturn.choices[1])
                putExtra("choiceC", quizItemReturn.choices[2])
                putExtra("choiceD", quizItemReturn.choices[3])
            }
            startActivity(intent)
        } catch (e:Exception) {
            println(e.stackTrace)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        ProviderInstaller.installIfNeeded(applicationContext)
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val email = pref.getString("email","DEFAULT")
        val studentid = pref.getString("studentId","DEFAULT")
        if (studentid.equals("DEFAULT",ignoreCase = true)){
            if (email.equals("DEFAULT",ignoreCase =true)) {
                //there is no studentId stored
                //check google accountmanager
                val accountManager = AccountManager.get(applicationContext)
                val accountlist = accountManager.getAccountsByType("com.google")
                for (account in accountlist) {
                    println( "account: " + account.name + " : " + account.type)
                }
                if (accountlist.isEmpty() || accountlist.size>1) {
                    val intent = AccountPicker.newChooseAccountIntent(null, null, arrayOf("com.google"), false, null, null, null, null)
                    startActivityForResult(intent,1)
                } else {
                    //now have the email from user
                    fetchStudentAndQuizFromServerAndStore(accountlist[0].name)
                }
            } else {
                println("I have the email now " +email)
                fetchStudentAndQuizFromServerAndStore(email)
            }
        } else {
            try {
                val t = findViewById<TextView>(R.id.helloTextView)
                MathTestHelper(pref).fetchQuizFromServerAndStore(studentid,t)
            } catch (e:Exception) {
                println(e.stackTrace)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1 && resultCode === Activity.RESULT_OK) {
            val email = data!!.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            fetchStudentAndQuizFromServerAndStore(email)
            val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
            val editor = pref.edit()
            editor.putString("email", email)
            editor.apply()
        }
    }
}



