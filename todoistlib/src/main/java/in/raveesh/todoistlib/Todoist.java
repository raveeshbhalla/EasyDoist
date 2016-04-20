package in.raveesh.todoistlib;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Raveesh on 20/04/16.
 */
public class Todoist {
    public static final String EXTRA_CLIENT_ID = "client_id";
    public static final String EXTRA_SCOPE = "scope";
    public static final String EXTRA_STATE = "state";
    public static final String EXTRA_CLIENT_SECRET = "client_secret";

    public static final String ACTION_TODOIST_RESULT = "TODOIST_RESULT";
    public static final String EXTRA_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String EXTRA_ERROR = "ERROR";

    public static final String ERROR_STATE_MISMATCH = "state_mismatch";
    public static final String ERROR_ACCESS_TOKEN_ERROR = "access_token_call_error";

    public static void beginAuth(@NonNull Activity activity, @NonNull String clientId, @NonNull String scope, @NonNull String state, @NonNull String clientSecret, int requestCode) {
        Intent intent = new Intent(activity, OAuthActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        intent.putExtra(EXTRA_SCOPE, scope);
        intent.putExtra(EXTRA_STATE, state);
        intent.putExtra(EXTRA_CLIENT_SECRET, clientSecret);
        activity.startActivityForResult(intent, requestCode);
    }
}
