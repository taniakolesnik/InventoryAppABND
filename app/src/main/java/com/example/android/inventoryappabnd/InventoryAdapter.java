package com.example.android.inventoryappabnd;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;


/**
 * Created by tetianakolesnik on 01/04/2018.
 */

class InventoryAdapter extends CursorAdapter{

    public static final String LOG_TAG = InventoryAdapter.class.getName();
    public InventoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.i(LOG_TAG, "InventoryAdapter newView");
        return LayoutInflater.from(context).inflate(R.layout.inventory_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.i(LOG_TAG, "InventoryAdapter bindView started " + cursor);
        String name = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
        int price = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY));

        Log.i(LOG_TAG, "InventoryAdapter bindView " + name+" "+price+" "+quantity);

        TextView nameView = view.findViewById(R.id.item_name_view);
        TextView detailsView = view.findViewById(R.id.details_view);

        nameView.setText(name);
        detailsView.setText(context.getString(R.string.quantity_list) + quantity
                + context.getString(R.string.price_list) + price);

    }
}
