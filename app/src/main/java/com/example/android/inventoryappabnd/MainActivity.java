package com.example.android.inventoryappabnd;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int TABLE_RECORDS = 10;
    InventoryAdapter inventoryAdapter;

    @BindView(R.id.list_view) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //insertTestRecords();

        inventoryAdapter = new InventoryAdapter(this, null);
        listView.setAdapter(inventoryAdapter);
        getLoaderManager().initLoader(1,null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNew_menu:
                break;
            case R.id.deleteAll_menu:
                deleteAllRecords();
                break;
        }
        return true;
    }


    private void deleteAllRecords() {
        getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
    }

    private void insertTestRecords() {

        for (int i = 10; i <= TABLE_RECORDS*4; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(InventoryEntry.COLUMN_PRODUCT_NAME, getString(R.string.product_name) + i);
            contentValues.put(InventoryEntry.COLUMN_PRICE, i + i);
            contentValues.put(InventoryEntry.COLUMN_QUANTITY, i);
            contentValues.put(InventoryEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier_name) + i);
            contentValues.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, i + "-" + i + i + i + i + i + "-" + i + i);
            getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY};
        return new CursorLoader (this, InventoryEntry.CONTENT_URI,
                projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        inventoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        inventoryAdapter.swapCursor(null);
    }
}
