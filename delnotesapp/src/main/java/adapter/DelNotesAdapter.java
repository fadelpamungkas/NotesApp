package adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.del.delnotesapp.R;

import static com.del.delnotesapp.DatabaseContract.*;
import static com.del.delnotesapp.DatabaseContract.NoteColumns.*;

public class DelNotesAdapter extends CursorAdapter {

    public DelNotesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_title);
            TextView tvDate = (TextView)view.findViewById(R.id.tv_date);
            TextView tvDescription = (TextView)view.findViewById(R.id.tv_description);

            tvTitle.setText(getColumnString(cursor, TITLE));
            tvDate.setText(getColumnString(cursor, DATE));
            tvDescription.setText(getColumnString(cursor, DESCRIPTION));
        }
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }
}
