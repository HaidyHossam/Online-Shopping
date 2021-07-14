package com.example.onlineshopping1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private ArrayList<Product> list = new ArrayList<Product>();
    private Context context;
    int id;

    public ProductAdapter(ArrayList<Product> list, Context context,int id) {
        this.list = list;
        this.context = context;
        this.id = id;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_layout, null);

        DatabaseHelper DB = new DatabaseHelper(parent.getContext());
        Button addToCart = (Button) view.findViewById(R.id.addToCart);
        TextView price = (TextView) view.findViewById(R.id.productPrice);
        TextView name = (TextView) view.findViewById(R.id.productName);

        if (list.get(position).quantity <= 0) {
            addToCart.setEnabled(false);
            addToCart.setAlpha(0.5f);
        }

        price.setText(list.get(position).price + "$");
        name.setText(list.get(position).name);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).quantity--;
                if (list.get(position).quantity == 0) {
                    addToCart.setEnabled(false);
                    addToCart.setAlpha(0.5f);
                }
                DB.AddToCart(list.get(position).ID, id);
                Toast.makeText(parent.getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}