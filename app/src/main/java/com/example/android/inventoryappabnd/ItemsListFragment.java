package com.example.android.inventoryappabnd;

import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    InventoryAdapter inventoryAdapter;
    public boolean inStock;

    @BindView(R.id.list_view)
    ListView listView;

    public ItemsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        inStock = getArguments().getBoolean("inStock");

        Log.i("mm",  String.valueOf(inStock));
        inventoryAdapter = new InventoryAdapter(getActivity(), null);
        listView.setAdapter(inventoryAdapter);
        getLoaderManager().initLoader(1,null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                Uri itemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                intent.setData(itemUri);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY};

        String selection = InventoryEntry.COLUMN_QUANTITY + "!=0";
        if (!inStock) {
            selection = InventoryEntry.COLUMN_QUANTITY + "=0";
        }

        return new android.support.v4.content.CursorLoader(getActivity(), InventoryEntry.CONTENT_URI,
                projection, selection, null, null);

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
