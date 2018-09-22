package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import data.ContactContract.ContactEntry;
import com.example.sushil.safe.ContactDbHelper;

public class ContactProvider extends ContentProvider {

    private static final String TAG = ContactProvider.class.getSimpleName();

    /** Tag for the log messages */

    private static final int CONTACTS = 100;
    private static final int CONTACTS_ID = 101;

    private ContactDbHelper mDbHelper ;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY,ContactContract.PATH_CONTACT,CONTACTS);

        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY,ContactContract.PATH_CONTACT + "/#",CONTACTS_ID);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ContactDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection , @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;


        int match = sUriMatcher.match(uri);

        switch (match){

            case CONTACTS:
                cursor = database.query(ContactEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case CONTACTS_ID:
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ContactEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                return ContactEntry.CONTENT_LIST_TYPE;
            case CONTACTS_ID:
                return ContactEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI type "+uri+"with match "+match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case CONTACTS:
                return insertContact(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is supported for " + uri);
        }
    }

    private Uri insertContact(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String name = contentValues.getAsString(ContactEntry.COLUMN_CONTACT_NAME);
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name shouldn't be empty");
        }

        Integer number = contentValues.getAsInteger(ContactEntry.COLUMN_CONTACT_NUMBER);
        if(number == null) {
            throw new IllegalArgumentException("Number cannot be Invalid");
        }

        long id = database.insert(ContactEntry.TABLE_NAME, null, contentValues);
        if(id == -1){
            Log.e(TAG, "TEST: Failed to insert row for "+uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsEffected;
        switch(match) {
            case CONTACTS:
                rowsEffected = database.delete(ContactEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsEffected;
            case CONTACTS_ID:
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsEffected = database.delete(ContactEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsEffected;
            default:
                throw new IllegalArgumentException("Invalid delete Uri: "+uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                return updateContact(uri, contentValues, selection, selectionArgs);
            case CONTACTS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateContact(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateContact(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if(values.containsKey(ContactEntry.COLUMN_CONTACT_NAME)) {
            String name = values.getAsString(ContactEntry.COLUMN_CONTACT_NAME);
            if(name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name shouldn't be empty");
            }
        }
        if(values.containsKey(ContactEntry.COLUMN_CONTACT_NUMBER)) {
            Integer number = values.getAsInteger(ContactEntry.COLUMN_CONTACT_NUMBER);
            if(number == null){
                throw new IllegalArgumentException("Number is Invalid");
            }
        }
        if(values.size() == 0){
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(ContactEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
