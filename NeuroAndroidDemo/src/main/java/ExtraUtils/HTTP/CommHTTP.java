package ExtraUtils.HTTP;

import android.util.Log;

import org.json.simple.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by linzhang on 12/8/2015.
 */
public class CommHTTP {
    private String JsonData;
    private int ResponseCode;
    public enum OperaState{
        ERROR,
        IDEL,
        START,
        END
    }
    private OperaState curState = OperaState.IDEL;
    private OkHttpClient client = new OkHttpClient();

    /**
     * put value to specificed url
     * @param url
     * @param access_token
     * @param json
     * @return
     */
    public int AsynPUT(String url, String access_token, JSONObject json) {
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + access_token)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .put(body)
                .build();

        this.curState = OperaState.START;     //make sure request is finished
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                curState = OperaState.ERROR;
                JsonData = null;
            }

            @Override
            public void onResponse(Call call, Response response)  {
                if (response.isSuccessful()){
                    try {
                        JsonData = response.body().string();
                        ResponseCode = response.code();
                        curState = OperaState.END;
                    } catch (IOException e) {
                        Log.i("CommHTTP", "Unexpected code " + response.code());
                    }
                }
            }
        });

        while (this.curState == OperaState.START);
        this.curState = OperaState.IDEL;

        return ResponseCode;
    }
    /**
     * Get HTTP
     * @param url
     * @param access_token
     */
    public String AsynGet(String url, String access_token)
    {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + access_token)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        this.curState = OperaState.START;     //make sure request is finished
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                curState = OperaState.ERROR;
                JsonData = null;
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        JsonData = response.body().string();
                        ResponseCode = response.code();
                        curState = OperaState.END;
                    }catch (IOException e){
                        Log.i("CommHTTP", "Unexpected code " + response.code());
                    }
                }
            }
        });

        while (this.curState == OperaState.START);
        this.curState = OperaState.IDEL;
        return this.JsonData;
    }

    /**
     * AsynDelete a resource and return the response code
     * @param url
     * @param access_token
     * @param intsance
     * @return
     */
    public int AsynDelete(String url, String access_token, String intsance) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "");
        url = url + "/" + intsance;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + access_token)
                .addHeader("Content-Length","0")
                .delete()
                .build();

        this.curState = OperaState.START;     //make sure request is finished
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                curState = OperaState.ERROR;
                JsonData = null;
            }

            @Override
            public void onResponse(Call call, Response response)  {
                if (response.isSuccessful()) {
                    try {
                        JsonData = response.body().string();
                        ResponseCode = response.code();
                        curState = OperaState.END;
                    } catch (IOException e){
                        Log.i("CommHTTP", "Unexpected code " + response.code());
                    }
                }
            }
        });

        while (this.curState == OperaState.START);
        this.curState = OperaState.IDEL;

        return ResponseCode;
    }

    /**
     * Patch neuroscale
     * @param url
     * @param intsance
     * @param access_token
     * @param json
     * @return
     */
    public String AsynPatch(String url,String intsance, String access_token, JSONObject json) {
        url = url + "/" + intsance;
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + access_token)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build();

        this.curState = OperaState.START;     //make sure request is finished
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                curState = OperaState.ERROR;
                JsonData = null;
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        JsonData = response.body().string();
                        ResponseCode = response.code();
                        curState = OperaState.END;
                    }catch (IOException e){
                        Log.i("CommHTTP", "Unexpected code " + response.code());
                    }
                }
            }
        });

        while (this.curState == OperaState.START);
        this.curState = OperaState.IDEL;

        return JsonData;
    }
    /**
     * post http
     * @param url
     * @param access_token
     * @param JSON
     */
    public static final MediaType JSON
            = MediaType.parse("application/json");
    public String AsynPost(String url, String access_token, JSONObject json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + access_token)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        //Response response = this.client.newCall(request).execute();
        this.curState = OperaState.START;     //make sure request is finished
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                curState = OperaState.ERROR;
                JsonData = null;
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        JsonData = response.body().string();
                        ResponseCode = response.code();
                        curState = OperaState.END;
                    }catch (IOException e){
                        Log.i("CommHTTP", "Unexpected code " + response);
                    }
                }
            }
        });

        while (this.curState == OperaState.START);
        this.curState = OperaState.IDEL;
        return this.JsonData;
    }
}
