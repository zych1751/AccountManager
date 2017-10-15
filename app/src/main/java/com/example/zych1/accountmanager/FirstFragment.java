package com.example.zych1.accountmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import db.DBHelper;

/**
 * Created by zych1 on 2017-10-13.
 */

public class FirstFragment extends android.support.v4.app.Fragment{

    private ImageButton addItem;
    private DBHelper dbHelper;
    private ListView lvExpense;
    boolean initialized;
    int year, month; // -1은 전체를 의미한다.

    public void getDataFromDB() {
        List expenses = dbHelper.getExpenseData(year, month);
        lvExpense.setAdapter(new ExpenseListAdapter(expenses, getContext()));
    }

    public FirstFragment() {
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

        View view = inflater.inflate(R.layout.fragment_first, container,false);
        lvExpense = (ListView) view.findViewById(R.id.lvExpense);
        addItem = (ImageButton) view.findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

                final Spinner yearSpinner = (Spinner) dialogView.findViewById(R.id.year);
                final Spinner monthSpinner = (Spinner) dialogView.findViewById(R.id.month);
                final Spinner daySpinner = (Spinner) dialogView.findViewById(R.id.day);
                final Spinner categorySpinner = (Spinner) dialogView.findViewById(R.id.category);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("아래 정보를 입력해주세요")
                        .setView(dialogView)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int year, month, day, money;
                                String category;
                                String content;

                                String yearString = yearSpinner.getSelectedItem().toString();
                                year = Integer.parseInt(yearString.substring(0, yearString.length()-1));
                                String monthString = monthSpinner.getSelectedItem().toString();
                                month = Integer.parseInt(monthString.substring(0, monthString.length()-1));
                                String dayString = daySpinner.getSelectedItem().toString();
                                day = Integer.parseInt(dayString.substring(0, dayString.length()-1));
                                category = categorySpinner.getSelectedItem().toString();
                                content = ((TextView)dialogView.findViewById(R.id.content)).getText().toString();
                                try {
                                    money = Integer.parseInt(((TextView)dialogView.findViewById(R.id.money)).getText().toString());
                                } catch(NumberFormatException e) {
                                    Toast.makeText(getContext(), "돈에는 숫자를 넣어주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Expense expense = new Expense(year, month, day, category, content, money);
                                dbHelper.addExpense(expense);

                                getDataFromDB();
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        getDataFromDB();
        initialized = true;

        // make spinner
        Spinner yearSpinner = (Spinner) view.findViewById(R.id.year_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.year, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] yearList = {-1, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025};
                year = yearList[position];
                getDataFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner monthSpinner = (Spinner) view.findViewById(R.id.month_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.month, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] monthList = {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
                month = monthList[position];
                getDataFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if (initialized && isVisible) {
            getDataFromDB();
        }else{
            return;
        }
    }
}
