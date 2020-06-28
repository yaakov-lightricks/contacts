package lightricks.yaakov.contacts.model.repo;

import androidx.lifecycle.LiveData;

import java.util.List;

import lightricks.yaakov.contacts.model.entities.ContactEntry;

public interface ContactRepo {


    /**
     * get all available contacts
     * @return LiveData wrapping a list of {@link lightricks.yaakov.contacts.model.entities.ContactEntry}
     */
    LiveData<List<ContactEntry>> getContacts();

    /**
     * set visibility for a contact
     * @param contactEntry to set it visibility
     * @param isHidden is should be hidden for user
     */
    void setContactVisibility(ContactEntry contactEntry, boolean isHidden);

}