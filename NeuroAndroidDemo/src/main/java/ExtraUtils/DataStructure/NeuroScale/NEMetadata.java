package ExtraUtils.DataStructure.NeuroScale;

import java.util.ArrayList;

/**
 * Created by LinZh on 2/12/2016.
 */
public class NEMetadata {
    public ArrayList<NENode> mInNode;
    public ArrayList<NENode> mOutNode;
//    private JSONObject mJson;
    public NEMetadata()
    {
        mInNode = new ArrayList<>();
        mOutNode = new ArrayList<>();
    }
//    public NEMetadata(JSONObject json)
//    {
//        mJson = json;
//    }

//    /**
//     * get the node of json object
//     * @param strNode
//     * @return
//     */
//    public NENode getNode(String strNode)
//    {
//        NENode node = null;
//        switch (strNode)
//        {
//            case "in":
//                node = new NENode();
//                node.mName =
//                break;
//            case "out":
//                break;
//            default:
//                node = null;
//                break;
//        }
//        return node;
//    }
}
