package com.example.android.inventoryappabnd;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;


/**
 * Created by tetianakolesnik on 01/04/2018.
 */

class InventoryAdapter extends CursorAdapter{

    public InventoryAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
        int price = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY));

        TextView nameView = view.findViewById(R.id.item_name_view);
        TextView detailsView = view.findViewById(R.id.details_view);
        Button button = view.findViewById(R.id.sell_one_button);

        nameView.setBackgroundResource(R.color.colorName);
        detailsView.setBackgroundResource(R.color.colorDetails);

        nameView.setText(name);
        detailsView.setText(context.getString(R.string.quantity_list) + quantity + " "
                + context.getString(R.string.price_list) + price);

        final Uri itemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
        final int quantityAfterOneItemSell = quantity - 1;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(InventoryEntry.COLUMN_QUANTITY, quantityAfterOneItemSell);
                context.getContentResolver().update(itemUri, contentValues, null, null);
            }
        });



    }

}
