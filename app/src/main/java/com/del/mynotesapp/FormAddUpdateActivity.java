package com.del.mynotesapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import db.NoteHelper;
import entity.Note;

import static db.DatabaseContract.CONTENT_URI;
import static db.DatabaseContract.NoteColumns.*;

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTitle, edtDescription;
    private Button btnSubmit;

    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";
    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Note note;
    //private int position;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);

        edtTitle = (TextInputEditText)findViewById(R.id.edt_title);
        edtDescription = (TextInputEditText)findViewById(R.id.edt_description);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        Uri uri = getIntent().getData();

        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null){
                if (cursor.moveToFirst()) note = new Note(cursor);
                cursor.close();
            }
        }

        String actionBarTitle = null;
        String btnTitle = null;

        if(note != null){
            isEdit = true;

            actionBarTitle = "Change";
            btnTitle = "Update";

            edtTitle.setText(note.getTitle());
            edtDescription.setText(note.getDescription());
        } else{
            actionBarTitle = "Add";
            btnTitle = "Save";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit){
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            boolean isEmpty = false;

            if (TextUtils.isEmpty(title)){
                isEmpty = true;
                edtTitle.setError("Field can't be blank");
            }

            if (!isEmpty){
                ContentValues values = new ContentValues();
                values.put(TITLE, title);
                values.put(DESCRIPTION, description);

                if (isEdit){
                    getContentResolver().update(getIntent().getData(), values, null, null);

                    setResult(RESULT_UPDATE);
                    finish();
                } else{
                    values.put(DATE, getCurrentDate());

                    getContentResolver().insert(CONTENT_URI, values);

                    setResult(RESULT_ADD);
                    finish();
                }
            }
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null;
        String dialogMessage = null;

        if (isDialogClose){
            dialogTitle = "Cancel";
            dialogMessage = "Discard Change?";
        } else{
            dialogTitle = "Delete Note";
            dialogMessage = "Delete Item?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose){
                            finish();
                        } else{
                            getContentResolver().delete(getIntent().getData(), null, null);
                            setResult(RESULT_DELETE, null);
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }
}
