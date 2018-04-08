package com.example.android.inventoryappabnd;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tetianakolesnik on 01/04/2018.
 */

public class ItemDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = ItemDetailsActivity.class.getName();

    Uri itemUri;
    private static final int ITEM_LOADER_ID = 1;
    private int itemsToAdd = 0;
    private int itemsToSell = 0;
    private static final int MAX_ITEMS_TO_ADD = 10;

    @BindView(R.id.item_name_edit) EditText nameEditText;
    @BindView(R.id.item_price_edit) EditText priceEditText;
    @BindView(R.id.item_quantity_edit) EditText quantityEditText;
    @BindView(R.id.item_supplier_edit) EditText supplierEditText;
    @BindView(R.id.item_supplier_contact_edit) EditText supplierContactEditText;
    @BindView(R.id.sell_button) Button sellButton;
    @BindView(R.id.add_items_button) Button addButton;
    @BindView(R.id.spinner_item_to_sell) Spinner spinnerSell;
    @BindView(R.id.spinner_item_to_add) Spinner spinnerAdd;
    @BindView(R.id.phone_icon) ImageView phoneImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);

        itemUri = getIntent().getData();
        if (itemUri != null) {
            getLoaderManager().initLoader(ITEM_LOADER_ID, null, this);
            setTitle(getString(R.string.edit_activity_title));
        } else {
            setTitle(getString(R.string.add_activity_title));
            invalidateOptionsMenu();
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (itemUri==null){
            MenuItem menuItem = menu.findItem(R.id.deleteItem_menu);
            menuItem.setVisible(false);
        }
        return true;
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
                deleteRecord();
                // TODO add confirmation pop up;
                break;
        }
        return true;
    }

    private void deleteRecord() {
        getContentResolver().delete(itemUri, null, null);
        Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
        startActivity(intent);
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
            quantity = Integer.parseInt(quantityEditText.getText().toString());
            if (quantity < 0) {
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

        Log.i("Save Item ", String.valueOf(quantity));

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
            Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
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
        int quantity = 0;
        String supplierContact = "";

        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
            quantity = data.getInt(data.getColumnIndex(InventoryEntry.COLUMN_QUANTITY));
            int price = data.getInt(data.getColumnIndex(InventoryEntry.COLUMN_PRICE));
            String supplier = data.getString(data.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME));
            supplierContact = data.getString(data.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE));

            nameEditText.setText(name);
            priceEditText.setText(String.valueOf(price));
            quantityEditText.setText(String.valueOf(quantity));
            supplierEditText.setText(supplier);
            supplierContactEditText.setText(supplierContact);
        }

            prepareQuantityChange(quantity);
            preparePhoneCall(supplierContact);
    }

    private void preparePhoneCall(String phoneNumber) {
        final String mPhoneNumber = phoneNumber;

        if (!TextUtils.isEmpty(phoneNumber)){
            phoneImageView.setVisibility(View.VISIBLE);
        }
        phoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + mPhoneNumber));
                    startActivity(intent);
                } catch (SecurityException e) {
                    Log.e(LOG_TAG, "SecurityException ", e);
                }
            }
        });

    }

    private void prepareQuantityChange(int quantity) {

        final int currentQuantity = quantity;
        addButton.setVisibility(View.VISIBLE);
        spinnerAdd.setVisibility(View.VISIBLE);
        if (quantity != 0) {
            sellButton.setVisibility(View.VISIBLE);
            spinnerSell.setVisibility(View.VISIBLE);
        }

        /**
         *  Sell items
         */
        ArrayList spinnerSellArrayList;
        spinnerSellArrayList = new ArrayList<String>();
        for (int i = 1; i <= currentQuantity; i++){
            spinnerSellArrayList.add(i);
        }

        ArrayAdapter<String> arraySellAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerSellArrayList);
        arraySellAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSell.setAdapter(arraySellAdapter);
        spinnerSell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemsToSell = (Integer) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellItems(itemsToSell, currentQuantity);
            }
        });

        /**
         *  Add items
         */
        ArrayList spinnerAddArrayList;
        spinnerAddArrayList = new ArrayList<String>();
        for (int i = 1; i <= MAX_ITEMS_TO_ADD; i++){
            spinnerAddArrayList.add(i);
        }

        ArrayAdapter<String> arrayAddAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerAddArrayList);
        arrayAddAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdd.setAdapter(arrayAddAdapter);
        spinnerAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemsToAdd = (Integer) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems(itemsToAdd, currentQuantity);
            }
        });

    }

    private void sellItems(int itemsToSell, int currentQuantity) {
        int newQuantity = currentQuantity - itemsToSell;
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_QUANTITY, newQuantity);
        getContentResolver().update(itemUri, contentValues, null, null);
    }

    private void addItems(int itemsToAdd, int currentQuantity) {
        int newQuantity = currentQuantity + itemsToAdd;
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_QUANTITY, newQuantity);
        getContentResolver().update(itemUri, contentValues, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.cancelLoad();
    }
}

