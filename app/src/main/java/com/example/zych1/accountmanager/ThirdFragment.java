package com.example.zych1.accountmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import db.DBHelper;

/**
 * Created by zych1 on 2017-10-13.
 */

public class ThirdFragment extends android.support.v4.app.Fragment{

    private DBHelper dbHelper;
    PieChart chart;
    LinearLayout layout;
    boolean initialized;
    int year, month;

    public ThirdFragment() {
        initialized = false;
        year = month = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(dbHelper == null) {
            dbHelper = new DBHelper(getContext(), "TEST", null, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(dbHelper == null) {
            dbHelper = new DBHelper(getContext(), "TEST", null, 1);
        }
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_third, container, false);
        makeChart();
        initialized = true;
        // make spinner
        Spinner yearSpinner = (Spinner) layout.findViewById(R.id.year_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.year, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //각 항목 클릭시 포지션값을 토스트에 띄운다.
                int[] yearList = {-1, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025};
                year = yearList[position];
                makeChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner monthSpinner = (Spinner) layout.findViewById(R.id.month_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.month, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //각 항목 클릭시 포지션값을 토스트에 띄운다.
                int[] monthList = {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
                month = monthList[position];
                makeChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return layout;
    }

    // make pie chart
    public void makeChart() {
        chart = (PieChart) layout.findViewById(R.id.chart);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.getDescription().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setRotationEnabled(true);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleAlpha(0);
        chart.setEntryLabelColor(Color.BLACK);
        addDataSet();
    }

    private void addDataSet() {

        List expenses = dbHelper.getExpenseData(year, month);

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        String categoryList[] = {"식비", "술", "오락", "교통비", "기타"};
        int categorySum[] = new int[5];
        int sum = 0;

        for(Object object: expenses) {
            Expense expense = (Expense) object;
            for(int i = 0; i < 5; i++) {
                if(expense.getCategory().equals(categoryList[i])) {
                    categorySum[i] += expense.getMoney();
                    break;
                }
            }
            sum += expense.getMoney();
        }

        for(int i = 0; i < 5; i++) {
            if(categorySum[i] == 0) continue;
            yEntrys.add(new PieEntry((float)categorySum[i]/sum*100, categoryList[i]+"\n"+categorySum[i]));
            xEntrys.add(categoryList[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Rate");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);
        chart.setData(pieData);

        chart.getLegend().setEnabled(false);

        chart.invalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if (initialized && isVisible) {
            makeChart();
        }else{
            return;
        }
    }
}
