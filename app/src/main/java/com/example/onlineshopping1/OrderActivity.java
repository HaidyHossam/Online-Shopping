package com.example.onlineshopping1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Button confirm = (Button) findViewById(R.id.finish);
        TextView total = (TextView) findViewById(R.id.finalTotal);
        TextView addressTxt = (TextView) findViewById(R.id.finalAddress);
        ListView products = (ListView) findViewById(R.id.finalProducts);

        DatabaseHelper DB = new DatabaseHelper(this);
        id = getIntent().getExtras().getInt("id");

        ArrayList<Product> arrayL = new ArrayList<Product>();

        String address = getIntent().getExtras().getString("address", "");
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

        orderAdapter adapter = new orderAdapter(arrayL, this, quantity);
        products.setAdapter(adapter);
        addressTxt.setText(address);

        int totalAmount = 0;
        for (int i = 0; i < arrayL.size(); i++) {
            totalAmount = totalAmount + (arrayL.get(i).price) * (quantity.get(arrayL.get(i).ID));
        }
        total.setText(String.valueOf(totalAmount) + "$");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                int OrderID = DB.addOrder(formattedDate, id, address);
                if (OrderID != -1) {
                    for (int i = 0; i < quantity.size(); i++) {
                        DB.addOrderDetails(quantity.get(arrayL.get(i).ID),arrayL.get(i).ID,OrderID);
                    }
                    DB.updateProducts(arrayL,quantity,id);
                    Toast.makeText(getApplicationContext(),"Order placed successfully",Toast.LENGTH_SHORT).show();
                    Intent i2 = new Intent(OrderActivity.this, HomeActvity.class);
                    i2.putExtra("id", id);
                    startActivity(i2);
                }
            }
        });
    }
}