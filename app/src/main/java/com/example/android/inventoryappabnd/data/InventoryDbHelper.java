package com.example.android.inventoryappabnd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;

/**
 * Created by tetianakolesnik on 21/03/2018.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "InventoryRecords";
    private final static int DATABASE_VERSION = 1;
    private static final String INTEGER_DEFAULT_0 = " INTEGER DEFAULT 0";
    private static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TEXT = " TEXT";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private final String COMMA = " , ";
    private final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE "
            + InventoryEntry.TABLE_NAME + "( "
            + InventoryEntry._ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA
            + InventoryEntry.COLUMN_PRODUCT_NAME + TEXT + COMMA
            + InventoryEntry.COLUMN_PRICE + INTEGER_DEFAULT_0 + COMMA
            + InventoryEntry.COLUMN_QUANTITY + INTEGER_DEFAULT_0 + COMMA
            + InventoryEntry.COLUMN_SUPPLIER_NAME + TEXT_NOT_NULL + COMMA
            + InventoryEntry.COLUMN_SUPPLIER_PHONE + TEXT + ")";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
