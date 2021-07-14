    package com.example.onlineshopping1;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends BaseAdapter {
    private ArrayList<Product> list = new ArrayList<Product>();
    private HashMap<Integer, Integer> ProdQuantity = new HashMap<Integer, Integer>();
    private int UserID;
    private Context context;
    private TextView total;
    int totalAmount = 0;

    public CartAdapter(ArrayList<Product> list, Context context, HashMap<Integer, Integer> ProdQuantity, int UserID, TextView total) {
        this.list = list;
        this.context = context;
        this.ProdQuantity = ProdQuantity;
        this.UserID = UserID;
        this.total = total;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    public void checkQuantity(int position, ImageButton addOne) {
        for (int j = 0; j < list.size(); j++) {
            int q = (int) ProdQuantity.get(list.get(position).ID);
            if (list.get(position).quantity == q) {
                addOne.setEnabled(false);
                addOne.setAlpha(0.5f);
            } else {
                addOne.setEnabled(true);
                addOne.setAlpha(1f);
            }
        }
    }

    public void remove(DatabaseHelper DB, int position, int productID) {
        list.remove(list.get(position));
        ProdQuantity.put(productID, 0);
        DB.updateCart(list, UserID, ProdQuantity,UserID);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.cart_layout, null);

        DatabaseHelper DB = new DatabaseHelper(parent.getContext());
        ImageButton addOne = (ImageButton) view.findViewById(R.id.addOne);
        TextView price = (TextView) view.findViewById(R.id.cartPrice);
        price.setText(list.get(position).price + "$");
        TextView name = (TextView) view.findViewById(R.id.cartProduct);
        name.setText(list.get(position).name);
        ImageButton removeOne = (ImageButton) view.findViewById(R.id.remove);
        Button removeAll = (Button) view.findViewById(R.id.removeAll);
        EditText quantity = (EditText) view.findViewById(R.id.quantity);

        checkQuantity(position, addOne);
        int productID = list.get(position).ID;
        quantity.setText(String.valueOf(ProdQuantity.get(productID)));

        totalAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            totalAmount = totalAmount + (list.get(i).price) * (ProdQuantity.get(list.get(i).ID));
        }
        total.setText(String.valueOf(totalAmount) + "$");

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (quantity.getText().toString().equals("")) {
                    quantity.setText(" ");
                }*/

                int q = list.get(position).quantity;
                if (Integer.valueOf(quantity.getText().toString()) > q) {
                    //quantity.setText(String.valueOf(list.get(position).quantity));
                    ProdQuantity.put(productID, Integer.valueOf(quantity.getText().toString()));
                }
                    /*totalAmount = totalAmount + (ProdQuantity.get(productID) * list.get(position).price);
                    total.setText(String.valueOf(totalAmount));
                    DB.updateCart(list, UserID, ProdQuantity);*/

            }
        });

        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmount = totalAmount - (ProdQuantity.get(productID) * list.get(position).price);
                total.setText(String.valueOf(totalAmount) + "$");
                remove(DB, position, productID);
            }
        });

        addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity.setText(String.valueOf(ProdQuantity.get(productID) + 1));
                ProdQuantity.put(productID, ProdQuantity.get(productID) + 1);
                checkQuantity(position, addOne);
                totalAmount = totalAmount + list.get(position).price;
                total.setText(String.valueOf(totalAmount) + "$");
                DB.updateCart(list, UserID, ProdQuantity,UserID);
            }
        });
        removeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityVal = ProdQuantity.get(productID) - 1;
                if (quantityVal == 0) {
                    totalAmount = totalAmount - (ProdQuantity.get(productID) * list.get(position).price);
                    total.setText(String.valueOf(totalAmount) + "$");
                    remove(DB, position, productID);
                } else {
                    quantity.setText(String.valueOf(quantityVal));
                    ProdQuantity.put(productID, quantityVal);
                    checkQuantity(position, addOne);
                    totalAmount = totalAmount - list.get(position).price;
                    total.setText(String.valueOf(totalAmount) + "$");
                }
                DB.updateCart(list, UserID, ProdQuantity,UserID);
            }
        });

        return view;
    }
}
