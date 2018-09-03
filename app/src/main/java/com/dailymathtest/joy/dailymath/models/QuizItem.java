package com.dailymathtest.joy.dailymath.models;


public class QuizItem {
    private int id;
    private double leftOperand;
    private double rightOperand;
    private Operator operator;
    private int quizId;
    private double answer;

    public int getId() {
        return id;
    }

    public double getLeftOperand() {
        return leftOperand;
    }

    public double getRightOperand() {
        return rightOperand;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getQuizId() {
        return quizId;
    }

    public double getAnswer() {
        return answer;
    }
}
