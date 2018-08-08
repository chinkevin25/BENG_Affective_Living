package ExtraUtils.DataStructure.NeuroScale;

/**
 * Created by LinZh on 2/12/2016.
 */

import org.json.simple.JSONObject;

/**
 * the base class of NeuroScale data structure
 */
public class NEObject {
    protected Object _self;
    protected JSONObject _json;

    public NEObject(JSONObject json)
    {
        this._json = json;
        this._self = new Object();
    }
}
