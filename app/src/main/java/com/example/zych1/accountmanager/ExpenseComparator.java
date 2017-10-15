package com.example.zych1.accountmanager;

import java.util.Comparator;

/**
 * Created by zych1 on 2017-10-15.
 */

public class ExpenseComparator implements Comparator<Expense> {
    @Override
    // 년, 월, 일, ID 순으로 정렬한다.
    public int compare(Expense e1, Expense e2) {
        if(e1.getYear() < e2.getYear()) {
            return -1;
        }
        if(e1.getYear() > e2.getYear()) {
            return 1;
        }

        if(e1.getMonth() < e2.getMonth()) {
            return -1;
        }
        if(e1.getMonth() > e2.getMonth()) {
            return 1;
        }

        if(e1.getDay() < e2.getDay()) {
            return -1;
        }
        if(e1.getDay() > e2.getDay()) {
            return 1;
        }

        if(e1.get_id() < e2.get_id()) {
            return -1;
        }
        return 1;
    }
}
