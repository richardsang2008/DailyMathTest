package com.dailymathtest.joy.dailymath.models;

import com.google.gson.annotations.SerializedName;

public  enum Operator{
    @SerializedName("1") Addition,
    @SerializedName("2")Subtraction,
    @SerializedName("3")Multiplication,
    @SerializedName("4")Division,
    UNKNOWN

}