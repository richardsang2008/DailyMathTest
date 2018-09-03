package com.dailymathtest.joy.dailymath.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Student {
    private String firstName;
    private String midName;
    private String lastName;
    private String studentId;
    private String email;
    private Date enrollmentDate;

    public int getId() {
        return id;
    }

    private int id;

    public String getFirstName() {
        return firstName;
    }

    public String getMidName() {
        return midName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getEmail() {
        return email;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public static Student fromJsonStr(String jsonStr) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Student student = new Student();
        try {
            Student s = gson.fromJson(jsonStr, Student.class);
            student.firstName = s.firstName;
            student.lastName = s.lastName;
            student.midName = s.midName;
            student.studentId = s.studentId;
            student.enrollmentDate = s.enrollmentDate;
            student.email = s.email;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return student;

    }
    public static Student fromJson(JSONObject jsonObject) {
        Student student = new Student();
        try{
            student.id = jsonObject.getInt("id");
            student.firstName = jsonObject.getString("firstName");
            student.lastName = jsonObject.getString("lastName");
            student.midName = jsonObject.getString("midName");
            student.email = jsonObject.getString("email");
            String dateStr = jsonObject.getString("enrollmentDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            student.enrollmentDate = sdf.parse(dateStr);
            student.studentId = jsonObject.getString("studentId");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return student;
    }
}
