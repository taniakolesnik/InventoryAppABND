package com.example.android.inventoryappabnd;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tetianakolesnik on 01/04/2018.
 */

public class ItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    Uri itemUri;

    @BindView(R.id.item_name_edit) EditText nameEditText;
    @BindView(R.id.item_price_edit) EditText priceEditText;
    @BindView(R.id.item_quantity_edit) EditText quantityEditText;
    @BindView(R.id.item_supplier_edit) EditText supplierEditText;
    @BindView(R.id.item_supplier_contact_edit) EditText supplierContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        itemUri = getIntent().getData();
        getLoaderManager().initLoader(3, null, this);
        ButterKnife.bind(this);
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
