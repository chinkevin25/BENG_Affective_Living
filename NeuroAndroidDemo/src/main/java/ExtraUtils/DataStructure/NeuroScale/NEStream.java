package ExtraUtils.DataStructure.NeuroScale;

import java.util.ArrayList;

/**
 * Created by LinZh on 2/12/2016.
 */
public class NEStream {
//{
//    "channels": [
//    {
//        "label": "Concentration"
//    }
//    ],
//    "name": "EEG",
//        "sampling_rate": 0,
//        "type": "Custom"
//}
    public ArrayList<NEChannel> mChannels;
    public String mName;
    public Double mSampleRate;
//    public type custome
    public NEStream()
    {
        mChannels = new ArrayList<>();
    }

    /**
     * constructor
     * @param name
     * @param sample_rate
     */
    public NEStream(String name, Double sample_rate)
    {
        this.mChannels = new ArrayList<>();
        this.mName = name;
        this.mSampleRate = sample_rate;
    }

    /**
     * I didn't set sample_rate, since for android, when i try to parse samplign rate,like
     * (Long) ((JSONObject) streamObj).get("sampling_rate")); then when it is 250.0(Double)
     * it will fail. then I changed to (Double) ((JSONObject) streamObj).get("sampling_rate"));
     * when it is really 0, it will fail, since it is recognized as Long
     * @param name
     */
    public NEStream(String name)
    {
        this.mChannels = new ArrayList<>();
        this.mName = name;
        this.mSampleRate = 0.0;
    }
}
