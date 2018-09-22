package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContactContract {
    public static final String CONTENT_AUTHORITY = "com.example.sushil.safe";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_CONTACT = "contacts";

    private ContactContract() {}

    public static abstract class ContactEntry implements BaseColumns{
        public static final String TABLE_NAME = "contacts";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CONTACT_NAME = "name";
        public static final String COLUMN_CONTACT_NUMBER = "number";
        public static final String COLUMN_CONTACT_PRIORITY = "priority";

        public static final int CONTACT_PRIORITY_POLICE = 0;
        public static final int CONTACT_PRIORITY_TRUSTED_NUMBER =1;
        public static final int CONTACT_PRIORITY_FRIENDS = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONTACT);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_CONTACT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_CONTACT;
    }
}
