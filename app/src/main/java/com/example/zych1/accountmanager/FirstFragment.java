package com.example.zych1.accountmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText etYear = new EditText(getContext());
                etYear.setHint("몇년인지 입력하세요.");
                final EditText etMonth = new EditText(getContext());
                etMonth.setHint("몇월인지 입력하세요.");
                final EditText etDay = new EditText(getContext());
                etDay.setHint("몇일인지 입력하세요.");
                final EditText etCategory = new EditText(getContext());
                etCategory.setHint("카테고리를 입력하세요.");
                final EditText etContent = new EditText(getContext());
                etContent.setHint("항목을 입력하세요.");
                final EditText etMoney = new EditText(getContext());
                etMoney.setHint("돈을 입력하세요");

                layout.addView(etYear);
                layout.addView(etMonth);
                layout.addView(etDay);
                layout.addView(etCategory);
                layout.addView(etContent);
                layout.addView(etMoney);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("아래 정보를 입력해주세요")
                        .setView(layout)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: try, catch를 null을 활용해 함수 하나로 뺴자
                                int year, month, day, money;
                                try {
                                    year = Integer.parseInt(etYear.getText().toString());
                                } catch (NumberFormatException e) {
                                    Toast.makeText(getContext(), "년도에는 숫자를 넣어주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    month = Integer.parseInt(etMonth.getText().toString());
                                } catch(NumberFormatException e) {
                                    Toast.makeText(getContext(), "월에는 숫자를 넣어주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    day = Integer.parseInt(etDay.getText().toString());
                                } catch(NumberFormatException e) {
                                    Toast.makeText(getContext(), "일에는 숫자를 넣어주세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String category = etCategory.getText().toString();
                                if(!(category.equals("식비") || category.equals("술") || category.equals("오락")
                                        || category.equals("교통비") || category.equals("기타"))) {
                                    Toast.makeText(getContext(), "카테고리에는 식비, 술, 오락, 교통비, 기타만 가능합니다", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                String content = etContent.getText().toString();
                                try {
                                    money = Integer.parseInt(etMoney.getText().toString());
                                } catch(NumberFormatException e) {
                                    Toast.makeText(getContext(), "일에는 숫자를 넣어주세요", Toast.LENGTH_SHORT).show();
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
                //각 항목 클릭시 포지션값을 토스트에 띄운다.
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
