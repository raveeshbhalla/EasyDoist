package in.raveesh.todoistlibexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import in.raveesh.todoistlib.Todoist;

public class MainActivity extends AppCompatActivity {

    private final static int TODOIST_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button begin = (Button)findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todoist.beginAuth(MainActivity.this, "ee6bfceaf4d344e295273f6e4eeb57fd", "data:read", "gibberish", TODOIST_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODOIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            }
            else if (resultCode == RESULT_CANCELED){

            }
        }
    }
}
