package com.example.onlineshopping1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    String dateValue;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        DatabaseHelper DB = new DatabaseHelper(this);
        Button signup = (Button) findViewById(R.id.signupbtn);
        final TextInputLayout UsernameLayout = (TextInputLayout) findViewById(R.id.usernameInputlayout);
        EditText usernameText = (EditText) findViewById(R.id.usernameSignup);
        EditText securityQ = (EditText) findViewById(R.id.securityQuestion);
        EditText date = (EditText) findViewById(R.id.date);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setInputType(0);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        dateValue = year + "-" + (month + 1) + "-" + dayOfMonth;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = ((EditText) findViewById(R.id.usernameSignup)).getText().toString();
                if (s.length() == 0)
                    UsernameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = ((EditText) findViewById(R.id.usernameSignup)).getText().toString();
                UsernameLayout.setErrorEnabled(false);
                if (username.length() == 0)
                    UsernameLayout.setErrorEnabled(false);
                else {
                    Cursor cursor = DB.checkUsername(username);
                    boolean usernameExists = false;
                    if (cursor != null && cursor.getCount() > 0)
                        usernameExists = true;
                    if (usernameExists == true) {
                        UsernameLayout.setError("Username already exists");
                        UsernameLayout.setErrorEnabled(true);
                    }
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.nameSignup)).getText().toString();
                String username = ((EditText) findViewById(R.id.usernameSignup)).getText().toString();
                String pass = ((EditText) findViewById(R.id.passwordSignup)).getText().toString();
                String job = ((EditText) findViewById(R.id.jobSignup)).getText().toString();
                RadioButton female = (RadioButton) findViewById(R.id.female);
                RadioButton male = (RadioButton) findViewById(R.id.male);
                String securityAns = securityQ.getText().toString();
                Cursor cursor = DB.checkUsername(username);
                boolean usernameExists = false;
                if (cursor != null && cursor.getCount() > 0)
                    usernameExists = true;
                if (usernameExists == true) {
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    if (female.isChecked()) {
                        gender = "female";
                    } else if (male.isChecked()) {
                        gender = "male";
                    }
                    DB.addCustomer(name, username, pass, gender, dateValue, job, securityAns);

                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}