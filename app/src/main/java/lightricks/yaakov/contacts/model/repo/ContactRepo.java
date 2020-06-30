package lightricks.yaakov.contacts.model.repo;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;

public interface ContactRepo {


    /**
     * get all visible contacts
     * @return LiveData wrapping a list of {@link lightricks.yaakov.contacts.model.entities.ContactEntry}
     */
    LiveData<List<ContactEntry>> getVisibleContacts();

    /**
     * get all hidden contacts
     * @return LiveData wrapping a list of {@link lightricks.yaakov.contacts.model.entities.ContactEntry}
     */
    LiveData<List<ContactEntry>> getHiddenContacts();

    /**
     * get contact entry by it Id
     * @param id of contact
     * @return {@link ContactEntry}
     */
    @Nullable
    public LiveData<ContactEntry> getContactById(int id);

    /**
     * toggle visibility for a contact
     * @param contactEntry to set it visibility
     * @return new visibility state for contact
     */
    boolean toggleContactVisibility(ContactEntry contactEntry);

}