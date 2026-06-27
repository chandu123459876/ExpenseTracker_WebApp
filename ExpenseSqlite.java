package com.softwareapp.expensestracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
public class ExpenseSqlite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DATABASE_LEMON";
    private static final int DATABASE_VERSION = 1;
    public ExpenseSqlite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table expense(id INTEGER primary key autoincrement, amount DOUBLE, reason TEXT, time DOUBLE)");
        database.execSQL("create table income(id INTEGER primary key autoincrement, amount DOUBLE, reason TEXT, time DOUBLE)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exists expense");
        database.execSQL("drop table if exists income");
        onCreate(database);
    }
    public void addExpense(double amount, String reason) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("reason", reason);
        values.put("time", (double) System.currentTimeMillis());
        database.insert("expense", null, values);
    }
    public void addIncome(double amount, String reason) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("reason", reason);
        values.put("time", (double) System.currentTimeMillis());
        database.insert("income", null, values);
    }
    public double showExpense() {
        double totalExpense = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("select amount from expense", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                totalExpense += cursor.getDouble(0);
            }
            cursor.close();
        }
        return totalExpense;
    }
    public double showIncome() {
        double totalIncome = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("select amount from income", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                totalIncome += cursor.getDouble(0);
            }
            cursor.close();
        }
        return totalIncome;
    }
    public Cursor showExpenseRecyclerView() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("select * from expense order by time desc", null);
    }
    public Cursor showIncomeRecyclerView() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("select * from income order by time desc", null);
    }
    public Cursor showOverallRecyclerView() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("select id, amount, reason, time, 'expense' as type from expense " +
                "UNION ALL " +
                "select id, amount, reason, time, 'income' as type from income " +
                "order by time desc", null);
    }
    public void deleteExpense(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("expense", "id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteIncome(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("income", "id = ?", new String[]{String.valueOf(id)});
    }
}