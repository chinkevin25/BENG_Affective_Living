package ExtraUtils.DataStructure.NeuroScale;

/**
 * Created by LinZh on 2/12/2016.
 */
public class NEEndpointData {
//    "created_at": 1455316750,
//    "id": "ep_U5Gc9GQg4esdGKCHx6o6kD",
//    "instance": "in_zdRwdWDdT3zWMJvaCwjMG9",
//    "mode": "write",
//    "object": "endpoint",
//    "updated_at": 1455316750,
//    "url": "mqtt://streaming.neuroscale.io:443/3Bp2mvMxjC5xHD2muqKLdX/in"
//    public long TimeInt;    // created_at
    public String mID;       // id
    public String mInstanceID;   // instance
    public String mMode;     //
    public NEType mObject;
    public String mUrl;

    public NEEndpointData()
    {
        mObject = NEType.Endpoint;
    }
}
