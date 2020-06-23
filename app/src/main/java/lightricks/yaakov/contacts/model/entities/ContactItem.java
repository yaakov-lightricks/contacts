package lightricks.yaakov.contacts.model.entities;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.Objects;

import lightricks.yaakov.contacts.R;

public class ContactItem implements Parcelable {

    private static Uri placeHolder;

    private final int id;
    @NonNull
    private final String lookupId;
    @Nullable
    private final String thumbnail_path;
    @NonNull
    private final String name;
    @Nullable
    private String handle;
    @Nullable
    private final String number;

    public ContactItem(int id, @NonNull String lookupId, @Nullable String thumbnail_path, @NonNull String name, @Nullable String number) {
        this.id = id;
        this.lookupId = lookupId;
        this.thumbnail_path = thumbnail_path;
        this.name = name;
        this.number = number;
    }

    public void setHandle(@Nullable String handle) {
        this.handle = handle;
    }

    public String getLookupId() {
        return lookupId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getHandle() {
        return handle;
    }

    @Nullable
    public String getNumber() {
        return number;
    }

    public Uri getThumbnailUri(Resources resources){
        if (thumbnail_path != null) {
            return Uri.parse(thumbnail_path);
        }
        synchronized (ContactItem.class){
            if (placeHolder == null) {
               placeHolder = new Uri.Builder()
                       .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                       .authority(resources.getResourcePackageName(R.drawable.profile_placeholder))
                       .appendPath(resources.getResourceTypeName(R.drawable.profile_placeholder))
                       .appendPath(resources.getResourceEntryName(R.drawable.profile_placeholder))
                       .build();
            }
        }
        return placeHolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactItem that = (ContactItem) o;
        return id == that.id &&
                lookupId.equals(that.lookupId) &&
                Objects.equals(thumbnail_path, that.thumbnail_path) &&
                name.equals(that.name) &&
                Objects.equals(handle, that.handle) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lookupId, thumbnail_path, name, handle, number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.lookupId);
        dest.writeString(this.thumbnail_path);
        dest.writeString(this.name);
        dest.writeString(this.handle);
        dest.writeString(this.number);
    }

    protected ContactItem(Parcel in) {
        this.id = in.readInt();
        this.lookupId = in.readString();
        this.thumbnail_path = in.readString();
        this.name = in.readString();
        this.handle = in.readString();
        this.number = in.readString();
    }

    public static final Parcelable.Creator<ContactItem> CREATOR = new Parcelable.Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel source) {
            return new ContactItem(source);
        }

        @Override
        public ContactItem[] newArray(int size) {
            return new ContactItem[size];
        }
    };
}
