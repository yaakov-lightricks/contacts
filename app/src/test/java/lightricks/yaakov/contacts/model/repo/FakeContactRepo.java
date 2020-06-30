package lightricks.yaakov.contacts.model.repo;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;

public class FakeContactRepo implements ContactRepo {

    private LiveData<List<ContactEntry>> contacts = new MutableLiveData<>(new ArrayList<>());

    public void addContacts(ContactEntry... args){
        //noinspection ConstantConditions
        contacts.getValue().addAll(Arrays.asList(args));
    }

    @Override
    public LiveData<List<ContactEntry>> getContacts() {
        return contacts;
    }

    @Override
    public void setContactVisibility(ContactEntry contactEntry, boolean isHidden) {
        //noinspection ConstantConditions
        Iterator<ContactEntry> iterator = contacts.getValue().iterator();
        int i = 0;
        while (iterator.hasNext()){
            ContactEntry entry = iterator.next();
            if (entry.id() == contactEntry.id()){
                ContactEntry newEntry = ContactEntry.create(contactEntry.id(), contactEntry.lookupId(), contactEntry.thumbnail(), contactEntry.name(), contactEntry.email(), contactEntry.number(), isHidden);
                iterator.remove();
                contacts.getValue().add(i, newEntry);
                break;
            }
            i++;
        }
    }
}
