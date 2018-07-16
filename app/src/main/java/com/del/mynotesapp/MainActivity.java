package com.del.mynotesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.LinkedList;

import adapter.NoteAdapter;
import db.NoteHelper;
import entity.Note;

import static db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    private Cursor list;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");

        rvNotes = (RecyclerView)findViewById(R.id.recycler_view);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setOnClickListener(this);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        fabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        adapter = new NoteAdapter(this);
        adapter.setListNotes(list);
        rvNotes.setAdapter(adapter);

        new LoadNoteAsync().execute();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes){
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list = notes;
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0){
                showSnackbarMessage("No Data Found");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                new LoadNoteAsync().execute();
                showSnackbarMessage("1 Item Added");
            }

        } else if(requestCode == FormAddUpdateActivity.REQUEST_UPDATE){
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                new LoadNoteAsync().execute();
                showSnackbarMessage("1 Item Changed");
            } else if(resultCode == FormAddUpdateActivity.RESULT_DELETE){
                new LoadNoteAsync().execute();
                showSnackbarMessage("1 Item Deleted");
        }

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }
}
