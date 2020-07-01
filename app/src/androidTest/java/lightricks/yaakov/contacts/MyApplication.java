package lightricks.yaakov.contacts;

import android.app.Application;

import lightricks.yaakov.contacts.model.repo.ContactRepo;
import lightricks.yaakov.contacts.model.repo.FakeContactRepo;

public class MyApplication extends Application {

    private ContactRepo contactRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        contactRepo = new FakeContactRepo();
    }

    public ContactRepo getContactRepo() {
        return contactRepo;
    }
}
