package com.example.bursel.databaseapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DairysProvider extends ContentProvider {
    DairysDBHelper mDbHelper;
    private static final int MULTYPLE_WORDS=1;
    private static final int SINGLE_WORDS=2;

    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(Dairys.AUTHORITY,Dairys.Dairy.PATH_SINGLE,SINGLE_WORDS);
        uriMatcher.addURI(Dairys.AUTHORITY,Dairys.Dairy.PATH_MULTIPLE,MULTYPLE_WORDS);
    }
    public DairysProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)){
            case MULTYPLE_WORDS:
                count=db.delete(Dairys.Dairy.TABLE_NAME,selection,selectionArgs);
                break;
            case SINGLE_WORDS:
                String whereClause=Dairys.Dairy._ID+"="+uri.getPathSegments().get(1);
                count=db.delete(Dairys.Dairy.TABLE_NAME,whereClause,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch(uriMatcher.match(uri)){
            case MULTYPLE_WORDS:
                return Dairys.Dairy.MINE_TYPE_MULTYPLE;
            case SINGLE_WORDS:
                return Dairys.Dairy.MINE_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:"+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        long id=db.insert(Dairys.Dairy.TABLE_NAME,null,values);
        if(id>0){
            Uri newUri= ContentUris.withAppendedId(Dairys.Dairy.CONTENT_URI,id);
            getContext().getContentResolver().notifyChange(newUri,null);
            return newUri;
        }
        throw new SQLException("Failed to insert row into"+uri);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();

        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        qb.setTables(Dairys.Dairy.TABLE_NAME);

        switch(uriMatcher.match(uri)){
            case MULTYPLE_WORDS:
                return db.query(Dairys.Dairy.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
            case SINGLE_WORDS:
                qb.appendWhere(Dairys.Dairy._ID+"="+uri.getPathSegments().get(1));
            default:
                throw new IllegalArgumentException("Unkonwn Uri:"+uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)){
            case MULTYPLE_WORDS:
                count=db.update(Dairys.Dairy.TABLE_NAME,values,selection,selectionArgs);
                break;
            case SINGLE_WORDS:
                String segment=uri.getPathSegments().get(1);
                count=db.update(Dairys.Dairy.TABLE_NAME,values,Dairys.Dairy._ID+"="+segment,selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Unkonwn Uri:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
