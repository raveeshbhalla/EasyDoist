package in.raveesh.todoistlib.retrofitServices;

import org.json.JSONArray;

import in.raveesh.todoistlib.model.Sync;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Raveesh on 20/04/16.
 */
public interface TodoistSyncService {
    @FormUrlEncoded
    @POST("API/v6/sync")
    Call<Sync> sync(@Field("token") String token,
                    @Field("seq_no") int seq_no,
                    @Field("resource_types") JSONArray types);
}
