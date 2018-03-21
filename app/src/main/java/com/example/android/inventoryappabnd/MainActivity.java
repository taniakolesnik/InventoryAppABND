package com.example.android.inventoryappabnd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.inventoryappabnd.InventoryContract.InventoryEntry;

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
        deleteTestRecords();
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

        String[] projection = {InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE};

        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        try {
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumnIndex);
                String productName = cursor.getString(nameColumnIndex);
                String price = cursor.getString(priceColumnIndex);
                String quantity = cursor.getString(quantityColumnIndex);
                String supplierName = cursor.getString(supplierNameColumnIndex);
                String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

                Log.i(LOG_TAG, "id " + id
                        + "; productName " + productName
                        + "; price " + price
                        + "; quantity " + quantity
                        + "; supplierName " + supplierName
                        + "; supplierPhone " + supplierPhone);
            }
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }
    }

    private void deleteTestRecords() {
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getReadableDatabase();
        sqLiteDatabase.delete(InventoryEntry.TABLE_NAME, null, null);
        Log.i(LOG_TAG, getString(R.string.test_records_deleted_message));
    }
}
