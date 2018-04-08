package com.example.android.inventoryappabnd.data;

import android.net.Uri;
import android.provider.BaseColumns;
/**
 * Created by tetianakolesnik on 21/03/2018.
 */

public class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryappabnd";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String INVENTORY_PATH = "inventory";
    public InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, INVENTORY_PATH);

        public static final String TABLE_NAME = "Inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "productName";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_SUPPLIER_PHONE = "supplierPhoneNumber";
    }
}
