package lightricks.yaakov.contacts.view.fragments;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AllContactListFragmentTest extends BaseFragmentTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void when_lunched_recycler_has_three_items() {
        FragmentScenario.launchInContainer(AllContactListFragment.class);
        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(3)));
    }

    @Test
    public void when_click_navigate_to_details_fragment() {
        FragmentScenario<AllContactListFragment> scenario = FragmentScenario.launchInContainer(AllContactListFragment.class);
        NavController navController = mock(NavController.class);
        scenario.onFragment((FragmentScenario.FragmentAction<AllContactListFragment>) fragment -> Navigation.setViewNavController(fragment.requireView(), navController));
        // WHEN - Click on the first list item
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //verify that we navigated to the details screen
        verify(navController).navigate(any(AllContactListFragmentDirections.MasterToDetailContact.class), any(Navigator.Extras.class));
    }

}