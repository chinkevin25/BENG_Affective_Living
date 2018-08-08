package ExtraUtils.HTTP;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;

import ExtraUtils.Configuration;
import ExtraUtils.DataStructure.NeuroInstance;
import ExtraUtils.DataStructure.NeuroPipeLine;


/**
 * Created by linzhang on 12/8/2015.
 */
public class HttpManager {
    public CommHTTP mGerneralHttp;

    public static org.json.simple.JSONObject Str2JSON(String str)
    {
        JSONObject Jobject = (JSONObject) JSONValue.parse(str);
        return Jobject;
    }

    /**
     * Init manager
     */
    public HttpManager()
    {
        mGerneralHttp = new CommHTTP(); // this will be used in activity when it is neccessary
    }

    public static ArrayList<NeuroPipeLine> GetPipelines()
    {
        CommHTTP mPipelineHttp = new CommHTTP();
        String url = Configuration.api_url + "/v1/pipelines";
        ArrayList<NeuroPipeLine> mPipelines = new ArrayList<>();
        // Get json
        String json = mPipelineHttp.AsynGet(url, Configuration.access_token);
        if(json == null){
            //Toast.makeText(HttpManager.this, "", Toast.LENGTH_SHORT).show();
            Log.i("Neuroscale", "There is no return from neuroscale");
            return null;
        }
        // Json Object
        JSONObject Jobject = (JSONObject) JSONValue.parse(json);
        if (Jobject != null) {
            JSONArray lines = (JSONArray) Jobject.get("data");
            if (lines != null) {
                for (Object pipeline : lines) {
                    String name = (String) ((JSONObject) pipeline).get("name");
                    String id = (String) ((JSONObject) pipeline).get("id");

                    mPipelines.add(new NeuroPipeLine(id, name));
                }
                return mPipelines;
            }
        }
        return null;
    }


//    /**
//     * Get all instances
//     * @return
//     */
//    public ArrayList<NeuroInstance> GetInstances()
//    {
//        ArrayList<NeuroInstance> mInstances = new ArrayList<>();
//        String url = Configuration.api_url + "/v1/instances";
//        // Get json
//        String json = mInstanceHttp.AsynGet(url, Configuration.access_token);
//        if(json == null){
//            //Toast.makeText(HttpManager.this, "", Toast.LENGTH_SHORT).show();
//            Log.i("Neuroscale", "There is no return from neuroscale");
//            return null;
//        }
//        // Json Object
//        JSONObject Jobject = (JSONObject) JSONValue.parse(json);
//        if (Jobject != null) {
//            JSONArray instances = (JSONArray) Jobject.get("data");
//            if (instances != null)
//            {
//                for (Object instance : instances)
//                {
//                    JSONObject in_json = (JSONObject) instance;
//                    String id = (String) in_json.get("id");
//                    String pipe_id = (String) in_json.get("pipeline");
//                    String state = (String) in_json.get("state");
//                    JSONObject properties = (JSONObject)in_json.get("properties");
//                    String tag =  (String)properties.get("tag");
//                    // add to current instant to pipeline that belonged
//                    mInstances.add(new NeuroInstance(id, tag, state, pipe_id));
//                }
//                return mInstances;
//            }
//        }
//        return null;
//    }

    /**
     * Get all instances
     * @return
     */
    public static ArrayList<NeuroInstance> GetInstances()
    {
        CommHTTP mInstanceHttp = new CommHTTP();
        ArrayList<NeuroInstance> mInstances = new ArrayList<>();
        String url = Configuration.api_url + "/v1/instances";
        // Get json
        String json = mInstanceHttp.AsynGet(url, Configuration.access_token);
        if(json == null){
            //Toast.makeText(HttpManager.this, "", Toast.LENGTH_SHORT).show();
            Log.i("Neuroscale", "There is no return from neuroscale");
            return null;
        }
        // Json Object
        JSONObject Jobject = (JSONObject) JSONValue.parse(json);
        if (Jobject != null) {
            JSONArray instances = (JSONArray) Jobject.get("data");
            if (instances != null)
            {
                for (Object instance : instances)
                {
                    JSONObject in_json = (JSONObject) instance;
                    String id = (String) in_json.get("id");
                    String pipe_id = (String) in_json.get("pipeline");
                    String state = (String) in_json.get("state");
                    JSONObject properties = (JSONObject)in_json.get("properties");
                    String tag =  (String)properties.get("tag");
                    // add to current instant to pipeline that belonged
                    mInstances.add(new NeuroInstance(id, tag, state, pipe_id));
                }
                return mInstances;
            }
        }
        return null;
    }
}
