package lightricks.yaakov.contacts.model.repo;

import lightricks.yaakov.contacts.model.entities.ContactItem;

import java.util.List;

public interface ContactRepo {


    /**
     * get all available contacts
     * @return list of {@link lightricks.yaakov.contacts.model.entities.ContactItem}
     */
    List<ContactItem> getContacts();

    /**
     * retrieve a contact email by it id
     * @param id of contact
     * @return email for user
     */
    String getContactEmailById(String id);
}
