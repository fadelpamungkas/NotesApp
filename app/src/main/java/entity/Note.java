package entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import db.DatabaseContract;

import static db.DatabaseContract.*;
import static db.DatabaseContract.NoteColumns.*;

public class Note implements Parcelable {
    private int id;
    private String title, description, date;

    public Note(){
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
    }

    public Note(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, NoteColumns.TITLE);
        this.description = getColumnString(cursor, NoteColumns.DESCRIPTION);
        this.date = getColumnString(cursor, NoteColumns.DATE);
    }
}
