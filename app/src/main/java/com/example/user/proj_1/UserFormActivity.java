package com.example.user.proj_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Callable;


public class UserFormActivity extends AppCompatActivity {
    // deklaracja zmiennych pol i odpowiadajace im zmienne znakowe
    private EditText nameEt, surnameEt, gradesEt;
    private Button gradesBtn, resultBtn;
    private TextView avgTv;
    private boolean nameChecked = false, surnameChecked = false,
            gradesChecked = false, positiveAvg = false, resultClicked=false;
    private double avg = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        getFields();       // przypisz zmiennym odpowiednie pola z layoutu
        hideFinalFields(); // ukryj zbedne elementy


        // wiadomosci co nalezy poprawic w przypadku zajscia zdarzenia
        final String nameErrorMessage, surnameErrorMessage, gradesErrorMessage;
        nameErrorMessage = "Please enter valid name: min 4, max 30 characters";
        surnameErrorMessage = "Please enter valid surname: min 4, max 50 characters";
        gradesErrorMessage = "Please enter number in range of 5-15";


        // ustawienie listenera na zmiane focusu pol z funkcja validujaca jako parametr
        // gdy zmieni sie focus, a dane sa bledne, wyswietla sie uwaga z informacja co
        // nalezy poprawic
        setOnFocusChangeListener(nameEt, new Callable<Boolean>() {
            public Boolean call(){
                return validateTextField(nameEt, 4, 30, nameErrorMessage, true);}});
        setOnFocusChangeListener(surnameEt, new Callable<Boolean>() {
            public Boolean call(){
                return validateTextField(surnameEt, 1, 50, surnameErrorMessage, true);}});
        setOnFocusChangeListener(gradesEt, new Callable<Boolean>() {
            public Boolean call(){
                return validateNumberField(gradesEt, 5, 15, gradesErrorMessage, true);}
        });


        // ustawienie textWatchera dla kazdego pola, przez co przycisk wyswietla sie
        // dynamicznie gdy dane sa poprawne, uzywajac tyc samych funkcji walidujacych
        // co focusListener, lecz z wylaczonymi komunikatami o bledzie
        nameEt.addTextChangedListener(new GenericTextWatcher(nameEt));
        surnameEt.addTextChangedListener(new GenericTextWatcher(surnameEt));
        gradesEt.addTextChangedListener(new GenericTextWatcher(gradesEt));
    }

    /**
     * Metoda wywolywana po powrocie z GradesFormActivity, i wyswietla informajce zalezne
     * od sredniej.
     * @param data zawiera informacje o  obliczonej sredniej
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        avg = data.getDoubleExtra("average", avg);
        Log.d("POKRECENIU:  ", "srednia po powrocie  "+avg);
        setTextFields(false);  // blokuje mozliwosc modyfikacji zawartosci pola
        setBtnResult(avg);     // ustawia finalny przycisk odpowiednio dla sredniej
    }


    /**
     * Zapisuje stan interfejsu do pakietu savedInstanceState, nastepnie jest on
     * przekazywany do metody onCreate jesli proces jest zniszczony i zrestartowany
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("avg", avg);  // zapisanie stanu sredniej
        // jesli srednia zmienila wartosc z zainicjalizowanej, to zapisz stan flagi
        // kliniecia w przycisk wyniku, przez co wiadomosc nie znika po obroceniu ekranu
        if(avg > 0)
            savedInstanceState.putBoolean("resultClicked", resultClicked);
    }


    /**
     * Odczytuje stan interfejsu z pakietu savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        avg = savedInstanceState.getDouble("avg");
        if(avg>0){
            setTextFields(false);
            setBtnResult(avg);
            resultClicked = savedInstanceState.getBoolean("resultClicked");
            // jesli srednia jest byla zmieniana, to sprawdz flage finalnej wiadomosci
            if(resultClicked)
                showFinalMessage();
        }
    }


    // Gdy dane sa wprowadzone pojawia sie przycisk, ktorego klikniecie przenosi aplikacje do
    // widoku formularza ocen
    public void onGradesBtnClick(View v){
        Intent generateGradesForm = new Intent(UserFormActivity.this, GradesFormActivity.class);
        int gradesQuantity = Integer.parseInt(gradesEt.getText().toString().trim());
        generateGradesForm.putExtra("gradesQuantity", gradesQuantity);
        startActivityForResult(generateGradesForm, 1);
    }


    /**
     * Obsluga przycisku po powrocie z GradesActivity. Po kilkinieciu wyswietlany jest komunikat
     * ktorego tresc zalezy od wczesniej podanej sredniej, po zamknieciu okienka aplikacja jest
     * zamykana i konczy sie cala aktywnosc.
     */
    public void onResultClick(View v){
        resultClicked = true;
        showFinalMessage();
    }


    // Funkcja zmienia mozliwosc modyfikacji zawartosci pola
    private void setTextFields(boolean logState){
        nameEt.setEnabled(logState);
        surnameEt.setEnabled(logState);
        gradesEt.setEnabled(logState);
    }


    // Funkcja zmienia tekst przycisku zaleznie od sredniej i ustawia flage dla sredniej
    private void setBtnResult(double avg){
        avgTv.setVisibility(View.VISIBLE);
        avgTv.setText(String.format("Twoja średnia to %s", String.valueOf(avg)));

        if(avg >= 3){
            resultBtn.setText("Super :)");
            positiveAvg = true;
        }else{
            resultBtn.setText("Not this time, sorry.");
            positiveAvg = false;
        }
        gradesBtn.setVisibility(View.GONE);
        resultBtn.setVisibility(View.VISIBLE);
    }


    /**
     * Obsluga zdarzenia utraty focusu dla danego pola. Jesli pole pomyslnie przejdzie walidacje
     * to wartosc jest zapisywana. W przeciwnym wypadku wyswietlana jest informacja co nalezy
     * poprawic w danym polu.
     * @param editText pole, ktorego zmiana focusu jest podgladana
     * @param validationFunction funkcja walidujaca przekazywane pole
     */
    private void setOnFocusChangeListener(final EditText editText,
                                          final Callable<Boolean> validationFunction){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    try {
                        validationFunction.call();
                    } catch (Exception e) {
                        Toast.makeText(UserFormActivity.this,
                                "There was an error on focus change listener.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    /**
     * Obsluga zdarzenia zmiany zawartosci danego pola tekstowego. Jesli pole pomyslnie przejdzie
     * walidacje, to wartosc jest zapisywana oraz ustawiana jest zmienna logiczna informujaca o
     * poprawnym uzupelnieniu tego pola. Gdy flagi dla wszystkich pol sa oznaczone jako 'true', to
     * pojawia sie przycisk, jesli jakas dana sie zmieni przycisk znika.
     */
    private class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view) {this.view = view;}

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            switch(view.getId()){
                case R.id.TFname:
                    nameChecked = validateTextField(nameEt, 4, 30, "", false);
                    break;
                case R.id.TFsurname:
                    surnameChecked = validateTextField(surnameEt, 1, 50, "", false);
                    break;
                case R.id.TFgrades:
                    gradesChecked = validateNumberField(gradesEt, 5, 15, "", false);
                    break;
            }
            if (nameChecked && surnameChecked && gradesChecked){
                gradesBtn.setVisibility(View.VISIBLE);
            }else {
                gradesBtn.setVisibility(View.GONE);
            }
        }
    }


    // zainicijalizuj zmienne wartosciami edytowalnych pol tekstowych
    private void getFields(){
        nameEt = (EditText)findViewById(R.id.TFname);
        surnameEt = (EditText)findViewById(R.id.TFsurname);
        gradesEt = (EditText)findViewById(R.id.TFgrades);
        gradesBtn = (Button)findViewById(R.id.Bgrades);
        resultBtn = (Button)findViewById(R.id.Bresult);
        avgTv = (TextView)findViewById(R.id.TVaverage);
    }


    // ukrycie pol niepozadanych w poczatkowym etapie programu
    private void hideFinalFields(){
        gradesBtn.setVisibility(View.GONE);
        resultBtn.setVisibility(View.GONE);
        avgTv.setVisibility(View.GONE);
    }


    // wyswietla informacje zalezna od osredniej i konczy dzialanie aplikacji
    public void showFinalMessage(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserFormActivity.this);
        dialog.setTitle("Result");
        if(positiveAvg)
            dialog.setMessage("Congratulations! You've passed!");
        else
            dialog.setMessage("I am applying for a conditional credit.");
        dialog.setNegativeButton("End", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UserFormActivity.this.finish();
                System.exit(0);
            }
        });
        dialog.show();
    }


    // walidacja pol dla zadanych parametrow z opcjonalnym wyswietlaniem powiadomien o bledach
    // 1) dla pol tekstowych
    public Boolean validateTextField(EditText textField, int minLength, int maxLength,
                                     String errorMessage, boolean showErrorMessage){
        String strName = textField.getText().toString().trim();
        if (strName.isEmpty() || minLength > strName.length() || strName.length() > maxLength) {
            if(showErrorMessage) {
                textField.setError(errorMessage);
            }
            return false;
        }
        return true;
    }

    // 2) dla liczb
    public Boolean validateNumberField(EditText textField, int minValue, int maxValue,
                                       String errorMessage, boolean showErrorMessage){
        String strNumber = textField.getText().toString().trim();
        int number;
        if (strNumber.isEmpty()){
            number = 0;
        }else{
            number = Integer.parseInt(strNumber);
        }
        if (number < minValue || number > maxValue) {
            if(showErrorMessage)
                textField.setError(errorMessage);
            return false;
        }
        return true;
    }
}