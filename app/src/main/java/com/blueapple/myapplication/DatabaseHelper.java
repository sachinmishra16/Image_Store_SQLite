package com.blueapple.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database_name";

    // Table Names
    private static final String DB_TABLE = "table_image";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " TEXT);";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // creating table
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // create new table
        onCreate(sqLiteDatabase);
    }

    public void addEntry( String name, byte[] image) throws SQLiteException {


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,    name);
        cv.put(KEY_IMAGE,   image);
        database.insert( DB_TABLE, null, cv );
    }

    public Image_Details getImage(String image_name)
    {
        SQLiteDatabase database=getReadableDatabase();

        Cursor cursor=database.query(DB_TABLE,new String[]{KEY_NAME,KEY_IMAGE},
                KEY_NAME+"=?",new String[]{image_name},null,null,null);

        if (cursor!=null)
        {
            cursor.moveToFirst();
           Image_Details details=new Image_Details();

            details.setName(cursor.getString(0));
            details.setImage(cursor.getBlob(1));
            return details;

        }
        else
        {
            return null;
        }




    }
}
