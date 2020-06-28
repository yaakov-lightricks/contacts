package lightricks.yaakov.contacts.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.model.repo.AllContactsLiveData;

public class ContactListVM extends AndroidViewModel {

    //hold contacts data
    private AllContactsLiveData contacts;
    //hold filtered contacts
    private LiveData<List<ContactEntry>> visibleContacts;
    //hold all hidden contacts
    private LiveData<List<ContactEntry>> hiddenContacts;
    //hold data about the adapter scrolling position
    private MutableLiveData<Integer> scrollPosition = new MutableLiveData<>(0);
//    private MutableLiveData<Boolean> isHiddenLiveData = new MutableLiveData<>(false);

    private final SharedPreferences prefs;


    public ContactListVM(@NonNull Application application) {
        super(application);
        prefs = PreferenceManager.getDefaultSharedPreferences(application);

    }

    /**
     * get contacts of in this device
     * @return LiveData wrapper for contact list
     */
    public LiveData<List<ContactEntry>> getVisibleContacts(Context context) {
        //lazy loading contacts
        if (contacts == null) {
            contacts = new AllContactsLiveData(context);
        }
        if (visibleContacts == null) {
            visibleContacts = Transformations.switchMap(contacts, entries -> {
                List<ContactEntry> visible = new ArrayList<>();
                for (ContactEntry entry : entries) {
                    if (!entry.isHidden()){
                        visible.add(entry);
                    }
                }
                return new MutableLiveData<>(visible);
            });
        }
        return visibleContacts;
    }

    /**
     * get contacts of in this device
     * @return LiveData wrapper for contact list
     */
    public LiveData<List<ContactEntry>> getHiddenContacts(Context context) {
        //lazy loading contacts
        if (contacts == null) {
            contacts = new AllContactsLiveData(context);
        }
        if (hiddenContacts == null) {
            hiddenContacts = Transformations.switchMap(contacts, entries -> {
                List<ContactEntry> hidden = new ArrayList<>();
                for (ContactEntry entry : entries) {
                    if (entry.isHidden()){
                        hidden.add(entry);
                    }
                }
                return new MutableLiveData<>(hidden);
            });
        }
        return hiddenContacts;
    }


    /**
     * get contact entry by it Id
     * @param id of contact
     * @return {@link ContactEntry}
     */
    @Nullable
    public LiveData<ContactEntry> getContactById(int id){
        if (contacts == null || contacts.getValue() == null) {
            return null;
        }
        return Transformations.switchMap(contacts, entries -> {
            for (ContactEntry contactEntry : entries) {
                if(contactEntry.id() == id){
                    return new MutableLiveData<>(contactEntry);
                }
            }
            return null;
        });
    }

    /**
     * get data about the adapter scrolling position
     */
    public LiveData<Integer> getScrollPosition() {
        return scrollPosition;
    }

    public void updateScrollPosition(int position){
        scrollPosition.setValue(position);
    }

    public void toggleContactVisibility(ContactEntry contactEntry){
        boolean isHidden = contactEntry.isHidden();
        prefs.edit().putBoolean(Constants.PREFIX_CONTACTS + contactEntry.id(), !isHidden).apply();
        contacts.refresh();
    }
}
