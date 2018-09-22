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

import com.example.sushil.safe.ContactDbHelper;
import com.example.sushil.safe.DetailDbHelper;

import data.ContactContract.DetailsEntry;
/**
 * Created by win10 on 22-09-2018.
 */

public class DetailProvider extends ContentProvider {

    private static final String TAG = DetailProvider.class.getSimpleName();

    /** Tag for the log messages */

    private static final int DETAILS = 100;
    private static final int DETAILS_ID = 101;

    private DetailDbHelper mDbHelper ;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY,ContactContract.PATH_DETAILS,DETAILS);

        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY,ContactContract.PATH_DETAILS + "/#",DETAILS_ID);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DetailDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DETAILS:
                cursor = database.query(DetailsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder );
                break;
            case DETAILS_ID:

                selection = DetailsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(DetailsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case DETAILS:
                return DetailsEntry.CONTENT_LIST_TYPE;
            case DETAILS_ID:
                return DetailsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI type " + uri + "with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DETAILS:
                return insertDetail(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+uri);
        }
    }

    private Uri insertDetail(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String name = contentValues.getAsString(DetailsEntry.COLUMN_DETAIL_NAME);
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name shouldn't be empty");
        }

        Integer number = contentValues.getAsInteger(DetailsEntry.COLUMN_DETAIL_NUMBER);
        if(number == null) {
            throw new IllegalArgumentException("Number cannot be Invalid");
        }

        String email = contentValues.getAsString(DetailsEntry.COLUMN_DETAIL_EMAIL);
        if(email == null){
            throw new IllegalArgumentException("Email shouldn't be null");
        }

        String password = contentValues.getAsString(DetailsEntry.COLUMN_DETAIL_PASSWORD);
        if(password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password shouldn't be empty");
        }

        String address = contentValues.getAsString(DetailsEntry.COLUMN_DETAIL_ADDRESS);
        if(address==null){
            throw new IllegalArgumentException("Address shouldn't be null");
        }

        long id = database.insert(DetailsEntry.TABLE_NAME, null, contentValues);
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
            case DETAILS:
                rowsEffected = database.delete(DetailsEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsEffected;
            case DETAILS_ID:
                selection = DetailsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsEffected = database.delete(DetailsEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsEffected;
            default:
                throw new IllegalArgumentException("Invalid delete Uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case DETAILS:
                    return updateDetail(uri, contentValues, selection, selectionArgs);
                case DETAILS_ID:
                    // For the PET_ID code, extract out the ID from the URI,
                    // so we know which row to update. Selection will be "_id=?" and selection
                    // arguments will be a String array containing the actual ID.
                    selection = DetailsEntry._ID + "=?";
                    selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                    return updateDetail(uri, contentValues, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Update is not supported for " + uri);
            }
    }

    private int updateDetail(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(DetailsEntry.COLUMN_DETAIL_NAME)) {
            String name = values.getAsString(DetailsEntry.COLUMN_DETAIL_NAME);
            if(name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name shouldn't be empty");
            }
        }
        if(values.containsKey(DetailsEntry.COLUMN_DETAIL_NUMBER)) {
            Integer number = values.getAsInteger(DetailsEntry.COLUMN_DETAIL_NUMBER);
            if(number == null) {
                throw new IllegalArgumentException("Number cannot be Invalid");
            }
        }
        if(values.containsKey(DetailsEntry.COLUMN_DETAIL_EMAIL)){
            String email = values.getAsString(DetailsEntry.COLUMN_DETAIL_EMAIL);
            if(email == null){
                throw new IllegalArgumentException("Email shouldn't be null");
            }
        }
        if(values.containsKey(DetailsEntry.COLUMN_DETAIL_PASSWORD)){
            String password = values.getAsString(DetailsEntry.COLUMN_DETAIL_PASSWORD);
            if(password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password shouldn't be empty");
            }
        }
        if(values.containsKey(DetailsEntry.COLUMN_DETAIL_ADDRESS)) {
            String address = values.getAsString(DetailsEntry.COLUMN_DETAIL_ADDRESS);
            if(address==null){
                throw new IllegalArgumentException("Address shouldn't be null");
            }
        }
        if(values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(DetailsEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
