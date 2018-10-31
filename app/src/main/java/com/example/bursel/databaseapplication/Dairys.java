package com.example.bursel.databaseapplication;

import android.provider.BaseColumns;
import android.net.Uri;

public class Dairys {
    public static final String AUTHORITY="com.example.bursel.wordsprovider";

    public Dairys(){}

    public static abstract class Dairy implements BaseColumns{
        public static final String TABLE_NAME="dairy";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_BODY="body";

        public static final String MIME_DIR_PREFIX="vnd.android.cursor.dir";
        public static final String MIME_ITEM_PREFIX="vnd.android.cursor.item";
        public static final String MIME_ITEM="vnd.bursel.dairy";

        public static final String MINE_TYPE_SINGLE=MIME_ITEM_PREFIX+"/"+MIME_ITEM;
        public static final String MINE_TYPE_MULTYPLE=MIME_DIR_PREFIX+"/"+MIME_ITEM;

        public static final String PATH_SINGLE="word/#";
        public static final String PATH_MULTIPLE="dairy";

        public static final String CONTENT_URI_STRING="content://"+AUTHORITY+"/"+PATH_MULTIPLE;
        public static final Uri CONTENT_URI=Uri.parse(CONTENT_URI_STRING);
    }
}
