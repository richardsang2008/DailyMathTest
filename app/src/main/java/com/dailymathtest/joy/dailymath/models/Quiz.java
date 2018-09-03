package com.dailymathtest.joy.dailymath.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

public class Quiz {
    private int id;
    private double  score;
    private Date quizDate;
    private Student student;
    private List<QuizItem> quizItems;

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public Date getQuizDate() {
        return quizDate;
    }

    public Student getStudent() {
        return student;
    }

    public List<QuizItem> getQuizItems() {
        return quizItems;
    }
    public static Quiz fromJsonStr(String jsonStr) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Quiz quiz = new Quiz();
        try {
            Quiz q = gson.fromJson(jsonStr, Quiz.class);
            quiz.quizItems = q.quizItems;
            quiz.student = q.student;
            quiz.score = q.score;
            quiz.id = q.id;
            quiz.quizDate = q.quizDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return quiz;
    }
}
