package lightricks.yaakov.contacts.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.model.repo.AllContactsLiveData;

public class ContactListVM extends ViewModel {

    //hold contacts data
    private AllContactsLiveData contacts;
    //hold data about the adapter scrolling position
    private MutableLiveData<Integer> scrollPosition = new MutableLiveData<>(0);

    /**
     * get contacts of in this device
     * @return LiveData wrapper for contact list
     */
    public LiveData<List<ContactEntry>> getContacts(Context context) {
        //lazy loading contacts
        if (contacts == null) {
            contacts = new AllContactsLiveData(context);
        }
        return contacts;
    }


    /**
     * get contact entry by it Id
     * @param id of contact
     * @return {@link ContactEntry}
     */
    public ContactEntry getContactById(int id){
        if (contacts == null) {
            return null;
        }
        for (ContactEntry contactEntry : contacts.getValue()) {
            if(contactEntry.id() == id){
                return contactEntry;
            }
        }
        return null;
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
}
