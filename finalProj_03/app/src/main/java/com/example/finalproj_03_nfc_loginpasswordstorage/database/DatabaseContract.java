package com.example.finalproj_03_nfc_loginpasswordstorage.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static class CredentialObjects implements BaseColumns{
        public static final String TABLE_NAME = "credential_table";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_SERVICENAME = "service_name";
        public static final String COLUMN_NAME_SERVICE_USERNAME = "service_username";
        public static final String COLUMN_NAME_SERVICE_PASSWORD = "service_password";
    }
}
