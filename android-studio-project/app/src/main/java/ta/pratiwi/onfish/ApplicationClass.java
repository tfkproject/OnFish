package ta.pratiwi.onfish;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Created by taufik on 23/07/18.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
