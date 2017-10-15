package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.zych1.accountmanager.Expense;
import com.example.zych1.accountmanager.ExpenseComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zych1 on 2017-10-10.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public void allDelete() {
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" DELETE FROM TEST_TABLE ");

        db.execSQL(sb.toString());
    }

    public void deleteByID(int id) {
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" DELETE FROM TEST_TABLE WHERE _ID=" + id);

        db.execSQL(sb.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE TEST_TABLE ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" YEAR INTEGER,");
        sb.append(" MONTH INTEGER,");
        sb.append(" DAY INTEGER,");
        sb.append(" CATEGORY TEXT,");
        sb.append(" CONTENT TEXT,");
        sb.append(" MONEY INTEGER ) ");

        db.execSQL(sb.toString());
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TEST_TABLE ( ");
        sb.append(" YEAR, MONTH, DAY, CATEGORY, CONTENT, MONEY ) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ?, ? ) ");

        db.execSQL(sb.toString(), new Object[]{
                expense.getYear(),
                expense.getMonth(),
                expense.getDay(),
                expense.getCategory(),
                expense.getContent(),
                expense.getMoney()
        });
        Toast.makeText(context, "등록 완료", Toast.LENGTH_SHORT).show();
    }

    public List getExpenseData(int year, int month) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, YEAR, MONTH, DAY, CATEGORY, CONTENT, MONEY FROM TEST_TABLE");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List expenses = new ArrayList();
        Expense expense = null;

        // TODO: change SQL query
        while(cursor.moveToNext()) {
            if((year != -1 && cursor.getInt(1) != year) || (month != -1 && cursor.getInt(2) != month))
                continue;
            expense = new Expense(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
            expense.set_id(cursor.getInt(0));
            expenses.add(expense);
        }

        ExpenseComparator comp = new ExpenseComparator();
        Collections.sort(expenses, comp);
        return expenses;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }
}
