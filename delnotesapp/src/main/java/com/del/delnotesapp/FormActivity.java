package com.del.delnotesapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import entity.NoteItem;

import static com.del.delnotesapp.DatabaseContract.CONTENT_URI;
import static com.del.delnotesapp.DatabaseContract.NoteColumns.*;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtTitle, edtDescription;
    Button btnSubmit;

    public static String EXTRA_NOTE_ITEM = "extra_note_item";
    private NoteItem noteItem = null;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        edtTitle = (EditText)findViewById(R.id.edt_title);
        edtDescription = (EditText)findViewById(R.id.edt_description);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        Uri uri = getIntent().getData();

        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

            if (cursor != null){

                if (cursor.moveToFirst()) noteItem = new NoteItem(cursor);
                cursor.close();
            }
        }

        String actionBarTitle = null;
        String btnActionTitle = null;

        if (noteItem != null){
            isUpdate = true;
            actionBarTitle = "Update";
            btnActionTitle = "Save";

            edtTitle.setText(noteItem.getTitle());
            edtDescription.setText(noteItem.getDescription());
        } else{
            actionBarTitle = "Add New";
            btnActionTitle = "Submit";
        }

        btnSubmit.setText(btnActionTitle);
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit){
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            boolean isEmptyField = false;

            if (TextUtils.isEmpty(title)) {
                isEmptyField = true;
                edtTitle.setError("Field can't be blank");
            }

            if (!isEmptyField){
                ContentValues contentValues = new ContentValues();
                contentValues.put(TITLE, title);
                contentValues.put(DESCRIPTION, description);
                contentValues.put(DATE, getCurrentDate());

                if (isUpdate){
                    Uri uri = getIntent().getData();
                    getContentResolver().update(uri, contentValues, null, null);

                    Toast.makeText(this, "1 Note Updated", Toast.LENGTH_SHORT).show();
                } else {
                    getContentResolver().insert(CONTENT_URI, contentValues);

                    Toast.makeText(this, "1 Note Added", Toast.LENGTH_SHORT).show();
                }
                finish();
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
        if (isUpdate){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        if (item.getItemId() == R.id.action_delete){
            showDeleteAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAlertDialog() {
        String dialogMessage = "Delete This Item?";
        String dialogTitle = "Delete Note";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Uri uri = getIntent().getData();
                        getContentResolver().delete(uri, null, null);
                        Toast.makeText(FormActivity.this, "1 Item Deleted", Toast.LENGTH_SHORT).show();
                        finish();
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
}
