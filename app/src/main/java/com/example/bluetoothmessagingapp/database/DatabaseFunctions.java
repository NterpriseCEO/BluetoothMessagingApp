/***********************************************************************
 *
 *  This code does two main things
 *    1) It has a custom method "open"  - which calls the (Extended) SQLLiteOpenHelper class and
 *       creates the database
 *    2) It implements various S/I/U/D  (i.e. CRUD) methods to use the database
 *
 *
 ***************************************************************************/

package com.example.bluetoothmessagingapp.database;
import static com.example.bluetoothmessagingapp.database.DatabaseConnection.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseFunctions  {
    Context context;
    private DatabaseConnection connection;
    private SQLiteDatabase db;


    public DatabaseFunctions(Context context) {
        this.context = context;

    }

    public DatabaseFunctions open() throws SQLException {
        connection = new DatabaseConnection(context);
        db = connection.getWritableDatabase();
        return this;
    }

    //---closes the database--- any activity that uses the dB will need to do this
    public void close()
    {
        connection.close();
    }

    public long insertUser(String username, int localUser) {
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_LOCAL_USER, localUser);
        values.put(KEY_BLUETOOTH_ID,"00:00:00:00:00:00");
        return db.insert(USER_DATABASE_TABLE, null, values);
    }

    //---insert a contact person into the database---
//    public long insertRecord(String taskName, String taskDesc, int complete)
//    {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_TASKNAMME, taskName);
//        initialValues.put(KEY_TASKDESC, taskDesc);
//        initialValues.put(KEY_COMPLETE, complete);
//        return myDatabase.insert(DATABASE_TABLE, null, initialValues);
//    }

    //---deletes a particular contact person---
//    public boolean deleteRecord(long rowId)
//    {
//        // delete statement.  If any rows deleted (i.e. >0), returns true
//        return myDatabase.delete(DATABASE_TABLE, KEY_ROWID +
//                "=" + rowId, null) > 0;
//    }

    //---retrieves all the rows ---
//    public Cursor getAllRecords()
//    {
//        return myDatabase.query(DATABASE_TABLE, new String[] {
//                        KEY_ROWID,
//                        KEY_TASKNAMME,
//                        KEY_TASKDESC,
//                        KEY_COMPLETE},
//                null,
//                null,
//                null,
//                null,
//                null);
//    }

    //---retrieves the local user (the user associated with this phone)
    public Cursor getLocalUser() throws SQLException
    {
        Cursor cursor =
                db.query(true, USER_DATABASE_TABLE, new String[] {
                    KEY_USERNAME,
                },
                KEY_LOCAL_USER + "= 1",
                null,
                null,
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //---updates the username of the local user
    public boolean updateLocalUser(String newUsername)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_USERNAME, newUsername);
        return db.update(USER_DATABASE_TABLE, args,
                KEY_LOCAL_USER + "= 1", null) > 0;
    }

}


