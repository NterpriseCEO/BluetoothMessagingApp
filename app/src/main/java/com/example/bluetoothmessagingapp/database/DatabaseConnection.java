/***********************************************************************
 *
 *  This code does two main things
 *    1) Contains an onCreate method which contains the database creation code for a sample database (builds up the SQL "create table" commmands
 *    2) Contains an onUpgrade method - which in this example isn't used.
 *
 *
 ***************************************************************************/
package com.example.bluetoothmessagingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnection extends SQLiteOpenHelper
{

    // These are the names of the columns the table will contain. Could make these private
    // and use getters so that other classes can access them, but, as they are "final", this removes
    // the security risk that encapsulation (privacy/ getters/ setters) protects against

    public static final String KEY_ROWID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_BLUETOOTH_ID = "bluetooth_id";
    public static final String KEY_LOCAL_USER = "local_user";

    public static final String DATABASE_NAME = "UserData";
    public static final String USER_DATABASE_TABLE = "Users";
    public static final int DATABASE_VERSION = 1;

    // This is the string containing the SQL database create statement
    private static final String DATABASE_CREATE =
            "create table " + USER_DATABASE_TABLE  +
                    " ("+KEY_ROWID+" integer primary key autoincrement, " +
                    KEY_USERNAME+" text not null, " +
                    KEY_BLUETOOTH_ID+" text not null, "  +
                    KEY_LOCAL_USER+" integer not null);";

    public DatabaseConnection(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

