package com.example.onlineshopping1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class Charts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        int id = getIntent().getExtras().getInt("id");
        DatabaseHelper DB = new DatabaseHelper(this);
        HashMap<String, Integer> values = DB.GetOrdersInProduct(id);
        ArrayList valueSet = new ArrayList();

        BarChart chart = (BarChart) findViewById(R.id.chart);

        int count = 1;
        for (String key : values.keySet()) {
            valueSet.add((new BarEntry(values.get(key), count)));
            count++;
        }

        BarDataSet barDataSet = new BarDataSet(valueSet, "Order History");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData Bardata = new BarData(barDataSet);
        chart.setData(Bardata);
        chart.animateXY(2000, 2000);

        ArrayList pievalueSet = new ArrayList();

        count = 0;
        for (String key : values.keySet()) {
            pievalueSet.add((new PieEntry(values.get(key), key)));
            count++;
        }
        PieChart pieChart = findViewById(R.id.piechart);
        PieDataSet dataSet = new PieDataSet(pievalueSet, "Order History");

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);
    }
}