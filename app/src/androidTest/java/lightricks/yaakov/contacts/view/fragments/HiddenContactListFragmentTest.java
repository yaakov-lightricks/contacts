package lightricks.yaakov.contacts.view.fragments;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import lightricks.yaakov.contacts.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class HiddenContactListFragmentTest extends BaseFragmentTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void when_click_hidden_should_remove_it() {
        contactRepo.toggleContactVisibility(contactEntry2);
        FragmentScenario.launchInContainer(HiddenContactListFragment.class);
        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(1)));
        //click on item should move it from list
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(0)));
    }
}