package com.example.zych1.accountmanager;

/**
 * Created by zych1 on 2017-10-13.
 */

public class Expense {
    private int _id;

    private int year;
    private int month;
    private int day;
    private String category;
    private String content;
    private int money;

    public Expense() {}
    public Expense(int year, int month, int day, String category, String content, int money) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.category = category;
        this.content = content;
        this.money = money;
    }

    public int get_id() {
        return _id;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public String getCategory() {
        return category;
    }
    public String getContent() {
        return content;
    }
    public int getMoney() {
        return money;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setMoney(int money) {
        this.money = money;
    }
}
