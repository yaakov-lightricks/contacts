package lightricks.yaakov.contacts.view.fragments;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import lightricks.yaakov.contacts.MyApplication;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.model.repo.FakeContactRepo;

@RunWith(AndroidJUnit4.class)
public class BaseFragmentTest {

    ContactEntry contactEntry1 = ContactEntry.create(1, "1", "", "test1", "", "number1");
    ContactEntry contactEntry2 = ContactEntry.create(2, "2", "", "test2", "", "number2");
    ContactEntry contactEntry3 = ContactEntry.create(3, "3", "", "test3", "", "number3");
    FakeContactRepo contactRepo;

    @Before
    public void setUp() {
        contactRepo = (FakeContactRepo) ((MyApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext()).getContactRepo();
        contactRepo.addContacts(contactEntry1, contactEntry2, contactEntry3);
    }

    @After
    public void tearDown() throws Exception {
        contactRepo.clear();
    }

    @Test
    public void empty() {
        //just to be able to run all test in directory without error
    }
}