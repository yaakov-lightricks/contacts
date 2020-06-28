package lightricks.yaakov.contacts.model.repo;

import android.content.Context;
import android.provider.ContactsContract;

import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;

public class ContactsLiveData extends ContentProviderLiveData<List<ContactEntry>> {

    public ContactsLiveData(Context context) {
        super(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    }

    @Override
    List<ContactEntry> getContentProviderValue() {
        return ContactsRetriever.getAllContacts(context);
    }
}
