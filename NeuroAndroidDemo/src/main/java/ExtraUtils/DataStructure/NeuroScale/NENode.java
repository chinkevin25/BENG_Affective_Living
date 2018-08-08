package ExtraUtils.DataStructure.NeuroScale;

import java.util.ArrayList;

/**
 * Created by LinZh on 2/12/2016.
 */
public class NENode {
    public String mName;    // name of the node
    public ArrayList<NEStream> mStreams;
    //
//    {
//        "name": "default",
//        "streams": [
//        {
//            "channels": [
//            {
//                "label": "Concentration"
//            }
//            ],
//            "name": "EEG",
//                "sampling_rate": 0,
//                "type": "Custom"
//        }
//        ]
//    },
    public NENode()
    {
        mStreams = new ArrayList<NEStream>();
    }
}
