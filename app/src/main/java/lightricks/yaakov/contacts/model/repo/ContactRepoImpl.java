package lightricks.yaakov.contacts.model.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import java.util.List;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.model.entities.ContactEntry;

public class ContactRepoImpl implements ContactRepo {

    private final SharedPreferences prefs;
    private ContactsLiveData contactsLiveData;

    public ContactRepoImpl(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.contactsLiveData = new ContactsLiveData(context);
    }

    @Override
    public LiveData<List<ContactEntry>> getContacts() {
        return contactsLiveData;
    }

    @Override
    public void setContactVisibility(ContactEntry contactEntry, boolean isHidden) {
        prefs.edit().putBoolean(Constants.PREFIX_CONTACTS + contactEntry.id(), isHidden).apply();
        contactsLiveData.refresh();
    }
}
