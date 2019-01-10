package com.dailymathtest.joy.dailymath.models;

import java.util.List;

public class QuizItemReturn {
    private String quizItem;
    private List<Integer> choices;
    public QuizItemReturn(String quizItem, List<Integer> choices) {
        this.quizItem = quizItem;
        this.choices = choices;
    }

    public String getQuizItem() {
        return quizItem;
    }

    public List<Integer> getChoices() {
        return choices;
    }
}
