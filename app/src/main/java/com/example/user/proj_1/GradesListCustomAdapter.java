package com.example.user.proj_1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;


public class GradesListCustomAdapter extends ArrayAdapter<GradeModel> {
    //przechowujemy referencję do listy ocen
    private List<GradeModel> gradesList;
    private Activity context;


    public GradesListCustomAdapter(Activity context, List<GradeModel> gradesList){
        super(context, R.layout.grade_row, gradesList);
        this.context = context;
        this.gradesList = gradesList;
    }


    //tworzenie nowego wiersza
    @Override
    public View getView(final int gradeRowNumber, View viewToRecycle, ViewGroup parent){
        LayoutInflater contextLayout = context.getLayoutInflater();
        //utworzenie layout na podstawie pliku XML
        View gradeRowView = contextLayout.inflate(R.layout.grade_row, parent, false);
        final GradeModel gradeRow = gradesList.get(gradeRowNumber);
        //tworzenie nowego wiersza
        if (viewToRecycle == null){
            //utworzenie layout na podstawie pliku XML
            //wybranie konkretnego przycisku radiowego musi zmieniać dane w modelu
            RadioGroup radioGradesGroup = (RadioGroup) gradeRowView.findViewById(R.id.gradesGroup);
            radioGradesGroup.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener(){
                        @Override // referencja do: 1) grupy przyc , 2) id wybranego przycisku
                        public void onCheckedChanged(RadioGroup group, int checkedId){
                            // 1) odczytanie z etykiety, który obiekt modelu przechowuje
                            // dane o zmienionej ocenie
                            // 2) zapisanie zmienionej oceny
                            switch (checkedId){
                                case R.id.grade2:
                                    gradeRow.setValue(2);
                                    break;
                                case R.id.grade3:
                                    gradeRow.setValue(3);
                                    break;
                                case R.id.grade4:
                                    gradeRow.setValue(4);
                                    break;
                                case R.id.grade5:
                                    gradeRow.setValue(5);
                                    break;
                                default:
                                    gradeRow.setValue(2);
                            }
                        }
                    });
            //powiązanie grupy przycisków z obiektem w modelu
        }else{//aktualizacja istniejącego wiersza
            //powiązanie grupy przycisków z obiektem w modelu i
            //zapisanie referencji do obiektu modelu w grupie przycisków
            gradeRowView = viewToRecycle;
        }

        TextView etykieta = (TextView) gradeRowView.findViewById(R.id.gradeLabel);
        //ustawienie tekstu etykiety na podstawie modelu
        etykieta.setText(gradeRow.getName());
        //zaznaczenie odpowiedniego przycisku na podtawie modelu
        //zwrócenie nowego lub zaktualizowanego wiersza listy
        return gradeRowView;
    }
}
