package in.raveesh.todoistlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        String URL = getIntent().getDataString();
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.contains("appsculture.com")) {
                    Toast.makeText(LoggedInActivity.this, url, Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    return false;
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                LoggedInActivity.this.setProgress(progress * 1000);
            }
        });
        webView.loadUrl(URL);
    }

    private void loginComplete(String URL){

    }
}
