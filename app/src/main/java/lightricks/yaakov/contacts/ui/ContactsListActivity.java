package lightricks.yaakov.contacts.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactItem;
import lightricks.yaakov.contacts.model.repo.ContactRepoStorage;
import lightricks.yaakov.contacts.model.repo.ContactRepo;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ContactsListActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupList();
    }

    @AfterPermissionGranted(Constants.CONTACT_PERMISSIONS)
    private void setupList() {
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            ContactRepo contactRepo = new ContactRepoStorage(getApplication());
            ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(contactRepo.getContacts(), this);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
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
        ContactItem contactItem = (ContactItem) view.getTag();
        if (contactItem != null) {
            Intent intent = new Intent(this, ContactDetailsActivity.class);
            intent.putExtra(Constants.KEY_CONTACTS, contactItem);
            View thumb = findViewById(R.id.thumbnail);
            Pair<View, String> thumbnail = Pair.create(thumb, Constants.KEY_PROFILE_TRANSITION + contactItem.getId());
            View labelView = findViewById(R.id.label_name);
            Pair<View, String> label = Pair.create(labelView, Constants.KEY_LABEL_TRANSITION + contactItem.getId());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, thumbnail, label);
            startActivity(intent, options.toBundle());
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
            Snackbar.make(findViewById(R.id.root),
                    R.string.revoke_permission, Snackbar.LENGTH_LONG).setAction(android.R.string.ok, view -> {
                setupList();
            }).show();
        }
    }
}
