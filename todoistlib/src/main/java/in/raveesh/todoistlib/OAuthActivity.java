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

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class OAuthActivity extends AppCompatActivity {

    private String clientId;
    private String state;
    private String clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        clientId = getIntent().getStringExtra(Todoist.EXTRA_CLIENT_ID);
        String scope = getIntent().getStringExtra(Todoist.EXTRA_SCOPE);
        state = getIntent().getStringExtra(Todoist.EXTRA_STATE);
        clientSecret = getIntent().getStringExtra(Todoist.EXTRA_CLIENT_SECRET);
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
                    onError(Todoist.ERROR_STATE_MISMATCH);
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://todoist.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TodoistService service = retrofit.create(TodoistService.class);
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
                    onError(Todoist.ERROR_ACCESS_TOKEN_ERROR);
                }
            }

            @Override
            public void onFailure(Call<TodoistTokenResponse> call, Throwable t) {
                onError(Todoist.ERROR_ACCESS_TOKEN_ERROR);
            }
        });
    }

    public void onSuccess(String accessToken) {
        Intent intent = new Intent(Todoist.ACTION_TODOIST_RESULT);
        intent.putExtra(Todoist.EXTRA_ACCESS_TOKEN, accessToken);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    private void onError(String error) {
        Intent intent = new Intent(Todoist.ACTION_TODOIST_RESULT);
        intent.putExtra(Todoist.EXTRA_ERROR, error);
        setResult(RESULT_CANCELED, intent);
        this.finish();
    }

    public interface TodoistService {
        @FormUrlEncoded
        @POST("oauth/access_token")
        Call<TodoistTokenResponse> getToken(@Field("client_id") String clientId,
                                            @Field("client_secret") String clientSecret,
                                            @Field("code") String code);
    }

    public class TodoistTokenResponse{
        String access_token;
        String error;
    }
}
