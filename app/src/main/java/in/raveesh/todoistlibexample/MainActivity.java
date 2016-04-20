package in.raveesh.todoistlibexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;

import in.raveesh.todoistlib.Todoist;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private final static int TODOIST_REQUEST_CODE = 12;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("todoist", MODE_PRIVATE);
        Button begin = (Button)findViewById(R.id.begin);
        Todoist.setApiCallLoggingLevel(HttpLoggingInterceptor.Level.BODY);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todoist.beginAuth(MainActivity.this, "ee6bfceaf4d344e295273f6e4eeb57fd", "data:read", "gibberish", "7c12327f529e49c89d93e6ea7f97e77e", TODOIST_REQUEST_CODE);
            }
        });
        if(prefs.getString(Todoist.EXTRA_ACCESS_TOKEN, null) != null) {
            sync(prefs.getString(Todoist.EXTRA_ACCESS_TOKEN, null));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODOIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String token = data.getStringExtra(Todoist.EXTRA_ACCESS_TOKEN);
                Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                prefs.edit().putString(Todoist.EXTRA_ACCESS_TOKEN, token).apply();
                sync(token);
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, data.getStringExtra(Todoist.EXTRA_ERROR), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sync(String token){
        Todoist.sync(token, 0, new JSONArray().put("items"));
    }
}
