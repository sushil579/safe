package com.example.sushil.safe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.CursorLoader;

import data.ContactContract.DetailsEntry;

import static android.widget.Toast.*;

public class signup extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = signup.class.getSimpleName();

    private EditText mNameEdit;
    private EditText mNumberEdit;
    private EditText mEmailEdit;
    private EditText mPassword;
    private Button mRegButton;

    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setUpViews();mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void insertData() {
        String name = mNameEdit.getText().toString().trim();
        String mobileString = mNameEdit.getText().toString().trim();
        int mobile = Integer.parseInt(mobileString);
        String email = mEmailEdit.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(DetailsEntry.COLUMN_DETAIL_NAME, name);
        values.put(DetailsEntry.COLUMN_DETAIL_NUMBER, mobile);
        values.put(DetailsEntry.COLUMN_DETAIL_EMAIL, email);
        values.put(DetailsEntry.COLUMN_DETAIL_PASSWORD, password);

        Uri newUri = getContentResolver().insert(DetailsEntry.CONTENT_URI, values);

        Log.i(TAG, "TEST: RowId"+newUri);
        if(newUri == null) {
            Toast.makeText(this, "Saving Info failed.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Successfully Saved Info",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpViews() {
        mNameEdit = (EditText) findViewById(R.id.name);
        mNumberEdit = (EditText) findViewById(R.id.mymobile);
        mEmailEdit = (EditText) findViewById(R.id.emailid);
        mPassword = (EditText) findViewById(R.id.password2);
        mRegButton = (Button) findViewById(R.id.reg2);

        setTitle("Save Info");
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i(TAG, "TEST: signup: OnCreateLoader method is called.");
        String[] projections = {DetailsEntry._ID, DetailsEntry.COLUMN_DETAIL_NAME, DetailsEntry.COLUMN_DETAIL_NUMBER, DetailsEntry.COLUMN_DETAIL_EMAIL,
                DetailsEntry.COLUMN_DETAIL_PASSWORD, DetailsEntry.COLUMN_DETAIL_ADDRESS};
        switch(i) {
            case LOADER_ID:
                Log.i(TAG, "TEST: signup: LoaderID is valid in OnCreateLoader method");
                return new CursorLoader(this, DetailsEntry.CONTENT_URI, projections, null, null, null);
            default:
                Log.i(TAG, "TEST: signup: LoaderId is null");
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "TEST: onLoadFinished method is called");

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        loader.abandon();
    }
}
