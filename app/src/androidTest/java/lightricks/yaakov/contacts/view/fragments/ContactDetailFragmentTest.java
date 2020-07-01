package lightricks.yaakov.contacts.view.fragments;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import lightricks.yaakov.contacts.LiveDataTestUtil;
import lightricks.yaakov.contacts.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static lightricks.yaakov.contacts.DrawableMatcher.withDrawable;
import static org.hamcrest.Matchers.hasSize;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ContactDetailFragmentTest extends BaseFragmentTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        super.setUp();
        AllContactListFragmentDirections.MasterToDetailContact action = AllContactListFragmentDirections.masterToDetailContact();
        action.setContactId(contactEntry2.id());
        FragmentScenario.launchInContainer(ContactDetailFragment.class, action.getArguments());
    }

    @Test
    public void when_lunched_should_display_details() {
        onView(withId(R.id.label_name)).check(matches(withText(contactEntry2.name())));
        onView(withId(R.id.label_number)).check(matches(withText(contactEntry2.number())));
    }

    @Test
    public void click_hide_should_toggle_hide_icon() {
        onView(withId(R.id.hide)).check(matches(withDrawable(R.drawable.ic_hide)));
        onView(withId(R.id.hide)).perform(click());
        onView(withId(R.id.hide)).check(matches(withDrawable(R.drawable.ic_unhide)));
    }

    @Test
    public void when_click_on_hide_should_hide() throws InterruptedException {
        contactRepo.toggleContactVisibility(contactEntry2);
        assertThat(LiveDataTestUtil.getOrAwaitValue(contactRepo.getHiddenContacts()), hasSize(1));
        assertThat(LiveDataTestUtil.getOrAwaitValue(contactRepo.getVisibleContacts()), hasSize(2));
        onView(withId(R.id.hide)).perform(click());
        //item should move from hidden to visible
        assertThat(LiveDataTestUtil.getOrAwaitValue(contactRepo.getHiddenContacts()), hasSize(0));
        assertThat(LiveDataTestUtil.getOrAwaitValue(contactRepo.getVisibleContacts()), hasSize(3));
    }
}