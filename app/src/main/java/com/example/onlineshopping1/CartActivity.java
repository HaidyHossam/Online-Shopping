package com.example.onlineshopping1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        DatabaseHelper DB = new DatabaseHelper(this);
        ListView cartProducts = (ListView) findViewById(R.id.cartProducts);
        TextView total = (TextView) findViewById(R.id.totalVal);
        Button completeOrder = (Button) findViewById(R.id.ToMap);

        id = getIntent().getExtras().getInt("id");
        ArrayList<Product> arrayL = new ArrayList<Product>();

        Cursor cursor = DB.GetCart(id);
        HashMap<Integer, Integer> quantity = new HashMap<Integer, Integer>();

        while (!cursor.isAfterLast()) {
            int key = cursor.getInt(0);
            if (quantity.containsKey(key)) {
                quantity.put(key, quantity.get(key) + 1);
            } else {
                quantity.put(key, 1);
                arrayL.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)
                        , cursor.getInt(3), cursor.getInt(4)));
            }
            cursor.moveToNext();
        }

        CartAdapter adapter = new CartAdapter(arrayL, this, quantity, id, total);
        cartProducts.setAdapter(adapter);

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayL.size() != 0){
                    Intent i = new Intent(CartActivity.this, MapsActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
                else
                    Toast.makeText(getApplicationContext(),"Your cart is empty, Please add items",Toast.LENGTH_SHORT).show();
            }
        });
    }
}