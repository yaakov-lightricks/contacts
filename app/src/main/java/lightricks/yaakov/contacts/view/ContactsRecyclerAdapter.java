package lightricks.yaakov.contacts.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lightricks.yaakov.contacts.Constants;
import lightricks.yaakov.contacts.R;
import lightricks.yaakov.contacts.model.entities.ContactEntry;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.CardItem> {

    private final List<ContactEntry> items;
    private final View.OnClickListener onItemClicked;

    public ContactsRecyclerAdapter(List<ContactEntry> items, View.OnClickListener onItemClicked) {
        this.items = items;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public CardItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        root.setOnClickListener(onItemClicked);
        return new CardItem(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CardItem holder, int position) {
        ContactEntry contactItem = items.get(position);
        holder.itemView.setTag(contactItem);
        holder.labelName.setText(contactItem.name());
        holder.labelName.setTransitionName(Constants.KEY_LABEL_TRANSITION + contactItem.id());
        holder.thumbnail.setTransitionName(Constants.KEY_PROFILE_TRANSITION + contactItem.id());
        Context context = holder.itemView.getContext();
        Glide
                .with(context)
                .load(contactItem.getThumbnailUri(context.getResources()))
                .into(holder.thumbnail);

    }

    @Override
    public long getItemId(int position) {
        return items.get(position).id();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CardItem extends RecyclerView.ViewHolder {

        private final TextView labelName;
        private final ImageView thumbnail;

        CardItem(@NonNull View itemView) {
            super(itemView);
            labelName = itemView.findViewById(R.id.label_name);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
