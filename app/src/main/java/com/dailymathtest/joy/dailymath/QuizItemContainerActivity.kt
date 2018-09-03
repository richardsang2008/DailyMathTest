package com.dailymathtest.joy.dailymath

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.dailymathtest.joy.dailymath.common.MathTestHelper

class QuizItemContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_item_container)
        //Get the intent that is passed in
        val ItemNumber = intent.getIntExtra("ItemNumber",0).toString()
        findViewById<TextView>(R.id.ItemNumbertextView).apply {
            text = ItemNumber
        }
        val QuizItem = intent.getStringExtra("QuizItem")
        findViewById<TextView>(R.id.quizeItemtextView).apply{
            text = QuizItem
        }
        val choiceA = intent.getIntExtra("choiceA",0).toString()
        findViewById<Button>(R.id.answer_A_Button).apply{
            text = choiceA
        }
        val choiceB = intent.getIntExtra("choiceB",0).toString()
        findViewById<Button>(R.id.answer_B_Button).apply{
            text = choiceB
        }
        val choiceC = intent.getIntExtra("choiceC",0).toString()
        findViewById<Button>(R.id.answer_C_Button).apply{
            text = choiceC
        }
        val choiceD = intent.getIntExtra("choiceD",0).toString()
        findViewById<Button>(R.id.answer_D_Button).apply{
            text = choiceD
        }

//        findViewById<Button>(R.id.scorebutton).apply{
//            this.isClickable = Integer.parseInt(ItemNumber) ==10
//        }
//        findViewById<ImageButton>(R.id.previousImageButton).apply{
//            this.isClickable = Integer.parseInt(ItemNumber) != 1
//        }
//        findViewById<ImageButton>(R.id.nextImageButton).apply{
//            this.isClickable = Integer.parseInt(ItemNumber) != 10
//        }
    }
    fun nextBtnClick(view: View) {
        //get the current
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        var ItemNumber = pref.getInt("currentpageNumber",0)
        if ((ItemNumber +1) >= 10){
            //no change
        } else {
            ItemNumber += 1
            editor.putInt("currentpageNumber", ItemNumber)
            editor.commit()
            val helper = MathTestHelper(pref)
            val quizItemReturn = helper.getQuizItem(ItemNumber - 1, pref)
            val intent = Intent(this, QuizItemContainerActivity::class.java).apply {
                putExtra("ItemNumber", ItemNumber)
                putExtra("QuizItem", quizItemReturn.quizItem)
                putExtra("choiceA", quizItemReturn.choices[0])
                putExtra("choiceB", quizItemReturn.choices[1])
                putExtra("choiceC", quizItemReturn.choices[2])
                putExtra("choiceD", quizItemReturn.choices[3])
            }
            val quizItem = helper.getQuizItem(ItemNumber, pref)
            findViewById<TextView>(R.id.ItemNumbertextView).apply {
                text = ItemNumber.toString()
            }
            findViewById<TextView>(R.id.quizeItemtextView).apply {
                text = quizItem.quizItem
            }
            findViewById<Button>(R.id.answer_A_Button).apply {
                text = quizItem.choices[0].toString()
            }
            findViewById<Button>(R.id.answer_B_Button).apply {
                text = quizItem.choices[1].toString()
            }
            findViewById<Button>(R.id.answer_C_Button).apply {
                text = quizItem.choices[2].toString()
            }
            findViewById<Button>(R.id.answer_D_Button).apply {
                text = quizItem.choices[3].toString()
            }
            /*findViewById<Button>(R.id.previousImageButton).apply{
                this.isClickable = Integer.parseInt(ItemNumber.toString()) !=1
            }*/
        }
    }
    fun previousBtnClick(view: View) {
        //get the current
        val pref = applicationContext.getSharedPreferences("appData", Context.MODE_PRIVATE) // for private mode
        val editor = pref.edit()
        var ItemNumber = pref.getInt("currentpageNumber",0)
        if ((ItemNumber -1) <=1){
            //no change
        } else {
            ItemNumber -= 1
            editor.apply()
            editor.putInt("ItemNumber", ItemNumber)
            val helper = MathTestHelper(pref)
            val quizItemReturn = helper.getQuizItem(ItemNumber - 1, pref)
            val intent = Intent(this, QuizItemContainerActivity::class.java).apply {
                putExtra("ItemNumber", ItemNumber)
                putExtra("QuizItem", quizItemReturn.quizItem)
                putExtra("choiceA", quizItemReturn.choices[0])
                putExtra("choiceB", quizItemReturn.choices[1])
                putExtra("choiceC", quizItemReturn.choices[2])
                putExtra("choiceD", quizItemReturn.choices[3])
            }
            val quizItem = helper.getQuizItem(ItemNumber, pref)
            findViewById<TextView>(R.id.ItemNumbertextView).apply {
                text = ItemNumber.toString()
            }
            findViewById<TextView>(R.id.quizeItemtextView).apply {
                text = quizItem.quizItem
            }
            findViewById<Button>(R.id.answer_A_Button).apply {
                text = quizItem.choices[0].toString()
            }
            findViewById<Button>(R.id.answer_B_Button).apply {
                text = quizItem.choices[1].toString()
            }
            findViewById<Button>(R.id.answer_C_Button).apply {
                text = quizItem.choices[2].toString()
            }
            findViewById<Button>(R.id.answer_D_Button).apply {
                text = quizItem.choices[3].toString()
            }
        }
    }
}
