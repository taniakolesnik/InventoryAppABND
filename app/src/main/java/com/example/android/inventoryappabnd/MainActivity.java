package com.example.android.inventoryappabnd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryappabnd.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int TABLE_RECORDS = 10;
    InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertTestRecords();
        readTestRecords();
       // deleteTestRecords();
    }

    private void insertTestRecords() {

        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getWritableDatabase();

        for (int i = 0; i <= TABLE_RECORDS; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(InventoryEntry.COLUMN_PRODUCT_NAME, getString(R.string.product_name) + i);
            contentValues.put(InventoryEntry.COLUMN_PRICE, i + i);
            contentValues.put(InventoryEntry.COLUMN_QUANTITY, i);
            contentValues.put(InventoryEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier_name) + i);
            contentValues.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, i + "-" + i + i + i + i + i + "-" + i + i);
            sqLiteDatabase.insert(InventoryEntry.TABLE_NAME, null, contentValues);
        }
        sqLiteDatabase.close();
    }

    private void readTestRecords() {

        String[] projection = {
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY};
        Log.i(LOG_TAG, "projection = " + projection );

        Cursor cursor = getContentResolver().query(InventoryEntry.CONTENT_URI,
                projection, null, null, null);

        Log.i(LOG_TAG, "getContentResolver = " + InventoryEntry.CONTENT_URI +
                projection );
            Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cursor));
            cursor.close();
    }

    private void deleteTestRecords() {
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getReadableDatabase();
        sqLiteDatabase.delete(InventoryEntry.TABLE_NAME, null, null);
        Log.i(LOG_TAG, getString(R.string.test_records_deleted_message));
    }
}
