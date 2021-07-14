package com.example.onlineshopping1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordRecovery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password_recovery);

        DatabaseHelper DB = new DatabaseHelper(this);
        EditText newPassword = (EditText) findViewById(R.id.newPassword);
        EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        EditText securityAns = (EditText) findViewById(R.id.sequrityQ);
        final TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.passlayout);
        final TextInputLayout questionLayout = (TextInputLayout) findViewById(R.id.questionLayout);
        Button change = (Button) findViewById(R.id.change);
        passwordLayout.setErrorEnabled(false);
        questionLayout.setErrorEnabled(false);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getExtras().getString("username");
                String newPass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                String Ans = securityAns.getText().toString();
                questionLayout.setErrorEnabled(false);
                if (confirmPass.equals(newPass)) {
                    passwordLayout.setErrorEnabled(false);
                    boolean check = DB.updatePassword(username, newPass, Ans);
                    if (check == false) {
                        questionLayout.setErrorEnabled(true);
                        questionLayout.setError("Wrong Answer!");
                    } else {
                        Intent i = new Intent(PasswordRecovery.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Password doesn't match!");
                }
            }
        });
    }
}