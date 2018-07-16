package adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.del.mynotesapp.CustomOnItemClickListener;
import com.del.mynotesapp.FormAddUpdateActivity;
import com.del.mynotesapp.R;

import entity.Note;

import static db.DatabaseContract.CONTENT_URI;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewholder> {
    private Cursor listNotes;
    private Activity activity;

    public NoteAdapter(Activity activity){
        this.activity = activity;
    }

    public void setListNotes(Cursor listNotes){
        this.listNotes = listNotes;
    }

    @Override
    public NoteViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewholder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewholder holder, int position) {
        final Note note = getItem(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
        holder.tvDate.setText(note.getDate());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormAddUpdateActivity.class);

                Uri uri = Uri.parse(CONTENT_URI + "/" + note.getId());
                intent.setData(uri);
                activity.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        if (listNotes == null) return 0;
        return listNotes.getCount();
    }

    private Note getItem(int position){
        if (!listNotes.moveToPosition(position)){
            throw new IllegalStateException("Position Invalid");
        }

        return new Note(listNotes);
    }

    public class NoteViewholder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDescription, tvDate;
        CardView cvNote;

        public NoteViewholder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_description);
            tvDate = (TextView)itemView.findViewById(R.id.tv_date);
            cvNote = (CardView)itemView.findViewById(R.id.cv_note);
        }
    }
}
