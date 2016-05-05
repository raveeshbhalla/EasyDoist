package in.raveesh.todoistlibexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import in.raveesh.todoistlib.EasyDoist;
import in.raveesh.todoistlib.model.Sync;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static int TODOIST_REQUEST_CODE = 12;
    SharedPreferences prefs;
    Button begin;

    int firstId;
    boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("todoist", MODE_PRIVATE);
        begin = (Button) findViewById(R.id.begin);
        EasyDoist.setApiCallLoggingLevel(HttpLoggingInterceptor.Level.BODY);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loggedIn) {
                    EasyDoist.beginAuth(MainActivity.this, "ee6bfceaf4d344e295273f6e4eeb57fd", "data:read_write", "gibberish", "7c12327f529e49c89d93e6ea7f97e77e", TODOIST_REQUEST_CODE);
                }
                else{
                    try {
                        EasyDoist.markComplete(getToken(), firstId, "okamsodmaoiamsdomaoskmdosdmk", new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.d("Test", response.body().toString());
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if (getToken() != null) {
            sync(prefs.getString(EasyDoist.EXTRA_ACCESS_TOKEN, null));
        }
    }

    private String getToken(){
        return prefs.getString(EasyDoist.EXTRA_ACCESS_TOKEN, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODOIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String token = data.getStringExtra(EasyDoist.EXTRA_ACCESS_TOKEN);
                Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                prefs.edit().putString(EasyDoist.EXTRA_ACCESS_TOKEN, token).apply();
                sync(token);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, data.getStringExtra(EasyDoist.EXTRA_ERROR), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sync(String token) {
        EasyDoist.rawSync(token, 0, new JSONArray().put("items"), new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    Sync sync = new Gson().fromJson(response.body(), Sync.class);
                    Log.d("sync", "Number of items:" + sync.getItems().size());
                    if (sync.getItems().size() > 0){
                        firstId = sync.getItems().get(0).id;
                        loggedIn = true;
                        begin.setText("Mark Complete");
                        Log.d("first", sync.getItems().get(0).content);
                    }
                } else {
                    Log.e("sync", "Error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("sync", "Error");
            }
        });
    }
}
