package com.example.android.inventoryappabnd;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tetianakolesnik on 01/04/2018.
 */

public class ItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    Uri itemUri;
    private static final int ITEM_LOADER_ID = 1;

    @BindView(R.id.item_name_edit) EditText nameEditText;
    @BindView(R.id.item_price_edit) EditText priceEditText;
    @BindView(R.id.item_quantity_edit) EditText quantityEditText;
    @BindView(R.id.item_supplier_edit) EditText supplierEditText;
    @BindView(R.id.item_supplier_contact_edit) EditText supplierContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);

        itemUri = getIntent().getData();
        if (itemUri != null) {
            getLoaderManager().initLoader(ITEM_LOADER_ID, null, this);
            setTitle(getString(R.string.edit_activity_title));
        } else {
            setTitle(getString(R.string.add_activity_title));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveItem_menu:
                saveItem();
                break;
            case R.id.deleteItem_menu:
                //TODO add delete item
                break;
        }
        return true;
    }

    private void saveItem() {

        boolean isReadyToBeSaved = true;
        int price = 0;
        int quantity = 0;

        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_cannot_be_empty));
            isReadyToBeSaved = false;
        }

        /**
         * Price
         */
        String priceString = priceEditText.getText().toString();
        try {
            price = Integer.parseInt(priceEditText.getText().toString());
            if (price < 0) {
                Log.i("Item Activity", "price is /" + price + "/");
                priceEditText.setError(getString(R.string.error_negative_value));
                isReadyToBeSaved = false;
            }
        } catch (NumberFormatException e) {
            if (TextUtils.isEmpty(priceString)) {
                priceEditText.setError(getString(R.string.error_cannot_be_empty));
            } else {
                priceEditText.setError(getString(R.string.error_not_number));
            }
            isReadyToBeSaved = false;
        }

        /**
         * Quantity
         */
        String quantityString = quantityEditText.getText().toString();
        try {
            price = Integer.parseInt(quantityEditText.getText().toString());
            if (price < 0) {
                quantityEditText.setError(getString(R.string.error_negative_value));
                isReadyToBeSaved = false;
            }
        } catch (NumberFormatException e) {
            if (TextUtils.isEmpty(quantityString)) {
                quantityEditText.setError(getString(R.string.error_cannot_be_empty));
            } else {
                quantityEditText.setError(getString(R.string.error_not_number));
            }
            isReadyToBeSaved = false;
        }

        String supplier = supplierEditText.getText().toString();
        if (TextUtils.isEmpty(supplier)) {
            supplierEditText.setError(getString(R.string.error_cannot_be_empty));
            isReadyToBeSaved = false;
        }

        String supplierContact = supplierContactEditText.getText().toString();

        if(isReadyToBeSaved){
            ContentValues contentValues = new ContentValues();
            contentValues.put(InventoryEntry.COLUMN_PRODUCT_NAME, name);
            contentValues.put(InventoryEntry.COLUMN_PRICE, price);
            contentValues.put(InventoryEntry.COLUMN_QUANTITY, quantity);
            contentValues.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplier);
            contentValues.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierContact);

            if (itemUri == null){
                getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);
            } else {
                getContentResolver().update(itemUri, contentValues, null, null);
            }
            Intent intent = new Intent(ItemActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, itemUri,
                null, // we want to get all info on this item
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
            int quantity = data.getInt(data.getColumnIndex(InventoryEntry.COLUMN_QUANTITY));
            int price = data.getInt(data.getColumnIndex(InventoryEntry.COLUMN_PRICE));
            String supplier = data.getString(data.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME));
            String supplierContact = data.getString(data.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE));

            nameEditText.setText(name);
            priceEditText.setText(String.valueOf(price));
            quantityEditText.setText(String.valueOf(quantity));
            supplierEditText.setText(supplier);
            supplierContactEditText.setText(supplierContact);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.cancelLoad();
    }
}
