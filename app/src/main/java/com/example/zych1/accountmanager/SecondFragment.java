package com.example.zych1.accountmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import db.DBHelper;

/**
 * Created by zych1 on 2017-10-13.
 */

public class SecondFragment extends android.support.v4.app.Fragment{
    private DBHelper dbHelper;
    private LinearLayout layout;
    boolean initialized;
    int year, month; // -1 means all

    public SecondFragment() {
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

        layout = (LinearLayout) inflater.inflate(R.layout.fragment_second, container, false);
        drawData();
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
                drawData();
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
                drawData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return layout;
    }

    private void drawData() {
        // read data
        String[] categoryList = {"식비", "술", "오락", "교통비", "기타", "합계", "1일평균"};
        int[][] categoryMoney = new int[32][6];
        float[] dayAverage = new float[32];

        List expenses = dbHelper.getExpenseData(year, month);
        for(Object object: expenses) {
            Expense expense = (Expense) object;
            for(int i = 0; i < 5; i++) {
                if(expense.getCategory().equals(categoryList[i])) {
                    categoryMoney[expense.getDay()][i] += expense.getMoney();
                    break;
                }
            }
        }

        for(int i = 1; i <= 31; i++)
            for(int j = 0; j < 5; j++)
                categoryMoney[i][5] += categoryMoney[i][j];

        int total_sum = 0;
        for(int i = 1; i <= 31; i++) {
            total_sum += categoryMoney[i][5];
            dayAverage[i] = (float)total_sum / i;
        }


        // make table
        TableLayout table = (TableLayout) layout.findViewById(R.id.tableLayout);
        table.removeAllViews();
        TableRow row = new TableRow(getContext());
        TextView t = new TextView(getContext());

        TableRow.LayoutParams trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        trlp.setMargins(1, 0, 1, 0);
        t.setLayoutParams(trlp);
        t.setText("");
        t.setTextColor(Color.BLACK);
        t.setTextSize(13f);
        t.setBackgroundColor(Color.WHITE);
        t.setPadding(5, 1, 5, 1);
        t.setGravity(Gravity.CENTER);
        row.addView(t);

        for(int i = 0; i < 7; i++) {
            t = new TextView(getContext());
            trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            trlp.setMargins(1, 0, 1, 0);
            t.setLayoutParams(trlp);
            t.setText(categoryList[i]);
            t.setTextColor(Color.BLACK);
            t.setTextSize(13f);
            t.setBackgroundColor(Color.WHITE);
            t.setPadding(2, 1, 2, 1);
            t.setGravity(Gravity.CENTER);
            row.addView(t);
        }
        TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tlp.setMargins(0, 1, 0, 1);
        table.addView(row, tlp);

        for(int day = 1; day <= 31; day++) {
            row = new TableRow(getContext());
            t = new TextView((getContext()));
            trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            trlp.setMargins(1, 0, 1, 0);
            t.setLayoutParams(trlp);
            t.setText(day+"일");
            t.setTextColor(Color.BLACK);
            t.setTextSize(13f);
            t.setBackgroundColor(Color.WHITE);
            t.setPadding(5, 1, 5, 1);
            t.setGravity(Gravity.FILL);
            row.addView(t);

            for(int i = 0; i < 6; i++) {
                t = new TextView(getContext());
                t.setText(categoryMoney[day][i]+"");
                if(categoryMoney[day][i] == 0) {
                    t.setText("");
                }
                trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                trlp.setMargins(1, 0, 1, 0);
                t.setLayoutParams(trlp);
                t.setTextColor(Color.BLACK);
                t.setTextSize(getFontSize(categoryMoney[day][i]));
                t.setBackgroundColor(Color.WHITE);
                t.setPadding(5, 1, 5, 1);
                t.setGravity(Gravity.FILL);
                row.addView(t);
            }
            t = new TextView(getContext());
            t.setText((int)dayAverage[day]+"");
            trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            trlp.setMargins(1, 0, 1, 0);
            t.setLayoutParams(trlp);
            t.setTextColor(Color.BLACK);
            t.setTextSize(getFontSize((int)dayAverage[day]));
            t.setBackgroundColor(Color.WHITE);
            t.setPadding(5, 1, 5, 1);
            t.setGravity(Gravity.FILL);
            row.addView(t);

            tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            tlp.setMargins(0, 1, 0, 1);
            table.addView(row, tlp);
        }

        // final line
        row = new TableRow(getContext());
        t = new TextView(getContext());
        t.setText("합계");
        trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        trlp.setMargins(1, 0, 1, 0);
        t.setLayoutParams(trlp);
        t.setTextColor(Color.BLACK);
        t.setTextSize(13f);
        t.setBackgroundColor(Color.WHITE);
        t.setPadding(5, 1, 5, 1);
        t.setGravity(Gravity.FILL);
        row.addView(t);

        int[] colSum = new int[6];
        for(int day = 1; day <= 31; day++) {
            for(int category = 0; category < 6; category++) {
                colSum[category] += categoryMoney[day][category];
            }
        }

        for(int cate = 0; cate < 6; cate++) {
            t = new TextView(getContext());
            t.setText(colSum[cate]+"");
            trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            trlp.setMargins(1, 0, 1, 0);
            t.setLayoutParams(trlp);
            t.setTextColor(Color.BLACK);
            t.setTextSize(getFontSize(colSum[cate]));
            t.setBackgroundColor(Color.WHITE);
            t.setPadding(5, 1, 5, 1);
            t.setGravity(Gravity.FILL);
            row.addView(t);
        }
        t = new TextView(getContext());
        t.setText("");
        trlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        trlp.setMargins(1, 0, 1, 0);
        t.setLayoutParams(trlp);
        t.setTextColor(Color.BLACK);
        t.setTextSize(13f);
        t.setBackgroundColor(Color.WHITE);
        t.setPadding(5, 1, 5, 1);
        t.setGravity(Gravity.FILL);
        row.addView(t);

        tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tlp.setMargins(0, 1, 0, 16);
        table.addView(row, tlp);
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if (initialized && isVisible) {
            drawData();
        }else{
            return;
        }
    }

    private int getLength(int num) {
        int result = 0;
        while(num > 0) {
            num /= 10;
            result++;
        }
        return result;
    }

    private float getFontSize(int num) {
        int len = getLength(num);
        if(len <= 5)
            return 13f;
        if(len == 6)
            return 11f;
        if(len == 7)
            return 10f;
        return 9f;
    }
}
