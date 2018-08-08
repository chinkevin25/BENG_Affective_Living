package ExtraUtils.DataStructure.NeuroScale;

import java.util.ArrayList;

/**
 * Created by LinZh on 2/12/2016.
 */
public class NEEndpoint {
//    "count": 2,
//    "data": [
//            {
//                "created_at": 1455316750,
//                    "id": "ep_U5Gc9GQg4esdGKCHx6o6kD",
//                    "instance": "in_zdRwdWDdT3zWMJvaCwjMG9",
//                    "mode": "write",
//                    "object": "endpoint",
//                    "updated_at": 1455316750,
//                    "url": "mqtt://streaming.neuroscale.io:443/3Bp2mvMxjC5xHD2muqKLdX/in"
//            },
//            {
//                "created_at": 1455316750,
//                    "id": "ep_PagPijG8ux93R2BxvgYrHJ",
//                    "instance": "in_zdRwdWDdT3zWMJvaCwjMG9",
//                    "mode": "read",
//                    "object": "endpoint",
//                    "updated_at": 1455316750,
//                    "url": "mqtt://streaming.neuroscale.io:443/3Bp2mvMxjC5xHD2muqKLdX/out"
//            }
//    ],
//    "object": "list",
//    "url": "/v1/instances/in_zdRwdWDdT3zWMJvaCwjMG9/endpoints"
    public int mCount;   // count of endpoints
    public String mUrl;  // URL of the endpoints
    public ArrayList<NEEndpointData> mData;
    public NEType mObject;

    public NEEndpoint()
    {
        mData = new ArrayList<NEEndpointData>();
        mObject = NEType.List;
    }
}
