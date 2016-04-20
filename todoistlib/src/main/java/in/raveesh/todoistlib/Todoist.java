package in.raveesh.todoistlib;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;

import in.raveesh.todoistlib.model.Sync;
import in.raveesh.todoistlib.retrofitServices.TodoistSyncService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class to perform required Todoist API calls
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

    private static HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.BASIC;
    private static Retrofit retrofit = null;

    /**
     * Begin Oauth process. This will launch OAuthActivity, which will handle all the work. Once complete,
     * onResult will be called which will inform calling Activity whether the process was successful or not
     *
     * @param activity Calling activity
     * @param clientId Your app's client ID
     * @param scope Permission scope
     * @param state A secret string of yours. If the state returned by Todoist is not the same, it means there was an error in between
     * @param clientSecret Your app's client secret
     * @param requestCode Request code for you to use with onActivityResult
     */
    public static void beginAuth(@NonNull Activity activity, @NonNull String clientId, @NonNull String scope, @NonNull String state, @NonNull String clientSecret, int requestCode) {
        Intent intent = new Intent(activity, OAuthActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        intent.putExtra(EXTRA_SCOPE, scope);
        intent.putExtra(EXTRA_STATE, state);
        intent.putExtra(EXTRA_CLIENT_SECRET, clientSecret);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Sets the logging level for Retrofit calls. Call this before any other Todoist calls are made
     * @param level Level to set logging to. Level.BASIC by default
     */
    public static void setApiCallLoggingLevel(HttpLoggingInterceptor.Level level) {
        loggingLevel = level;
    }

    /**
     * Gets the current logging level
     * @return
     */
    public static HttpLoggingInterceptor.Level getApiCallLoggingLevel() {
        return loggingLevel;
    }

    /**
     * Gets an instance of the Retrofit object being used by the EasyDoist library
     * @return
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(Todoist.getApiCallLoggingLevel());
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://todoist.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Perform a sync. Currently only supports items
     * @param token Access Token
     * @param seqNo Sequence number as defined by Todoist Documentation
     * @param types Types to be returned. Currently only supports items
     */
    public static void sync(String token, int seqNo, JSONArray types){
        TodoistSyncService syncService = getRetrofit().create(TodoistSyncService.class);
        Call<Sync> call = syncService.sync(token, seqNo, types);
        call.enqueue(new Callback<Sync>() {
            @Override
            public void onResponse(Call<Sync> call, Response<Sync> response) {
                Log.d("sync", "success");
            }

            @Override
            public void onFailure(Call<Sync> call, Throwable t) {
                Log.d("sync", "failure");
            }
        });
    }
}
