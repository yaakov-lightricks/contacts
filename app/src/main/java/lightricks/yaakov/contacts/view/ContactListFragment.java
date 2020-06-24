package lightricks.yaakov.contacts.view;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.viewmodel.ContactListVM;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ContactListFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private RecyclerView recyclerView;
    private ContactListVM model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(ContactListVM.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_contacts_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        setupList();
        return rootView;
    }

    @AfterPermissionGranted(Constants.CONTACT_PERMISSIONS)
    private void setupList() {
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            model.getContacts(requireContext().getApplicationContext()).observe(getViewLifecycleOwner(), contactEntries -> {
                ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(contactEntries, ContactListFragment.this);
                recyclerView.setAdapter(adapter);
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
            View labelView = view.findViewById(R.id.label_name);
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(thumb, Constants.KEY_PROFILE_TRANSITION )
                    .addSharedElement(labelView, Constants.KEY_LABEL_TRANSITION)
                    .build();
            ContactListFragmentDirections.MasterToDetailContact action = ContactListFragmentDirections.masterToDetailContact();
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
