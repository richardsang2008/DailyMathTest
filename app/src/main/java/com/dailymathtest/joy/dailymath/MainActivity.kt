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
import com.dailymathtest.joy.dailymath.controllers.UnsafeOkHttpClient
import com.dailymathtest.joy.dailymath.models.Quiz

import com.dailymathtest.joy.dailymath.models.Student
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    fun fetchStudentFromServerAndStore(username: String) {
        val url = "https://10.0.3.2:5001/api/Student/byEmail?email="+username
        val request = Request.Builder().url(url).build()
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("********************************Reach fetchStudentFromServerAndStore")
                val body = response.body()?.string()
                JSONObject(body)
                val student = Student.fromJsonStr(body)
                val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
                val editor = pref.edit()
                editor.putString("studentId", student.studentId)
                editor.putString("firstName", student.firstName)
                editor.putString("lastName", student.lastName)
                editor.apply()
                println("************studentid "+student.studentId)
                goMainActivity(pref)
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("Failure to excute method")
            }
        })
    }
    @SuppressLint("SetTextI18n")
    fun goMainActivity(pref: SharedPreferences) {
        val helper = MathTestHelper(pref)
        helper.createQuiz()
        Handler(Looper.getMainLooper()).post {
            val t = findViewById<TextView>(R.id.helloTextView)
            t.text = "Wellcome " + pref.getString("firstName","DEFAULT") + " "+ pref.getString("lastName","DEFAULT")
        }
    }
    fun clearSharedPreference() {
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        editor.clear()
        editor.apply()

    }

    fun addButtonClick(view: View) {
        try {
            val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt("currentpageNumber", 1)
            editor.apply()
            val intent = Intent(this, QuizItemContainerActivity::class.java).apply {
                putExtra("ItemNumber", 1)
                //get the quiz from the sharedpreferences
                //val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
                //val jsonstr = pref.getString("quizjson", "DEFAULT")
                //gson.fromJson(jsonstr, Quiz::class.java)
                val helper = MathTestHelper(pref)
                val quizItemReturn = helper.getQuizItem(0, pref)
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
                if (accountlist.isEmpty()) {
                    val intent = AccountPicker.newChooseAccountIntent(null, null, arrayOf("com.google"), false, null, null, null, null)
                    startActivityForResult(intent,1)
                } else {
                    //now have the email from user
                    fetchStudentFromServerAndStore(email)
                }
            } else {
                println("I have the email now " +email)
                fetchStudentFromServerAndStore(email)
            }
        } else {
            try {
                goMainActivity(pref)
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
            println("==============="+email)
            fetchStudentFromServerAndStore(email)
            val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
            val editor = pref.edit()
            editor.putString("email", email)
            editor.apply()
        }
    }
}



