package ExtraUtils;

import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

import ExtraUtils.DataStructure.NeuroScale.NENode;
import ExtraUtils.DataStructure.UserAccount;


/**
 * Created by Administrator on 2015-12-06.
 */
public class Configuration {
    public static String api_url = "https://api.neuroscale.io";
    public static String access_token = "";
    /**
     * added Feb 12, 2016, zhanglin
     * function, to store the information about which output node is
     * being retrieved right now
     * */
    //public static ArrayList<String> output_nodes = new ArrayList<>();
    public static ArrayList<NENode> output_nodes = new ArrayList<>();
    public static int curNodeIdx = 0; // current retrieved node
    public static URLUtils read_endpoint = null;
    /**
     * added by Feb 12, 2016 zhanglin
     * function, set a choice of received message formation
     * parameter pack_json_msgpack is true, using JSON, otherwise using MESSAGEPACK
     */
    public static boolean pack_json_msgpack = true;

    /**indicates current page*/
    public static long ID_Configure = 1;
    public static long ID_Publish = 2;
    public static long ID_Subscribe = 3;
    public static long ID_AdvancedSetting = 4;
//    public static long PageIdentifer = 1;
    /**user account information*/
    public static ArrayList<UserAccount> users_account = new ArrayList<>();

//    /**Connection status*/
//    public static enum NEState
//    {
//        DISCONNECTED,
//
////        CONNECTING,
////        MUSE_CONNECTIING,
////        NEUROSCALE_CONNECTIING,
//
//        CONNECTED,
//        MUSE_CONNECTED,
//        NEUROSCALE_CONNECTED,
//    }
//    public static NEState PublishState = NEState.DISCONNECTED;
//    public static NEState SubscribeState = NEState.DISCONNECTED;
}
