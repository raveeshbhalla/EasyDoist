package in.raveesh.todoistlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import in.raveesh.todoistlib.model.TodoistTokenResponse;
import in.raveesh.todoistlib.retrofitServices.TodoistAccessTokenService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OAuthActivity extends AppCompatActivity {

    private String clientId;
    private String state;
    private String clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        clientId = getIntent().getStringExtra(EasyDoist.EXTRA_CLIENT_ID);
        String scope = getIntent().getStringExtra(EasyDoist.EXTRA_SCOPE);
        state = getIntent().getStringExtra(EasyDoist.EXTRA_STATE);
        clientSecret = getIntent().getStringExtra(EasyDoist.EXTRA_CLIENT_SECRET);
        String url = String.format(Locale.ENGLISH, "https://todoist.com/oauth/authorize?client_id=%s&scope=%s&state=%s", clientId, scope, state);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.contains("appsculture.com")) {
                    loginComplete(url);
                    return true;
                } else {
                    return false;
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                OAuthActivity.this.setProgress(progress * 1000);
            }
        });
        webView.loadUrl(url);
    }

    private void loginComplete(String url) {
        try {
            Map<String, String> query = splitQuery(new URL(url));
            if (query.containsKey("error")) {
                onError(query.get("error"));
            } else if (query.containsKey("code") && query.containsKey("state")) {
                if (!state.equals(query.get("state"))) {
                    /**
                     * Something fishy
                     */
                    onError(EasyDoist.ERROR_STATE_MISMATCH);
                } else {
                    /**
                     * Get Access Token
                     */
                    getAccessToken(query.get("code"));
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }


    private void getAccessToken(String code){

        TodoistAccessTokenService service = EasyDoist.getRetrofit().create(TodoistAccessTokenService.class);
        Call<TodoistTokenResponse> call = service.getToken(clientId, clientSecret, code);
        call.enqueue(new Callback<TodoistTokenResponse>() {
            @Override
            public void onResponse(Call<TodoistTokenResponse> call, Response<TodoistTokenResponse> response) {
                if (response.body() != null) {
                    if (response.body().error == null) {
                        onSuccess(response.body().access_token);
                    } else {
                        onError(response.body().error);
                    }
                }
                else {
                    onError(EasyDoist.ERROR_ACCESS_TOKEN_ERROR);
                }
            }

            @Override
            public void onFailure(Call<TodoistTokenResponse> call, Throwable t) {
                onError(EasyDoist.ERROR_ACCESS_TOKEN_ERROR);
            }
        });
    }

    public void onSuccess(String accessToken) {
        Intent intent = new Intent(EasyDoist.ACTION_TODOIST_RESULT);
        intent.putExtra(EasyDoist.EXTRA_ACCESS_TOKEN, accessToken);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    private void onError(String error) {
        Intent intent = new Intent(EasyDoist.ACTION_TODOIST_RESULT);
        intent.putExtra(EasyDoist.EXTRA_ERROR, error);
        setResult(RESULT_CANCELED, intent);
        this.finish();
    }

}
