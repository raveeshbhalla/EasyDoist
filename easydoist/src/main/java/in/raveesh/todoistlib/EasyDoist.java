package in.raveesh.todoistlib;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import in.raveesh.todoistlib.model.ItemCommand;
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
 * Helper class to perform required EasyDoist API calls
 */
public class EasyDoist {
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
     * @param activity     Calling activity
     * @param clientId     Your app's client ID
     * @param scope        Permission scope
     * @param state        A secret string of yours. If the state returned by EasyDoist is not the same, it means there was an error in between
     * @param clientSecret Your app's client secret
     * @param requestCode  Request code for you to use with onActivityResult
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
     * Sets the logging level for Retrofit calls. Call this before any other EasyDoist calls are made
     *
     * @param level Level to set logging to. Level.BASIC by default
     */
    public static void setApiCallLoggingLevel(HttpLoggingInterceptor.Level level) {
        loggingLevel = level;
    }

    /**
     * Gets the current logging level
     *
     * @return
     */
    public static HttpLoggingInterceptor.Level getApiCallLoggingLevel() {
        return loggingLevel;
    }

    /**
     * Gets an instance of the Retrofit object being used by the EasyDoist library
     *
     * @return
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(EasyDoist.getApiCallLoggingLevel());
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
     *
     * @param token    Access Token
     * @param seqNo    Sequence number as defined by ToDoist Documentation
     * @param types    Types to be returned. Currently only supports items
     * @param callback Retrofit async callback when sync is complete
     */
    public static void sync(@NonNull String token, @IntRange(from = 0) long seqNo, @NonNull JSONArray types, @NonNull Callback<Sync> callback) {
        TodoistSyncService syncService = getRetrofit().create(TodoistSyncService.class);
        Call<Sync> call = syncService.sync(token, seqNo, types);
        call.enqueue(callback);
    }

    /**
     * Performs a sync, with a JsonObject callback. Useful if you want raw response back or to use with other objects
     *
     * @param token Access Token
     * @param seqNo Sequence number as defined by Todoist documentation
     * @param types Types to be returned. All types can be used here
     * @param callback Retrofit async callback
     */
    public static void rawSync(@NonNull String token, @IntRange(from = 0)long seqNo, @NonNull JSONArray types, @NonNull Callback<JsonObject> callback){
        TodoistSyncService syncService = getRetrofit().create(TodoistSyncService.class);
        Call<JsonObject> call = syncService.rawSync(token, seqNo, types);
        call.enqueue(callback);
    }

    /**
     * Mark a particular task complete
     * @param token Access Token
     * @param id ID of the task
     * @param uuid A custom String that you define. Must be unique each time, so as to ensure you don't perform same task twice
     * @param callback Callback for retrofit
     */
    public static void markComplete(@NonNull String token, @IntRange(from = 0) int id, @NonNull String uuid, @NonNull Callback<JsonObject> callback) throws JSONException {
        TodoistSyncService syncService = getRetrofit().create(TodoistSyncService.class);
        List<ItemCommand> commands = new ArrayList<>();
        commands.add(ItemCommand.getCompleteCommand(uuid, id));
        JSONArray array = new JSONArray(new Gson().toJson(commands));
        Call<JsonObject> call = syncService.markComplete(token, array);
        call.enqueue(callback);
    }
}
