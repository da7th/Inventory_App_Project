package com.example.android.inventoryappproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by da7th on 7/29/2016.
 */
public class DetailsFragment extends AppCompatActivity {


    Integer i;
    ItemDbHelper myDb;
    TextView idTV, nameTV, priceTV, quantityTV, pictureTV, soldTV;
    Button sellB, restockB, deleteB, stockB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item);

        idTV = (TextView) findViewById(R.id.detail_product_id_text_view);
        nameTV = (TextView) findViewById(R.id.detail_product_name_text_view);
        priceTV = (TextView) findViewById(R.id.detail_product_price_text_view);
        quantityTV = (TextView) findViewById(R.id.detail_product_quantity_text_view);
        pictureTV = (TextView) findViewById(R.id.detail_product_picture_text_view);
        soldTV = (TextView) findViewById(R.id.detail_sold_text_view);

        sellB = (Button) findViewById(R.id.sell_details_button);
        restockB = (Button) findViewById(R.id.restock_details_button);
        deleteB = (Button) findViewById(R.id.delete_details_button);
        stockB = (Button) findViewById(R.id.stock_details_button);

        myDb = new ItemDbHelper(this);

        Log.e("the value of i: ", "this stage");

        Intent thisIntent = getIntent();
        ArrayList<Integer> integers = thisIntent.getIntegerArrayListExtra("integerList");
        Integer i = integers.get(0);

        Log.e("the value of i: ", i.toString());
        Cursor res = myDb.readAllData();

        res.moveToPosition(i);

        int count = res.getCount();
        Log.e("the value of getCount: ", "= " + count);
        String idTVData;
        String nameTVData;
        Integer priceTVData;
        Integer quantityTVData;
        String pictureTVData;
        Integer soldTVData;

        if (res.getCount() > 0) {

            idTV.setText(res.getString(0));
            nameTV.setText(res.getString(1));
            priceTV.setText(res.getString(2));
            quantityTV.setText(res.getString(3));
            pictureTV.setText(res.getString(4));
            soldTV.setText(res.getString(5));

            idTVData = res.getString(0);
            nameTVData = res.getString(1);
            priceTVData = Integer.parseInt(res.getString(2));
            quantityTVData = Integer.parseInt(res.getString(3));
            pictureTVData = res.getString(4);
            soldTVData = Integer.parseInt(res.getString(5));

            restock(nameTVData);
            sell(idTVData, nameTVData, priceTVData, quantityTVData, pictureTVData, soldTVData);
            deleteProduct(idTVData);
            stock(idTVData, nameTVData, priceTVData, quantityTVData, pictureTVData, soldTVData);

        } else {
            Toast.makeText(this, "nothing in the given resource", Toast.LENGTH_SHORT);
        }
    }

    public void sell(final String idTVData, final String nameTVData, final Integer priceTVData,
                     final Integer quantityTVData, final String pictureTVData, final Integer
                             soldTVData) {
        sellB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Integer quantityDecrement = quantityTVData;
                        Integer soldIncrement = soldTVData;

                        if (quantityTVData > 0) {

                            quantityDecrement = quantityTVData - 1;
                            soldIncrement = soldTVData + 1;
                            myDb.updateData(idTVData, nameTVData, priceTVData, quantityDecrement,
                                    pictureTVData, soldIncrement);
                        }

                        myDb.updateData(idTVData, nameTVData, priceTVData, quantityDecrement,
                                pictureTVData, soldIncrement);

                        Intent returnToMainIntent = new Intent(DetailsFragment.this,
                                MainActivity.class);
                        startActivity(returnToMainIntent);
                    }
                }
        );
    }

    public void restock(final String productName) {
        restockB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("restock method", "restocking");

                        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                        mailIntent.setData(Uri.parse("mailto:"));
                        mailIntent.putExtra(Intent.EXTRA_EMAIL, "Supplier@gmail.com");
                        mailIntent.putExtra(Intent.EXTRA_SUBJECT, ("Restock order for the product: "
                                + productName));
                        mailIntent.putExtra(Intent.EXTRA_TEXT, "Restock order for the product: "
                                + productName + "the quantity required is: ###");
                        if (mailIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mailIntent);
                        }
                    }
                }
        );
    }

    public void deleteProduct(final String idTVData) {
        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsFragment.this);
                builder.setMessage("Are you sure you wih to delete this product permanently?"
                ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("delete button", "cancelled");
                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDb.deleteData(idTVData);

                        Intent returnToMainIntent = new Intent(DetailsFragment.this,
                                MainActivity.class);
                        startActivity(returnToMainIntent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void stock(final String idTVData, final String nameTVData, final Integer priceTVData,
                      final Integer quantityTVData, final String pictureTVData, final Integer
                              soldTVData) {
        stockB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Integer quantityDecrement = quantityTVData + 100;

                        myDb.updateData(idTVData, nameTVData, priceTVData, quantityDecrement,
                                pictureTVData, soldTVData);

                        Intent returnToMainIntent = new Intent(DetailsFragment.this,
                                MainActivity.class);
                        startActivity(returnToMainIntent);
                    }
                }
        );
    }
}
