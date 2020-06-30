package lightricks.yaakov.contacts.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import lightricks.yaakov.contacts.model.repo.ContactRepo;

public class ContactsVmFactory implements ViewModelProvider.Factory {

    private final ContactRepo contactRepo;

    public ContactsVmFactory(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ContactsVM(contactRepo);
    }
}
