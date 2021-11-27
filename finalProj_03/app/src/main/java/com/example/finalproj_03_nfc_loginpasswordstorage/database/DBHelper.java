package com.example.finalproj_03_nfc_loginpasswordstorage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.finalproj_03_nfc_loginpasswordstorage.credential.CredentialObject;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userpassstorage.db";

    //create strings to init database
    //create strings to delete db table
    private static final String SQL_CREATE_CREDENTIAL_TABLE = "CREATE TABLE " + DatabaseContract.CredentialObjects.TABLE_NAME +
            "(" + DatabaseContract.CredentialObjects._ID + " INTEGER PRIMARY KEY, " +
            DatabaseContract.CredentialObjects.COLUMN_NAME_USERNAME + " TEXT, " +
            DatabaseContract.CredentialObjects.COLUMN_NAME_PASSWORD + " TEXT, " +
            DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICENAME + " TEXT, " +
            DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_USERNAME + " TEXT, " +
            DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_PASSWORD + " TEXT)";
    private static final String SQL_DELETE_CREDENTIAL_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.CredentialObjects.TABLE_NAME;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CREDENTIAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_CREDENTIAL_TABLE);
    }

    private CredentialObject mapCursortoCredential(Cursor resultCursor){
        CredentialObject newCredentialObject = new CredentialObject();
        int userName = resultCursor.getColumnIndexOrThrow(DatabaseContract.CredentialObjects.COLUMN_NAME_USERNAME);
        int password = resultCursor.getColumnIndexOrThrow(DatabaseContract.CredentialObjects.COLUMN_NAME_PASSWORD);
        int serviceName = resultCursor.getColumnIndexOrThrow(DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICENAME);
        int serviceUserName = resultCursor.getColumnIndexOrThrow(DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_USERNAME);
        int servicePassword = resultCursor.getColumnIndexOrThrow(DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_PASSWORD);

        newCredentialObject.setUserName(resultCursor.getString(userName));
        newCredentialObject.setPassword(resultCursor.getString(password));
        newCredentialObject.setServiceName(resultCursor.getString(serviceName));
        newCredentialObject.setServiceUserName(resultCursor.getString(serviceUserName));
        newCredentialObject.setServicePassword(resultCursor.getString(servicePassword));

        return newCredentialObject;
    }

    public List<CredentialObject> getCredentialsByUserPass(String userName, String password) {
        List<CredentialObject> existingCredentialObjects = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                DatabaseContract.CredentialObjects._ID,
                DatabaseContract.CredentialObjects.COLUMN_NAME_USERNAME,
                DatabaseContract.CredentialObjects.COLUMN_NAME_PASSWORD,
                DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICENAME,
                DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_USERNAME,
                DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_PASSWORD
        };
        //string selection = DatabaseContract.CredentialObjects.COLUMNE_NAME_USERNAME + " = ? AND " + DatabaseContract.CredentailObjects.COLUMN_NAME_PASSWORD + " = ?";
        String selections = DatabaseContract.CredentialObjects.COLUMN_NAME_USERNAME + " = ? AND " + DatabaseContract.CredentialObjects.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = {userName, password};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor resultsCursor = db.query(DatabaseContract.CredentialObjects.TABLE_NAME, columns, selections, selectionArgs, groupBy, having, orderBy);

        while (resultsCursor.moveToNext()){
            CredentialObject singleCredential = mapCursortoCredential(resultsCursor);
            existingCredentialObjects.add(singleCredential);
        }
        return existingCredentialObjects;
    }

    public void addCredentialObject(CredentialObject newCredentialObject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CredentialObjects.COLUMN_NAME_USERNAME, newCredentialObject.getUserName());
        values.put(DatabaseContract.CredentialObjects.COLUMN_NAME_PASSWORD, newCredentialObject.getPassword());
        values.put(DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICENAME, newCredentialObject.getServiceName());
        values.put(DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_USERNAME, newCredentialObject.getServiceUserName());
        values.put(DatabaseContract.CredentialObjects.COLUMN_NAME_SERVICE_PASSWORD, newCredentialObject.getServicePassword());
        db.insert(DatabaseContract.CredentialObjects.TABLE_NAME, null, values);
    }
}
