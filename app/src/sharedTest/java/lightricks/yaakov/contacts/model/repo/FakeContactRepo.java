package lightricks.yaakov.contacts.model.repo;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;

public class FakeContactRepo implements ContactRepo {

    private MutableLiveData<List<ContactEntry>> contacts = new MutableLiveData<>(new ArrayList<>());
    //hold all hidden contacts
    private MutableLiveData<List<ContactEntry>> hiddenContacts = new MutableLiveData<>(new ArrayList<>());

    public void addContacts(ContactEntry... args) {
        contacts.getValue().addAll(Arrays.asList(args));
    }

    @SuppressWarnings("ConstantConditions")
    public void clear(){
        contacts.getValue().clear();
        hiddenContacts.getValue().clear();
    }

    @Override
    public LiveData<List<ContactEntry>> getVisibleContacts() {
        return contacts;
    }

    @Override
    public LiveData<List<ContactEntry>> getHiddenContacts() {
        return hiddenContacts;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public LiveData<ContactEntry> getContactById(int id) {
        for (ContactEntry contactEntry : contacts.getValue()) {
            if (contactEntry.id() == id) {
                return new MutableLiveData<>(contactEntry);
            }
        }
        for (ContactEntry contactEntry : hiddenContacts.getValue()) {
            if (contactEntry.id() == id) {
                return new MutableLiveData<>(contactEntry);
            }
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean toggleContactVisibility(ContactEntry contactEntry) {
        boolean isHidden = hiddenContacts.getValue().contains(contactEntry);
        if (isHidden) {
            hiddenContacts.getValue().remove(contactEntry);
            contacts.getValue().add(contactEntry);
        } else {
            contacts.getValue().remove(contactEntry);
            hiddenContacts.getValue().add(contactEntry);
        }
        hiddenContacts.postValue(hiddenContacts.getValue());
        contacts.postValue(contacts.getValue());
        return !isHidden;
    }
}
