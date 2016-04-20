package in.raveesh.todoistlibexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import in.raveesh.todoistlib.Todoist;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button begin = (Button)findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todoist.beginAuth(MainActivity.this, "ee6bfceaf4d344e295273f6e4eeb57fd", "data:read", "gibberish");
            }
        });
    }
}
