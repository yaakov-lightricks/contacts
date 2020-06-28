package lightricks.yaakov.contacts.view.fragments;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.viewmodel.ContactListVM;


public class ContactDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView thumbnail;
    private ImageView hideIcon;
    private TextView textViewName;
    private TextView textViewHandle;
    private TextView textViewNumber;
    private View rootView = null;
    private ContactListVM model;

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
            hideIcon = rootView.findViewById(R.id.hide);
            textViewName = rootView.findViewById(R.id.label_name);
            textViewHandle = rootView.findViewById(R.id.label_handle);
            textViewNumber = rootView.findViewById(R.id.label_number);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(ContactListVM.class);
        if (getArguments() != null && getArguments().containsKey(Constants.ARG_ITEM_ID)) {
            int contactId = getArguments().getInt(Constants.ARG_ITEM_ID);
            LiveData<ContactEntry> contactById = model.getContactById(contactId);
            if (contactById != null) {
                contactById.observe(getViewLifecycleOwner(), contactEntry -> {
                    if (contactEntry != null) {
                        Toolbar appBarLayout = requireActivity().findViewById(R.id.toolbar);
                        if (appBarLayout != null) {
                            appBarLayout.setTitle(contactEntry.name());
                        }
                        bindContact(contactEntry);
                    }
                });
            }
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
        hideIcon.setOnClickListener(this);
        hideIcon.setTag(contactItem);
        hideIcon.setImageResource(contactItem.isHidden() ? R.drawable.ic_unhide : R.drawable.ic_hide);
        Glide
                .with(this)
                .load(contactItem.getThumbnailUri(getResources()))
                .centerCrop()
                .into(thumbnail);
    }

    @Override
    public void onClick(View view) {
        model.toggleContactVisibility((ContactEntry) view.getTag());
    }
}