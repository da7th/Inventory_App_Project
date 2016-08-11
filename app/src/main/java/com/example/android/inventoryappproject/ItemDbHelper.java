package com.example.android.inventoryappproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by da7th on 7/29/2016.
 */
public class ItemDbHelper extends SQLiteOpenHelper {

    //tag used to find errors in this particular file
    private String DB_LOG_TAG = "DATABASE_HELPER";

    public ItemDbHelper(Context context) {
        super(context, itemSQLContract.TABLE_NAME, null, 1);
    }

    //oncreate will create the table with the given columns and value datatypes to be in each column
    //the _ID will be the primary key parameter and will auto increment
    @Override
    public void onCreate(SQLiteDatabase itemDb) {

        itemDb.execSQL("create table " + itemSQLContract.TABLE_NAME + " (_ID INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, ITEM TEXT, PRICE INTEGER, QUANTITY INTEGER, " +
                "IMAGE TEXT, SOLD INTEGER)");
        Log.v(DB_LOG_TAG, "the onCreate method has been called the table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase itemDb, int i, int i1) {
        //upon the need to upgrade, the entire table is deleted
        deleteAllData(itemDb);
        //upon the need to upgrade, the entire table is dropped if it still exists
        itemDb.execSQL("DROP TABLE IF EXISTS " + itemSQLContract.TABLE_NAME);
        //and here it is recreated by calling the onCreate method again
        onCreate(itemDb);
        Log.v(DB_LOG_TAG, "the onUpgrade method has been called and the table has been dropped " +
                "and recreated");
    }

    public boolean insertData(String item, Integer price, Integer quantity, String image, Integer
            sold) {

        //create a writable instance of the database
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v(DB_LOG_TAG, "database is set to get Writable");

        //we use a variable of content values to take in the values to be set
        ContentValues contentValues = new ContentValues();
        contentValues.put(itemSQLContract.COL_1, item);
        contentValues.put(itemSQLContract.COL_2, price);
        contentValues.put(itemSQLContract.COL_3, quantity);
        contentValues.put(itemSQLContract.COL_4, image);
        contentValues.put(itemSQLContract.COL_5, sold);

        //the insert function takes the table name and the content values set and sets it to the
        //table, in order to check that the insert funcation was successful it must not return -1
        //by placing the result in a long variable we can check for the -1 using an if statement
        long result = db.insert(itemSQLContract.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor readAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        //a cursor object allows for random read and write, this code allows us to store all of the
        // table data into the cursor res and return it to where the method was called to be shown
        Cursor res = db.query(itemSQLContract.TABLE_NAME, new String[]{itemSQLContract._ID,
                itemSQLContract.COL_1, itemSQLContract.COL_2, itemSQLContract.COL_3,
                itemSQLContract.COL_4, itemSQLContract.COL_5}, null, null, null, null, null);
        return res;
    }

    public Boolean updateData(String id, String item, Integer price, Integer quantity, String image,
                              Integer sold) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(itemSQLContract._ID, id);
        contentValues.put(itemSQLContract.COL_1, item);
        contentValues.put(itemSQLContract.COL_2, price);
        contentValues.put(itemSQLContract.COL_3, quantity);
        contentValues.put(itemSQLContract.COL_4, image);
        contentValues.put(itemSQLContract.COL_5, sold);

        //the following method call will replace the row based on the unique identifier which in
        // this case is the id
        db.update(itemSQLContract.TABLE_NAME, contentValues, "_ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        //the following deleted a row of data according to the given id
        return db.delete(itemSQLContract.TABLE_NAME, "_ID = ?", new String[]{id});
    }

    public Integer deleteAllData(SQLiteDatabase db) {
        db = this.getWritableDatabase();

        //the following will delete all the data in the table
        return db.delete(itemSQLContract.TABLE_NAME, "select * from " +
                itemSQLContract.TABLE_NAME, new String[]{"0"});
    }

    public Cursor readData(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //a cursor object allows for random read and write, this code allows us to store all of the
        // table data into the cursor res and return it to where the method was called to be shown
        Cursor res = db.query(itemSQLContract.TABLE_NAME, new String[]{itemSQLContract._ID,
                        itemSQLContract.COL_1,
                        itemSQLContract.COL_2, itemSQLContract.COL_3, itemSQLContract.COL_4,
                        itemSQLContract.COL_5}, itemSQLContract._ID + " like" + "'%" + id + "%'", null,
                null, null, null);

        return res;
    }

    //the contract class by which to setup the table
    //basecolumn will setup two extra columns for _ID and _count
    public static abstract class itemSQLContract implements BaseColumns {

        //table name and column names
        public static final String TABLE_NAME = "inventory";
        public static final String COL_1 = "item";
        public static final String COL_2 = "price";
        public static final String COL_3 = "quantity";
        public static final String COL_4 = "image";
        public static final String COL_5 = "sold";
    }
}