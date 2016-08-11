package com.example.android.inventoryappproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by da7th on 7/29/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private ItemAdapter adapter;
    private Context mainContext;

    public ItemAdapter(MainActivity context, ArrayList<Item> resource) {

        super(context, 0, resource);
        this.mainContext = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView sellBtn = (TextView) listView.findViewById(R.id.sell_button);

        Item currentItem = getItem(position);

        TextView nameTV = (TextView) listView.findViewById(R.id.product_name_text_view);
        nameTV.setText(currentItem.getName());

        TextView priceTV = (TextView) listView.findViewById(R.id.price_text_view);
        priceTV.setText((currentItem.getPrice()).toString());

        final TextView quantityTV = (TextView) listView.findViewById(R.id.quantity_text_view);
        quantityTV.setText((currentItem.getQuantity()).toString());

        TextView pictureTV = (TextView) listView.findViewById(R.id.picture_text_view);
        pictureTV.setText(currentItem.getPicture());

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Selling 1", "sold 1");

                ItemDbHelper myDb;
                myDb = new ItemDbHelper(getContext());
                Cursor res = myDb.readAllData();

                res.moveToPosition(position);

                String idTVData;
                String nameTVData;
                Integer priceTVData;
                Integer quantityTVData;
                String pictureTVData;
                Integer soldTVData;

                idTVData = res.getString(0);
                nameTVData = res.getString(1);
                priceTVData = Integer.parseInt(res.getString(2));
                quantityTVData = Integer.parseInt(res.getString(3));
                pictureTVData = res.getString(4);
                soldTVData = Integer.parseInt(res.getString(5));

                Integer quantityDecrement = quantityTVData;
                Integer soldIncrement = soldTVData;

                if (quantityTVData > 0) {

                    quantityDecrement = quantityTVData - 1;
                    soldIncrement = soldTVData + 1;
                    myDb.updateData(idTVData, nameTVData, priceTVData, quantityDecrement,
                            pictureTVData, soldIncrement);
                    Intent mainIntent = new Intent(mainContext, MainActivity.class);
                    mainContext.startActivity(mainIntent);
                }
            }
        });

        return listView;
    }
}
