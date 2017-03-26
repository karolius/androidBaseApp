package com.example.user.proj_1;

/**
 * Created by user on 2017-03-23.
 */

public class GradeModel {
    private int value;
    private String name;

    public GradeModel(String name) {
        this.name = name;
        value = 5;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
