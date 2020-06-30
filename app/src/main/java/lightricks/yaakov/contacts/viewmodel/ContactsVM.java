package lightricks.yaakov.contacts.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.model.repo.ContactRepo;

public class ContactsVM extends ViewModel {

    private final ContactRepo contactRepo;
    private MutableLiveData<Integer> scrollPosition = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> contactDetailsIsHidden = new MutableLiveData<>(false);

    public ContactsVM(@NonNull ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    /**
     * get contacts of in this device
     * @return LiveData wrapper for contact list
     */
    public LiveData<List<ContactEntry>> getVisibleContacts() {
        return contactRepo.getVisibleContacts();
    }

    /**
     * get contacts of in this device
     * @return LiveData wrapper for contact list
     */
    public LiveData<List<ContactEntry>> getHiddenContacts() {
        return contactRepo.getHiddenContacts();

    }


    /**
     * get contact entry by it Id
     * @param id of contact
     * @return {@link ContactEntry}
     */
    @Nullable
    public LiveData<ContactEntry> getContactById(int id){
        return contactRepo.getContactById(id);
    }

    /**
     * get data about the adapter scrolling position
     */
    public LiveData<Integer> getScrollPosition() {
        return scrollPosition;
    }

    public MutableLiveData<Boolean> getContactDetailsIsHidden() {
        return contactDetailsIsHidden;
    }

    public void updateScrollPosition(int position){
        scrollPosition.setValue(position);
    }

    public void toggleContactVisibility(ContactEntry contactEntry){
        boolean contactVisibility = contactRepo.toggleContactVisibility(contactEntry);
        contactDetailsIsHidden.setValue(contactVisibility);
    }
}
