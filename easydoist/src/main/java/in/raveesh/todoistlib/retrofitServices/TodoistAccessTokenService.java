package in.raveesh.todoistlib.retrofitServices;

import in.raveesh.todoistlib.model.TodoistTokenResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Raveesh on 20/04/16.
 */
public interface TodoistAccessTokenService {
    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<TodoistTokenResponse> getToken(@Field("client_id") String clientId,
                                        @Field("client_secret") String clientSecret,
                                        @Field("code") String code);
}
