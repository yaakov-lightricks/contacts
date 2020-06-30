package lightricks.yaakov.contacts.model.repo;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

@SuppressWarnings("deprecation")
public abstract class ContentProviderLiveData<T> extends LiveData<T> {

    final Context context;
    private final Uri uri;
    private final ContentObserver contentObserver;

    protected ContentProviderLiveData(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
        this.contentObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                // Notify LiveData listeners an event has happened
                refresh();
            }
        };
    }

    /**
     * Implement if you need to provide [T] value to be posted
     * when observed content is changed.
     */
    abstract T getContentProviderValue();

    @Override
    protected void onActive() {
        context.getContentResolver().registerContentObserver(uri, true, contentObserver);
        refresh();
    }

    @Override
    protected void onInactive() {
        context.getContentResolver().unregisterContentObserver(contentObserver);
    }

    public void refresh(){
        //don't want to block ui
        AsyncTask.SERIAL_EXECUTOR.execute(() -> postValue(getContentProviderValue()));
    }
}
