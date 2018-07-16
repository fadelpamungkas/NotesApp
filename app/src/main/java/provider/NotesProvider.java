package provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import db.DatabaseContract;
import db.NoteHelper;

import static db.DatabaseContract.AUTHORITY;
import static db.DatabaseContract.CONTENT_URI;

public class NotesProvider extends ContentProvider {
    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //content://com.del.mynotesapp/note
        uriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_NOTE, NOTE);

        //content://com.del.mynotesapp/note/id
        uriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_NOTE + "/#", NOTE_ID);
    }

    private NoteHelper noteHelper;

    @Override
    public boolean onCreate() {
        noteHelper = new NoteHelper(getContext());
        noteHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String strings2) {
        Cursor cursor;
        switch(uriMatcher.match(uri)){
            case NOTE:
                cursor = noteHelper.queryProvider();
                break;
            case NOTE_ID:
                cursor = noteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;

        switch (uriMatcher.match(uri)){
            case NOTE:
                added = noteHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int deleted;

        switch (uriMatcher.match(uri)){
            case NOTE_ID:
                deleted = noteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String s, String[] strings) {
        int updated;

        switch (uriMatcher.match(uri)){
            case NOTE_ID:
                updated = noteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }
}
