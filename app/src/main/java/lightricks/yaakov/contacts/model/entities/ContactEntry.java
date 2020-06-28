package lightricks.yaakov.contacts.model.entities;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

import lightricks.yaakov.contacts.R;

@AutoValue
public abstract class ContactEntry implements Parcelable {

    public static ContactEntry create(int id, String lookupId, String thumbnail_path, String name, String email, String number, boolean isHidden) {
        return new AutoValue_ContactEntry(id, lookupId, thumbnail_path, name, email, number, isHidden);
    }

    private static Uri placeHolder;

    public abstract int id();

    public abstract String lookupId();

    @Nullable
    public abstract String thumbnail();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String email();

    @Nullable
    public abstract String number();

    public abstract boolean isHidden();


    public Uri getThumbnailUri(Resources resources){
        if (thumbnail() != null) {
            return Uri.parse(thumbnail());
        }
        synchronized (ContactEntry.class){
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


}
