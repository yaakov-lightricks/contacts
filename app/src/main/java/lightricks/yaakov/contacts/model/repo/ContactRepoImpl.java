package lightricks.yaakov.contacts.model.repo;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.model.entities.ContactEntry;

@SuppressWarnings("deprecation")
public class ContactRepoImpl implements ContactRepo {

    private final SharedPreferences prefs;
    private ContentProviderLiveData<List<ContactEntry>> contactsLiveData;
    //hold filtered contacts
    private LiveData<List<ContactEntry>> visibleContacts;
    //hold all hidden contacts
    private LiveData<List<ContactEntry>> hiddenContacts;

    public ContactRepoImpl(SharedPreferences prefs, ContentProviderLiveData<List<ContactEntry>> contactsLiveData) {
        this.prefs = prefs;
        this.contactsLiveData = contactsLiveData;
        //todo remove duplications, find more efficient way for this
        visibleContacts = Transformations.switchMap(contactsLiveData, entries -> {
            List<ContactEntry> visible = new ArrayList<>();
            for (ContactEntry entry : entries) {
                if (!prefs.getBoolean(Constants.PREFIX_CONTACTS + entry.id(), false)){
                    visible.add(entry);
                }
            }
            return new MutableLiveData<>(visible);
        });
        hiddenContacts = Transformations.switchMap(contactsLiveData, entries -> {
            List<ContactEntry> visible = new ArrayList<>();
            for (ContactEntry entry : entries) {
                if (prefs.getBoolean(Constants.PREFIX_CONTACTS + entry.id(), false)){
                    visible.add(entry);
                }
            }
            return new MutableLiveData<>(visible);
        });
    }

    @Override
    public LiveData<List<ContactEntry>> getVisibleContacts() {
        return visibleContacts;
    }

    @Override
    public LiveData<List<ContactEntry>> getHiddenContacts() {
        return hiddenContacts;
    }

    @Nullable
    @Override
    public LiveData<ContactEntry> getContactById(int id) {
        return Transformations.switchMap(contactsLiveData, entries -> {
            for (ContactEntry contactEntry : entries) {
                if(contactEntry.id() == id){
                    return new MutableLiveData<>(contactEntry);
                }
            }
            return null;
        });
    }

    @Override
    public boolean toggleContactVisibility(ContactEntry contactEntry) {
        boolean isHidden = prefs.getBoolean(Constants.PREFIX_CONTACTS + contactEntry.id(), false);
        prefs.edit().putBoolean(Constants.PREFIX_CONTACTS + contactEntry.id(), !isHidden).apply();
        contactsLiveData.refresh();
        return prefs.getBoolean(Constants.PREFIX_CONTACTS + contactEntry.id(), false);
    }
}
