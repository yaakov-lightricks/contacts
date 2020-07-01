package lightricks.yaakov.contacts.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lightricks.yaakov.contacts.MyApplication;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactEntry;
import lightricks.yaakov.contacts.model.repo.ContactRepo;
import lightricks.yaakov.contacts.view.ContactsRecyclerAdapter;
import lightricks.yaakov.contacts.viewmodel.ContactsVM;
import lightricks.yaakov.contacts.viewmodel.ContactsVmFactory;

public class HiddenContactListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView emptyListView;
    private ContactsVM model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactRepo contactRepo = ((MyApplication)requireActivity().getApplication()).getContactRepo();
        model = new ViewModelProvider(requireActivity(),new ContactsVmFactory(contactRepo)).get(ContactsVM.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_contacts_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyListView = rootView.findViewById(R.id.empty_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        setupList();
        return rootView;
    }

    private void setupList() {
        model.getHiddenContacts().observe(getViewLifecycleOwner(), contactEntries -> {
            emptyListView.setVisibility(contactEntries.size() == 0 ? View.VISIBLE : View.GONE);
            ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(contactEntries, HiddenContactListFragment.this);
            recyclerView.setAdapter(adapter);
            model.getScrollPosition().observe(getViewLifecycleOwner(), position -> recyclerView.scrollToPosition(position));
        });
    }

    @Override
    public void onClick(View view) {
        ContactEntry contactItem = (ContactEntry) view.getTag();
        model.toggleContactVisibility(contactItem);
    }
}
