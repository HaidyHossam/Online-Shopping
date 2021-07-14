package com.example.onlineshopping1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private String sharedPrefFile = "com.example.android.onlineShoppingPref";
    String loggedinUsername, loggedinPass;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DatabaseHelper DB = new DatabaseHelper(this);
        TextView signup = (TextView) findViewById(R.id.signup);
        TextView forgotpass = (TextView) findViewById(R.id.forgetpass);
        Button login = (Button) findViewById(R.id.login);
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.UsernametextInputLayout);
        final TextInputLayout floatingPassLabel = (TextInputLayout) findViewById(R.id.PasstextInputLayout);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        CheckBox remember = (CheckBox) findViewById(R.id.rememberme);

        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Boolean Logged_in = mPreferences.getBoolean("remember", false);
        if (Logged_in == true) {
            username.setText(mPreferences.getString("username", ""));
            password.setText(mPreferences.getString("password", ""));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                floatingUsernameLabel.setErrorEnabled(false);
                floatingPassLabel.setErrorEnabled(false);
                Boolean checkPass = DB.checkPassword(usernameValue, passwordValue);
                if (checkPass == true) {
                    //Toast.makeText(getApplicationContext(), "Welcome ya amr", Toast.LENGTH_SHORT).show();
                    if (remember.isChecked()) {
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString("username", usernameValue);
                        editor.putString("password", passwordValue);
                        editor.putBoolean("remember", true);
                        editor.apply();
                    }
                    Cursor cursor = DB.checkUsername(username.getText().toString());
                    Intent i = new Intent(MainActivity.this, HomeActvity.class);
                    i.putExtra("id", cursor.getInt(0));
                    startActivity(i);
                } else {
                    floatingPassLabel.setError("Wrong username or password");
                    floatingPassLabel.setErrorEnabled(true);
                    floatingUsernameLabel.setError("Wrong username or password");
                    floatingUsernameLabel.setErrorEnabled(true);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().equals("")) {
                    Cursor cursor = DB.checkUsername(username.getText().toString());
                    boolean usernameExists = false;
                    if (cursor != null && cursor.getCount() > 0)
                        usernameExists = true;
                    if (usernameExists == true) {
                        floatingUsernameLabel.setErrorEnabled(false);
                        Intent i = new Intent(MainActivity.this, PasswordRecovery.class);
                        i.putExtra("username", username.getText().toString());
                        startActivity(i);
                    } else {
                        floatingUsernameLabel.setError("Please enter a valid username");
                        floatingUsernameLabel.setErrorEnabled(true);
                    }
                }
            }
        });

    }
}