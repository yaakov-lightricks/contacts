package lightricks.yaakov.contacts.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import lightricks.yaakov.contacts.LiveDataTestUtil;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.model.repo.FakeContactRepo;

import static org.junit.Assert.assertEquals;

public class ContactListVMTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ContactsVM model;
    private FakeContactRepo contactRepo;
    ContactEntry contactEntry1 = ContactEntry.create(1, "1", "", "test1", "", "number1", false);
    ContactEntry contactEntry2 = ContactEntry.create(2, "2", "", "test2", "", "number2", false);
    ContactEntry contactEntry3 = ContactEntry.create(3, "3", "", "test3", "", "number3", false);

    @Before
    public void setUp() throws Exception {
        contactRepo = new FakeContactRepo();
        contactRepo.addContacts(contactEntry1, contactEntry2, contactEntry3);
        model = new ContactsVM(contactRepo);
    }

    @Test
    public void when_get_contacts_get_all_list() throws InterruptedException {
        //when asking to get visible contacts
        LiveData<List<ContactEntry>> visibleContacts = model.getVisibleContacts();
        List<ContactEntry> results = LiveDataTestUtil.getOrAwaitValue(visibleContacts);
        assertEquals(results, Arrays.asList(contactEntry1, contactEntry2, contactEntry3));
    }

    @Test
    public void test_when_change_visibility_invisible_is_changed() throws InterruptedException {
        LiveData<List<ContactEntry>> hiddenContacts = model.getHiddenContacts();
        model.toggleContactVisibility(contactEntry2);
        List<ContactEntry> results = LiveDataTestUtil.getOrAwaitValue(hiddenContacts);
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).id());
    }
}