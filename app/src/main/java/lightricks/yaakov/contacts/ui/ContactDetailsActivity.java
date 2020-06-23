package lightricks.yaakov.contacts.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactItem;
import lightricks.yaakov.contacts.model.repo.ContactRepo;
import lightricks.yaakov.contacts.model.repo.ContactRepoStorage;

public class ContactDetailsActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView textViewName;
    private TextView textViewHandle;
    private TextView textViewNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        thumbnail = findViewById(R.id.contact_image);
        textViewName = findViewById(R.id.label_name);
        textViewHandle = findViewById(R.id.label_handle);
        textViewNumber = findViewById(R.id.label_number);
        ContactRepo contactRepo = new ContactRepoStorage(getApplication());
        if (getIntent() != null) {
            ContactItem contactItem = getIntent().getParcelableExtra(Constants.KEY_CONTACTS);
            if (contactItem != null) {
                textViewName.setTransitionName(Constants.KEY_LABEL_TRANSITION + contactItem.getId());
                thumbnail.setTransitionName(Constants.KEY_PROFILE_TRANSITION + contactItem.getId());
                String email = contactRepo.getContactEmailById(contactItem.getLookupId());
                contactItem.setHandle(email);
                bindContact(contactItem);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //To support reverse transitions when user clicks the device back button
        supportFinishAfterTransition();
    }

    @Override
    public void onPrepareSupportNavigateUpTaskStack(@NonNull TaskStackBuilder builder) {
        super.onPrepareSupportNavigateUpTaskStack(builder);
    }

    private void bindContact(ContactItem contactItem) {
        textViewName.setText(contactItem.getName());
        textViewHandle.setText(contactItem.getHandle());
        textViewNumber.setText(contactItem.getNumber());
        Glide
                .with(this)
                .load(contactItem.getThumbnailUri(getResources()))
                .centerCrop()
                .into(thumbnail);
    }
}
