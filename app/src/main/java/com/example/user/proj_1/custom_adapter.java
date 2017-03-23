package com.example.user.proj_1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;


public class InteraktywnyAdapterTablicy extends ArrayAdapter<ModelOceny> {
    //przechowujemy referencję do listy ocen
    private List<ModelOceny> listaOcen;
    private Activity kontekst;
    public InteraktywnyAdapterTablicy(Activity kontekst, List<ModelOceny> listaOcen){
        super(kontekst, R.layout.activity_grades_form, listaOcen);
        this.kontekst = kontekst;
        this.listaOcen = listaOcen;
    }

    //tworzenie nowego wiersza
    @Override
    public View getView(final int numerWiersza, View widokDoRecyklingu, ViewGroup parent){
        View widok = null;

        //utworzenie layout na podstawie pliku XML
        LayoutInflater pompka = kontekst.getLayoutInflater();
        widok = pompka.inflate(R.layout.activity_grades_form, null);

        //tworzenie nowego wiersza
        if (widokDoRecyklingu == null){
            //utworzenie layout na podstawie pliku XML
            //wybranie konkretnego przycisku radiowego musi zmieniać dane w modelu
            RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.grupaOceny);
            grupaOceny.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener(){
                        @Override // referencja do: 1) grupy przyc , 2) id wybranego przycisku
                        public void onCheckedChanged(RadioGroup group, int checkedId){
                            // 1) odczytanie z etykiety, który obiekt modelu przechowuje
                            // dane o zmienionej ocenie
                            Integer pos = (Integer) group.getTag();
                            // 2) zapisanie zmienionej oceny
                            listaOcen.get(numerWiersza).setGrade(pos);
                        }
                    }
            );
            //powiązanie grupy przycisków z obiektem w modelu
        }else{//aktualizacja istniejącego wiersza
            widok = widokDoRecyklingu;
            RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.grupaOceny);
            //powiązanie grupy przycisków z obiektem w modelu i
            //zapisanie referencji do obiektu modelu w grupie przycisków
            grupaOceny.setTag(listaOcen.get(numerWiersza));
        }


        TextView etykieta = (TextView) widok.findViewById(R.id.listaTextEtykieta);
        //ustawienie tekstu etykiety na podstawie modelu
        etykieta.setText(numerWiersza);
        RadioGroup grupaOceny = (RadioGroup) widok.findViewById(R.id.grupaOceny);
        grupaOceny.check(R.id.numerWiersza);
        //zaznaczenie odpowiedniego przycisku na podtawie modelu
        //zwrócenie nowego lub zaktualizowanego wiersza listy

        return widok;
    }
}
