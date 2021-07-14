package com.example.onlineshopping1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class orderAdapter extends BaseAdapter {
    private ArrayList<Product> list = new ArrayList<Product>();
    private Context context;
    HashMap<Integer, Integer> ProdQuantity;

    public orderAdapter(ArrayList<Product> list, Context context,HashMap<Integer, Integer> ProdQuantity) {
        this.list = list;
        this.context = context;
        this.ProdQuantity = ProdQuantity;
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
        view = inflater.inflate(R.layout.product_layout, null);

        TextView price = (TextView) view.findViewById(R.id.priceFinal);
        TextView name = (TextView) view.findViewById(R.id.productFinal);
        TextView quantity = (TextView) view.findViewById(R.id.quantityFinal);

        price.setText(list.get(position).price + "$");
        name.setText(list.get(position).name);
        quantity.setText(String.valueOf(ProdQuantity.get(list.get(position).ID)));

        return view;
    }
}
