package com.example.android.inventoryappabnd;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventoryappabnd.data.InventoryContract.InventoryEntry;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public boolean inStock;
    InventoryAdapter inventoryAdapter;
    @BindView(R.id.list_view) ListView listView;
    @BindView(R.id.empty_view) TextView emptyView;

    public ItemsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        inStock = getArguments().getBoolean(getString(R.string.in_stock));

        if (!inStock) {
            emptyView.setText(R.string.empty_list_for_soldout);
        } else {
            emptyView.setText(R.string.empty_list_for_available);
        }

        listView.setEmptyView(emptyView);

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

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });

        return rootView;
    }

    private void insertItem() {
        Intent intent = new Intent(getContext(), ItemDetailsActivity.class);
        startActivity(intent);
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
