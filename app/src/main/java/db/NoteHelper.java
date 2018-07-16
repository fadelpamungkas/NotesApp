package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import entity.Note;

import static db.DatabaseContract.TABLE_NOTE;
import static db.DatabaseContract.NoteColumns.*;

public class NoteHelper {
    private static String DATABASE_TABLE = TABLE_NOTE;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public NoteHelper(Context context){
        this.context = context;
    }

    public NoteHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public ArrayList<Note> query(){
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null);
        cursor.moveToFirst();
        Note note;
        if(cursor.getCount() > 0){
            do {
                note = new Note();

                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public long insert(Note note){
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, note.getTitle());
        initialValues.put(DESCRIPTION, note.getDescription());
        initialValues.put(DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void update(Note note){
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());
        database.update(DATABASE_TABLE, args, _ID + "= '" + note.getId() + "'", null);
    }

    public void delete(int id){
        database.delete(TABLE_NOTE, _ID + " ='" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
