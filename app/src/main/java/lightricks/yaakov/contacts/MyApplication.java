package lightricks.yaakov.contacts;

import android.app.Application;
import android.preference.PreferenceManager;

import lightricks.yaakov.contacts.model.repo.ContactRepo;
import lightricks.yaakov.contacts.model.repo.ContactRepoImpl;
import lightricks.yaakov.contacts.model.repo.ContactsLiveData;

public class MyApplication extends Application {

    private ContactRepo contactRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        contactRepo = new ContactRepoImpl(PreferenceManager.getDefaultSharedPreferences(this), new ContactsLiveData(this));
    }

    public ContactRepo getContactRepo() {
        return contactRepo;
    }
}
