package lightricks.yaakov.contacts.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.viewmodel.ContactListVM;


public class ContactDetailFragment extends Fragment {

    private ImageView thumbnail;
    private TextView textViewName;
    private TextView textViewHandle;
    private TextView textViewNumber;
    private View rootView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.activity_contact_details, container, false);
            thumbnail = rootView.findViewById(R.id.contact_image);
            textViewName = rootView.findViewById(R.id.label_name);
            textViewHandle = rootView.findViewById(R.id.label_handle);
            textViewNumber = rootView.findViewById(R.id.label_number);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ContactListVM model = new ViewModelProvider(requireActivity()).get(ContactListVM.class);
        if (getArguments() != null && getArguments().containsKey(Constants.ARG_ITEM_ID)) {
            int contactId = getArguments().getInt(Constants.ARG_ITEM_ID);
            ContactEntry contactEntry = model.getContactById(contactId);
            Toolbar appBarLayout = requireActivity().findViewById(R.id.toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle(contactEntry.name());
            }
            bindContact(contactEntry);
        }

    }

    private void bindContact(ContactEntry contactItem) {
        textViewName.setText(contactItem.name());
        String email = contactItem.email();
        if (email != null) {
            textViewHandle.setText(email);
        } else {
            textViewHandle.setVisibility(View.INVISIBLE);
        }
        textViewNumber.setText(contactItem.number());
        Glide
                .with(this)
                .load(contactItem.getThumbnailUri(getResources()))
                .centerCrop()
                .into(thumbnail);
    }
}