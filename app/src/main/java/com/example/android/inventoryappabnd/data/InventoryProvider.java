package com.example.android.inventoryappabnd.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;

/**
 * Created by tetianakolesnik on 01/04/2018.
 */

public class InventoryProvider extends ContentProvider {

    public static final String LOG_TAG = InventoryProvider.class.getName();

    private InventoryDbHelper inventoryDbHelper;
    private static final int INVENTORY_ITEMS = 1;
    private static final int INVENTORY_ITEM_ID = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.INVENTORY_PATH, INVENTORY_ITEMS);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.INVENTORY_PATH + "/#", INVENTORY_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        inventoryDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        Log.i(LOG_TAG, "query match id = " + match );
        switch (match){
            case INVENTORY_ITEM_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(InventoryEntry.TABLE_NAME, projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INVENTORY_ITEMS:
                cursor = sqLiteDatabase.query(InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("match error");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getWritableDatabase();
        long newItemId = sqLiteDatabase.insert(InventoryEntry.TABLE_NAME, null, values);
        Uri newITemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, newItemId);
       //TODO turn on Toast.makeText(getContext(), "New item added. id: ",Toast.LENGTH_SHORT).show();
        return newITemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match){
            case INVENTORY_ITEM_ID:
                selection = InventoryEntry._ID + "?=";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ITEMS:
                rowsDeleted = sqLiteDatabase.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("match error");
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
