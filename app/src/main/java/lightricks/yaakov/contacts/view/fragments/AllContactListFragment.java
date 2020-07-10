package lightricks.yaakov.contacts.view.fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.view.ContactsRecyclerAdapter;
import lightricks.yaakov.contacts.viewmodel.ContactListVM;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AllContactListFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private RecyclerView recyclerView;
    private ContactListVM model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(ContactListVM.class);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.content_contacts_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @AfterPermissionGranted(Constants.CONTACT_PERMISSIONS)
    private void setupList() {
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            model.getVisibleContacts(requireContext().getApplicationContext()).observe(getViewLifecycleOwner(), contactEntries -> {
                postponeEnterTransition(3, TimeUnit.SECONDS);
                ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(contactEntries, AllContactListFragment.this);
                recyclerView.setAdapter(adapter);
                recyclerView.getViewTreeObserver().addOnPreDrawListener(() -> {
                    startPostponedEnterTransition();
                    return true;
                });
                model.getScrollPosition().observe(getViewLifecycleOwner(), position -> recyclerView.scrollToPosition(position));
            });
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.contacts_rationale), Constants.CONTACT_PERMISSIONS, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onClick(View view) {
        ContactEntry contactItem = (ContactEntry) view.getTag();
        int position = recyclerView.getChildLayoutPosition(view);
        model.updateScrollPosition(position);
        if (contactItem != null) {
            View thumb = view.findViewById(R.id.thumbnail);
            thumb.setTransitionName(Constants.KEY_PROFILE_TRANSITION + contactItem.id());
            View labelView = view.findViewById(R.id.label_name);
            labelView.setTransitionName(Constants.KEY_LABEL_TRANSITION + contactItem.id());
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(thumb, Constants.KEY_PROFILE_TRANSITION  + contactItem.id())
                    .addSharedElement(labelView, Constants.KEY_LABEL_TRANSITION + contactItem.id())
                    .build();
            AllContactListFragmentDirections.MasterToDetailContact action = AllContactListFragmentDirections.masterToDetailContact();
            action.setContactId(contactItem.id());
            Navigation.findNavController(view).navigate(action, extras);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        setupList();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            Snackbar.make(getView().findViewById(R.id.root),
                    R.string.revoke_permission, Snackbar.LENGTH_LONG).setAction(android.R.string.ok, view -> {
                setupList();
            }).show();
        }
    }
}
