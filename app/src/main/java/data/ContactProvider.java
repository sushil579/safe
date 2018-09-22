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
        
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
