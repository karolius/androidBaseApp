package com.example.user.proj_1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


class ModelOceny {
    private String name;
    private int grade;


    //konstruktor
    public ModelOceny(String name) {
        this.name = name;
    }


    public void setGrade(int grade) {
        this.grade = grade;
    }

    //metody dostępowe
    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }
}


public class GradesFormActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_form_list);


        Bundle incomingData = getIntent().getExtras();
        Integer count = incomingData.getInt("Count");
        List<ModelOceny> gradesList = new ArrayList<ModelOceny>();
        for(int i=0; i<count; i++){
            gradesList.add(new ModelOceny("ocena "+(i+1)));
        }

        //łączenie danych z listą
        InteraktywnyAdapterTablicy adapter=new InteraktywnyAdapterTablicy(this, gradesList);
        ListView listaOcen=(ListView) findViewById(R.id.mListView);
        listaOcen.setAdapter(adapter);
    }
}