package lightricks.yaakov.contacts.model.repo;

import android.app.Application;

import lightricks.yaakov.contacts.model.entities.ContactItem;

import java.util.List;

public class ContactRepoStorage implements ContactRepo {

    private final Application context;

    public ContactRepoStorage(Application context) {
        this.context = context;
    }

    @Override
    public List<ContactItem> getContacts() {
//        List<ContactItem> contacts = new ArrayList<>();
//        Uri imageUri = (new Uri.Builder())
//                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//                .authority(context.getResources().getResourcePackageName(R.drawable.profile))
//                .appendPath(context.getResources().getResourceTypeName(R.drawable.profile))
//                .appendPath(context.getResources().getResourceEntryName(R.drawable.profile))
//                .build();
//        contacts.add(new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, null, "user1", "handle1", "number1"));
//        contacts.add(new ContactItem(1, null, "user2", "handle2", "number2"));
//        contacts.add(new ContactItem(1, null, "user2", "handle2", "number2"));
        return ContactsRetriever.getAllContacts(context);
//        return contacts;
    }

    @Override
    public String getContactEmailById(String id) {
//        Uri imageUri = (new Uri.Builder())
//                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//                .authority(context.getResources().getResourcePackageName(R.drawable.profile))
//                .appendPath(context.getResources().getResourceTypeName(R.drawable.profile))
//                .appendPath(context.getResources().getResourceEntryName(R.drawable.profile))
//                .build();
//        return new ContactItem(1, imageUri.toString(), "user1", "handle1", "number1");
        return ContactsRetriever.getContactEmailById(context, id);
    }
}
