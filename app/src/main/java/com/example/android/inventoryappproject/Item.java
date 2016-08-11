package com.example.android.inventoryappproject;

/**
 * Created by da7th on 7/29/2016.
 */

public class Item {

    private String mName;
    private Integer mPrice;
    private Integer mQuantity;
    private String mPicture;

    public Item(String name, Integer price, Integer quantity, String picture) {
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mPicture = picture;
    }

    public String getName() {
        return mName;
    }

    public Integer getPrice() {
        return mPrice;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public String getPicture() {
        return mPicture;
    }
}
