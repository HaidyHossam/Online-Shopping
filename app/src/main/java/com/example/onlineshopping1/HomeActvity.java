package com.example.onlineshopping1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeActvity extends AppCompatActivity {
    int id;
    ArrayList<Product> array;
    int voiceCode = 1;
    ProductAdapter adap;
    ListView productList;

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView productList = (ListView) findViewById(R.id.productList);
        DatabaseHelper DB = new DatabaseHelper(this);
        Cursor cursor = DB.getProducts();
        ArrayList<Product> arrayL = DB.getData(cursor, id);
        ProductAdapter adap = new ProductAdapter(arrayL, this, id);
        productList.setAdapter(adap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.categories:
                Intent i = new Intent(HomeActvity.this, CategoryActivity.class);
                i.putExtra("id", id);
                startActivity(i);
                return true;
            case R.id.logout:
                Intent i2 = new Intent(HomeActvity.this, MainActivity.class);
                startActivity(i2);
                return true;
            case R.id.chartOption:
                Intent i3 = new Intent(HomeActvity.this, Charts.class);
                i3.putExtra("id", id);
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        EditText textInput = (EditText) findViewById(R.id.searchText);
        DatabaseHelper DB = new DatabaseHelper(this);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                array.clear();
                Cursor cursor = DB.barcodeSearch(result.getContents());
                if(cursor.getCount() > 0) {
                    array = DB.getData(cursor,id);
                    ProductAdapter adapter = new ProductAdapter(array,this,id);
                    productList.setAdapter(adapter);
                }
                else{
                    Toast.makeText(this,"Product doesn't exist!!",Toast.LENGTH_SHORT).show();
                    onResume();
                }
            }
        }
        if (requestCode == voiceCode && resultCode == HomeActvity.this.RESULT_OK) {
            ArrayList<String> resultText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textInput.setText(resultText.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_actvity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        DatabaseHelper DB = new DatabaseHelper(this);
        ImageButton cameraSearch = (ImageButton) findViewById(R.id.cameraSearch);
        ImageButton voiceSearch = (ImageButton) findViewById(R.id.voiceSearch);
        EditText textInput = (EditText) findViewById(R.id.searchText);
        productList = (ListView) findViewById(R.id.productList);
        FloatingActionButton GoToCart = (FloatingActionButton) findViewById(R.id.cartButton);

        Cursor cursor = DB.getProducts();
        array = DB.getData(cursor, id);

        id = getIntent().getExtras().getInt("id", 0);
        adap = new ProductAdapter(array, this, id);
        productList.setAdapter(adap);

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                array.clear();
                String text = textInput.getText().toString();
                Cursor cursor = DB.searchText(text);
                HashMap<Integer, Integer> quantity = DB.getCart(id);
                int q = 0;

                while (!cursor.isAfterLast()) {
                    if (quantity.containsKey(cursor.getInt(0)))
                        q = quantity.get(cursor.getInt(0));
                    else
                        q = 0;
                    array.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)
                            , (cursor.getInt(3) - q), cursor.getInt(4)));
                    cursor.moveToNext();
                }
                productList.setAdapter(adap);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        GoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActvity.this, CartActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        cameraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeActvity.this);
                intentIntegrator.initiateScan();
            }
        });
        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(i, voiceCode);
            }
        });
    }
}