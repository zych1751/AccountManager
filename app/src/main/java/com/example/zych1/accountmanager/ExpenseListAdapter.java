package com.example.zych1.accountmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import db.DBHelper;

/**
 * Created by zych1 on 2017-10-13.
 */

public class ExpenseListAdapter extends BaseAdapter {

    private DBHelper dbHelper;
    private List expenses;
    private Context context;

    public ExpenseListAdapter(List expenses, Context context) {
        this.expenses = expenses;
        this.context = context;
        dbHelper = new DBHelper(context, "TEST", null, 1);
    }

    @Override
    public int getCount() {
        return this.expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return this.expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView == null) {
            convertView = new LinearLayout(context);
            ((LinearLayout) convertView).setOrientation(LinearLayout.VERTICAL);

            LinearLayout titleLayout = new LinearLayout(convertView.getContext());
            titleLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout subLayout = new LinearLayout(convertView.getContext());
            subLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Title
            TextView tvCategory = new TextView(context);
            tvCategory.setPadding(10, 0, 20, 0);
            tvCategory.setTextColor(Color.rgb(0, 0, 0));
            tvCategory.setTextSize(25.0f);

            TextView tvContent = new TextView(context);
            tvContent.setPadding(10, 0, 20, 0);
            tvContent.setTextColor(Color.rgb(0, 0, 0));
            tvContent.setTextSize(25.0f);

            titleLayout.addView(tvCategory);
            titleLayout.addView(tvContent);

            // Sub
            TextView tvYear = new TextView(context);
            tvYear.setPadding(10, 0, 20, 0);
            tvYear.setTextColor(Color.rgb(0, 0, 0));
            tvYear.setTextSize(18.0f);

            TextView tvMonth = new TextView(context);
            tvMonth.setPadding(10, 0, 20, 0);
            tvMonth.setTextColor(Color.rgb(0, 0, 0));
            tvMonth.setTextSize(18.0f);

            TextView tvDay = new TextView(context);
            tvDay.setPadding(10, 0, 20, 0);
            tvDay.setTextColor(Color.rgb(0, 0, 0));
            tvDay.setTextSize(18.0f);

            TextView tvMoney = new TextView(context);
            tvMoney.setPadding(10, 0, 20, 0);
            tvMoney.setTextColor(Color.rgb(0, 0, 0));
            tvMoney.setTextSize(18.0f);

            subLayout.addView(tvYear);
            subLayout.addView(tvMonth);
            subLayout.addView(tvDay);
            subLayout.addView(tvMoney);

            ((LinearLayout) convertView).addView(titleLayout);
            ((LinearLayout) convertView).addView(subLayout);

            holder = new Holder(tvYear, tvMonth, tvDay, tvCategory, tvContent, tvMoney);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        final Expense expense = (Expense) getItem(position);

        holder.tvYear.setText(expense.getYear()+"년");
        holder.tvMonth.setText(expense.getMonth()+"월");
        holder.tvDay.setText(expense.getDay()+"일");
        holder.tvCategory.setText(expense.getCategory()+"  -");
        holder.tvContent.setText(expense.getContent());
        holder.tvMoney.setText(expense.getMoney()+"원");

        final int _id = expense.get_id();
        ((LinearLayout)convertView).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dbHelper.deleteByID(_id);
                                expenses.remove(expense);
                                notifyDataSetChanged();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("지우시겠습니까?").setPositiveButton("네", dialogClickListener)
                        .setNegativeButton("아니오", dialogClickListener).show();
                return false;
            }
        });
        convertView.setPadding(0, 5, 0, 5);
        return convertView;
    }

    private class Holder {
        public TextView tvYear;
        public TextView tvMonth;
        public TextView tvDay;
        public TextView tvCategory;
        public TextView tvContent;
        public TextView tvMoney;

        Holder(TextView tvYear, TextView tvMonth, TextView tvDay, TextView tvCategory, TextView tvContent, TextView tvMoney) {
            this.tvYear = tvYear;
            this.tvMonth = tvMonth;
            this.tvDay = tvDay;
            this.tvCategory = tvCategory;
            this.tvContent = tvContent;
            this.tvMoney = tvMoney;
        }
    }
}
