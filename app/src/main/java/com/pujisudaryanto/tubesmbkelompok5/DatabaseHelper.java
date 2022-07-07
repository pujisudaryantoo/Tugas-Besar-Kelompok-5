package com.pujisudaryanto.tubesmbkelompok5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "db_pengeluaran.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE tb_pengeluaran (\n" +
                "    id_pengeluaran INTEGER       PRIMARY KEY AUTOINCREMENT,\n" +
                "    title          VARCHAR (255),\n" +
                "    date           DATE,\n" +
                "    fee            INT,\n" +
                "    category       VARCHAR (255) \n" +
                ");";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
//        sql = "INSERT INTO tb_pengeluaran (id_pengeluaran, title, date, fee, category) VALUES ('1', 'Traktir Temen', '2019-10-13', '225000','pengeluaran');";
//        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
