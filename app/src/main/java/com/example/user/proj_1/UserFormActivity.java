package com.example.user.proj_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Callable;


// todo for some reason 'surname' / TFsurname field doesn't trigger error on lose focus ??
public class UserFormActivity extends AppCompatActivity {
    // deklaracja zmiennych pol i odpowiadajace im zmienne znakowe
    private EditText et_name, et_surname, et_grades;
    private Button et_buttonGrades;
    private boolean nameChecked = false, surnameChecked = false, gradesChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        getFields();
        et_buttonGrades.setVisibility(View.INVISIBLE);


        // obsluge zmiany focusu pol tekstowych:
        // wiadomosci co nalezy poprawic w przypadku zajscia zdarzenia
        final String nameErrorMessage, surnameErrorMessage, gradesErrorMessage;
        nameErrorMessage = "Please enter valid name: min 4, max 30 characters";
        surnameErrorMessage = "Please enter valid surname: min 4, max 50 characters";
        gradesErrorMessage = "Please enter number in range of 5-15";


        // ustawienie listenera na zmiane focusu pol z funkcja validujaca jako parametr
        setOnFocusChangeListener(et_name, new Callable<Boolean>() {
            public Boolean call(){
                return validateTextField(et_name, 4, 30, nameErrorMessage, true);}
        });
        setOnFocusChangeListener(et_surname, new Callable<Boolean>() {
            public Boolean call(){
                return validateTextField(et_surname, 4, 50, surnameErrorMessage, true);}
        });
        setOnFocusChangeListener(et_surname, new Callable<Boolean>() {
            public Boolean call(){
                return validateNumberField(et_grades, 5, 15, gradesErrorMessage, true);}
        });


        // ustawienie textWatchera dla kazdego pola
        et_name.addTextChangedListener(new GenericTextWatcher(et_name));
        et_surname.addTextChangedListener(new GenericTextWatcher(et_surname));
        et_grades.addTextChangedListener(new GenericTextWatcher(et_grades));
    }


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


    // GenericTextWatcher sprawdza flagi wszyskich pol w celu dynamicznego wyswietlenia przycisku
    // gdy pola sa uzuplenione poprawnie lub ukrycie go gdy jakies znow bedzie niepoprawnie
    // uzupelnione. Do tego celu uzylem flag i odzielnie zdefiniowanych funkcji.
    private class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view) {this.view = view;}

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            switch(view.getId()){
                case R.id.TFname:
                    nameChecked = validateTextField(et_name, 4, 30, "", false);
                    break;
                case R.id.TFsurname:
                    surnameChecked = validateTextField(et_surname, 4, 50, "", false);
                    break;
                case R.id.TFgrades:
                    gradesChecked = validateNumberField(et_grades, 5, 15, "", false);
                    break;
            }
            if (nameChecked && surnameChecked && gradesChecked){
                et_buttonGrades.setVisibility(View.VISIBLE);
            }else {
                et_buttonGrades.setVisibility(View.INVISIBLE);
            }
        }
    }


    // zainicijalizuj zmienne wartosciami edytowalnych pol tekstowych
    private void getFields(){
        et_name = (EditText)findViewById(R.id.TFname);
        et_surname = (EditText)findViewById(R.id.TFsurname);
        et_grades = (EditText)findViewById(R.id.TFgrades);
        et_buttonGrades = (Button) findViewById(R.id.Bgrades);
    }


    // walidacja pol dla zadanych parametrow z opcjonalnym wyswietlaniem powiadomien o bledach
    public Boolean validateTextField(EditText textField, int minLength, int maxLength,
                                     String errorMessage, boolean showErrorMessage){
        String strName = textField.getText().toString().trim();
        if (strName.isEmpty() || minLength > strName.length() || strName.length() > maxLength) {
            if(showErrorMessage)
                textField.setError(errorMessage);
            return false;
        }
        return true;
    }

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
