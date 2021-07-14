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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class CategoryActivity extends AppCompatActivity {
    int cat_id = 0;
    int CustID = 0;
    int voiceCode = 1;

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(CategoryActivity.this, HomeActvity.class);
                i.putExtra("id",CustID);
                startActivity(i);
                return true;
            case R.id.logout:
                Intent i2 = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(i2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        EditText textInput = (EditText) findViewById(R.id.searchTextcat);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                textInput.setText(result.getContents());
            }
        }
        if (requestCode == voiceCode && resultCode == CategoryActivity.this.RESULT_OK) {
            ArrayList<String> resultText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textInput.setText(resultText.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ListView productList = (ListView) findViewById(R.id.productCat);
        GridView catGrid = (GridView) findViewById(R.id.categories);
        TextView catgegory = (TextView) findViewById(R.id.category);
        TextView textInput = (TextView) findViewById(R.id.searchTextcat);
        ImageButton voiceSearch = (ImageButton) findViewById(R.id.voiceSearchCat);
        ImageButton cameraSearch = (ImageButton) findViewById(R.id.cameraSearchcat);
        FloatingActionButton GoToCart = (FloatingActionButton) findViewById(R.id.cartButtoncat);

        CustID = getIntent().getExtras().getInt("id", 0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        catGrid.setAdapter(adapter);
        DatabaseHelper DB = new DatabaseHelper(this);
        ArrayList<Product> productsByCat = new ArrayList<Product>();
        ProductAdapter adapter2 = new ProductAdapter(productsByCat, this, CustID);

        Cursor cursor = DB.getCategories();
        while (!cursor.isAfterLast()) {
            ids.add(cursor.getInt(0));
            adapter.add(cursor.getString(1));
            cursor.moveToNext();
        }

        catGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productsByCat.clear();
                Cursor c = DB.getProductByCat(ids.get(position));
                cat_id = position;

                catgegory.setText(adapter.getItem(cat_id));
                HashMap<Integer, Integer> quantity = DB.getCart(CustID);
                int q = 0;

                while (!c.isAfterLast()) {
                    if (quantity.containsKey(c.getInt(0)))
                        q = quantity.get(c.getInt(0));
                    else
                        q = 0;
                    productsByCat.add(new Product(c.getInt(0), c.getString(1), c.getInt(2)
                            , (c.getInt(3) - q), c.getInt(4)));
                    c.moveToNext();
                }
                productList.setAdapter(adapter2);
            }
        });

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productsByCat.clear();
                String text = textInput.getText().toString();
                Cursor c = DB.searchTextByCat(text, ids.get(cat_id));

                HashMap<Integer, Integer> quantity = DB.getCart(CustID);
                int q = 0;

                while (!c.isAfterLast()) {
                    if (quantity.containsKey(c.getInt(0)))
                        q = quantity.get(c.getInt(0));
                    else
                        q = 0;
                    productsByCat.add(new Product(c.getInt(0), c.getString(1), c.getInt(2)
                            , (c.getInt(3) - q), c.getInt(4)));
                    c.moveToNext();
                }
                productList.setAdapter(adapter2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        GoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, CartActivity.class);
                i.putExtra("id", CustID);
                startActivity(i);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        cameraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(CategoryActivity.this);
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