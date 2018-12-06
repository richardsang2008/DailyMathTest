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
    public String toString() {
        String op ="";
        String correctAnswer ="";
        if (operator == Operator.Addition) {
            op ="+";
            correctAnswer = Integer.toString((int)Math.round(leftOperand+rightOperand));
        } else if (operator == Operator.Subtraction){
            op ="-";
            correctAnswer = Integer.toString((int)Math.round(leftOperand-rightOperand));
        } else if (operator == Operator.Multiplication) {
            op ="x";
            correctAnswer = Integer.toString((int)Math.round(leftOperand*rightOperand));
        } else if (operator == Operator.Division) {
            op ="/";
            correctAnswer = Double.toString(((double)Math.round((leftOperand/rightOperand)*100))/100);
        }
        String mark ="";
        if (correctAnswer.equals(Integer.toString((int)Math.round(answer)))) {
            mark ="";
        } else {
            mark ="Wrong";
        }
        String msg = "   "+ Integer.toString((int)Math.round(leftOperand)) +"  "+op+"  "+Integer.toString((int)Math.round(rightOperand)) +"  =  "+Integer.toString((int)Math.round(answer)) + " "+mark;
        return msg;
    }
}
