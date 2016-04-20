package in.raveesh.todoistlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by Raveesh on 20/04/16.
 */
public class Todoist {

    public static void beginAuth(@NonNull Context context,@NonNull String clientId,@NonNull String scope,@NonNull String state) {
        String url = String.format(Locale.ENGLISH, "https://todoist.com/oauth/authorize?client_id=%s&scope=%s&state=%s", clientId, scope, state);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
        Bundle extras = new Bundle();
        extras.putBinder(EXTRA_CUSTOM_TABS_SESSION, null);
        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
