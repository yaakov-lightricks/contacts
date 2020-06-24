package lightricks.yaakov.contacts.model.repo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;

public class ContactsRetriever {

    private static final String[] PROJECTION_DETAILS =
            {
                    ContactsContract.CommonDataKinds.Email._ID,
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.TYPE,
                    ContactsContract.CommonDataKinds.Email.LABEL,
            };

    /*
     * Defines the selection clause. Search for a lookup key
     * and the Email MIME type
     */
    private static final String SELECTION =
            ContactsContract.Data.LOOKUP_KEY + " = ?" +
                    " AND " +
                    ContactsContract.Data.MIMETYPE + " = " +
                    "'" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";

    private static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    @WorkerThread
    public static List<ContactEntry> getAllContacts(@NonNull Context context) {
        ContentResolver cr = context.getContentResolver();
        List<ContactEntry> items = new ArrayList<>();
        if (cr != null) {
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                try {
                    final int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    final int lookupIdIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                    final int thumbnailIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
                    final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int contactId;
                    String lookUpId, name, number, thumbnail;
                    while (cursor.moveToNext()) {
                        contactId = cursor.getInt(idIndex);
                        lookUpId = cursor.getString(lookupIdIndex);
                        name = cursor.getString(nameIndex);
                        number = cursor.getString(numberIndex);
                        thumbnail = cursor.getString(thumbnailIndex);
                        String email = getContactEmailById(context, lookUpId);
                        items.add(ContactEntry.create(contactId, lookUpId, thumbnail, name, email, number));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return items;
    }

    @Nullable
    private static String getContactEmailById(@NonNull Context context, String lookupId) {
        String email = null;
        ContentResolver cr = context.getContentResolver();
        if (cr != null) {
            Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, PROJECTION_DETAILS, SELECTION, new String[]{lookupId}, null);
            if (cursor != null && cursor.getCount() > 0) {
                try {
                    final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                    while (cursor.moveToNext()) {
                        email = cursor.getString(emailIndex);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return email;
    }


}
