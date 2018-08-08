package ExtraUtils.DataStructure.NeuroScale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by LinZh on 2/12/2016.
 */
public class NEParser {
    private JSONObject mJson; /**the jsonobject that should be parsed*/
    public NEMetadata mMetaData;

    /**
     * load json to current parsing work
     * @param json
     */
    public NEParser(JSONObject json)
    {
        this.mJson = json;
        mMetaData = getMetaData(this.mJson);
    }

    /**
     * to retrieve all the output channels with their name.
     * @return
     */
    public ArrayList<String> getOutNodeName()
    {
        ArrayList<String> names = new ArrayList<String>();
        if (this.mMetaData != null)
        {
            for(NENode node : this.mMetaData.mOutNode)
            {
                names.add(node.mName);
            }
        }
        return names;
    }
//    public

    /**
     * Get the endpoint from json object
     * @param json
     * @return
     */
    private NEEndpoint getEndpoint(JSONObject json) {
        NEEndpoint endpoint = null;// returned endpoint

        try {
            // get the endpoints
            JSONObject endpoints = (JSONObject) (json.get("endpoints"));
            // get data
            JSONArray data = (JSONArray) endpoints.get("data");

            // get properties of endpoints
            endpoint = new NEEndpoint();
            endpoint.mUrl = (String) (endpoints.get("url"));
            endpoint.mCount = (int) (endpoints.get("count"));
//          endpoint.mObject = endpoints.get("object");// we don't need it right now

            for (Object e : data) {
                NEEndpointData endpoint_data = new NEEndpointData();
                //get properties
                JSONObject jsonOBJ = (JSONObject) e;
                endpoint_data.mMode = (String) jsonOBJ.get("mode");
                endpoint_data.mUrl = (String)jsonOBJ.get("url");
                endpoint_data.mID = (String)jsonOBJ.get("id");
                endpoint_data.mInstanceID = (String)jsonOBJ.get("instance");
                //add to endpoint
                endpoint.mData.add(endpoint_data);
            }
        }catch (Exception e)
        {
            endpoint = null;
        }
        return endpoint;
    }

    /**
     * get the metadata from JSONObject
     * @param json
     * @return
     */
    private NEMetadata getMetaData(JSONObject json)
    {
        NEMetadata metadata = null;
        try {
            metadata = new NEMetadata();
            JSONObject metaJson = (JSONObject) (json.get("metadata"));
            metadata.mOutNode = getOutputNode(metaJson);
            metadata.mInNode = getInputNode(metaJson);
        }catch (Exception e)
        {
            metadata = null;
        }
        return metadata;
    }

    /**
     * get the node from json object
     * @param metaJson
     * @param mode : in or out, for input and output
     * @return
     */
    private ArrayList<NENode> getNode(JSONObject metaJson, String mode)
    {
        ArrayList<NENode> nodes = null;
        try {
            //JSONObject dataJson = (JSONObject) (metaJson.get("metadata"));
            JSONObject nodeJson = (JSONObject)(metaJson.get("nodes"));
            JSONArray out_node = (JSONArray)(nodeJson.get(mode));
            // to new a arrylist, and add all the nodes to our Configuration
            if (out_node.size() >0 ) {
                nodes = new ArrayList<>();
                for (Object nodeObj : out_node)
                {
                    NENode node = new NENode();
                    node.mName = (String) ((JSONObject) nodeObj).get("name"); // get the node name
                    //get the streams
                    JSONArray streams = (JSONArray)((JSONObject) nodeObj).get("streams");
                    for (Object streamObj: streams) {
                        NEStream stream = new NEStream(
                                (String) ((JSONObject) streamObj).get("name")//get stream name
                                /*(Double) ((JSONObject) streamObj).get("sampling_rate")*/);// get the sample rate of this stream
                        //get the channels
                        JSONArray channels = (JSONArray) ((JSONObject) streamObj).get("channels");
                        for (Object channelObj : channels) {
                            stream.mChannels.add(new NEChannel((String) ((JSONObject) channelObj).get("label")));
                        }
                        node.mStreams.add(stream);
                    }
                    nodes.add(node);
                }
            }
        }
        catch (Exception e)
        {
            nodes = null;
        }
        return nodes;
    }
    /**
     * get the output node from json(should be metadata)
     * @return
     */
    private ArrayList<NENode> getOutputNode(JSONObject metaJson)
    {
        return getNode(metaJson,"out");
    }
    /**
     * get the input node from json(should be metadata)
     * @param metaJson
     * @return
     */
    private ArrayList<NENode> getInputNode(JSONObject metaJson)
    {
        return getNode(metaJson,"in");
    }
}
