package com.example.user.proj_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


public class GradesFormActivity extends AppCompatActivity {
    ArrayList<GradeModel> gradesList;
    ListView gradesListView;
    int gradesQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_form);

        // odczytanie liczby ocen z danych otrzmyanych z UserFormActivity
        gradesQuantity = getIntent().getIntExtra("gradesQuantity", gradesQuantity);
        generateGradesList();
    }


    /**
     * Inicjalizacja listy elementów GradeModel
     * Utworzenie obiektu adaptera i powiazanie go z lista
     * Ustawienie adaptera dla kontrolki ListView widoku
     * Zapisanie listy nowymi obiektami
     */
    private  void generateGradesList(){
        gradesList = new ArrayList<GradeModel>();
        GradesListCustomAdapter adapter = new GradesListCustomAdapter(
                GradesFormActivity.this, gradesList);
        gradesListView=(ListView) findViewById(R.id.gradesListView);
        gradesListView.setAdapter((ListAdapter) adapter);
        for(int i=0; i<gradesQuantity; i++)
            gradesList.add(new GradeModel("Grade "+(i+1)));
    }


    // Funkcja liczaca srednia ocen
    private double getAverage(){
        double avg = 0;
        for(GradeModel grade: gradesList)
            avg += grade.getValue();
        avg /= gradesList.size();
        return round(avg, 2);
    }


    /**
     * Funkcja zaokraglajaca wartosc typu double
     * @param value przekazywana wartosc
     * @param places ilosc miejsc po przecinku
     * @return
     */
    public double round(double value, int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    // Funkcja tworzy obiekt do aktywnosci UserFromAcriviy dolaczajac wartosc sredniej ocen
    public void onConfirmBtnClick(View v) {
        Intent giveResult = new Intent(GradesFormActivity.this, UserFormActivity.class);

        setResult(RESULT_OK, giveResult);
        giveResult.putExtra("average",getAverage());
        finish(); // zakoncz aktywnosc, wroc do aktywnosci glownej
    }
}