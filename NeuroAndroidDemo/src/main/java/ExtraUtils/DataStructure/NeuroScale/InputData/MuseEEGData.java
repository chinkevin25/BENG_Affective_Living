package ExtraUtils.DataStructure.NeuroScale.InputData;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ExtraUtils.ArrayUtils;
import ExtraUtils.Configuration;

/**
 * Created by LinZh on 2/25/2016.
 * this is the structure for muse EEG data
 * which will be input into neruscale
 */
public class MuseEEGData {
    private ArrayList<Double[]> mData;   /**eeg data, every smaple contains a Double[] */
    private int mDimension;              /**how mang channels there would be*/
    /**
     * return the size of samples
     */
    public int Size()
    {
        return this.mData.size();
    }
    /**
     * constructor
     */
    public MuseEEGData(int channels)
    {
        this.mData = new ArrayList<>();
        this.mDimension = channels;
    }

    /**
     * clone constructor
      * @param data
     */
    public MuseEEGData(MuseEEGData data)
    {
        this.mData = new ArrayList<>(data.mData);
        this.mDimension = data.mDimension;
    }

    /**
     * append a new smaple with exact channels
     * @param data
     */
    public void Append(Double[] data)
    {
        if (this.mDimension != data.length) return; /** make sure every data should be right*/
        this.mData.add(data);
    }

    /**
     * get ith sample
     * @param i
     * @return
     */
    public Double[] Get(int i)
    {
        return this.mData.get(i);
    }

    /**
     * clear data
     */
    public void Clear()
    {
        this.mData.clear();
    }

    public static long TIME0 = System.currentTimeMillis();
    /**
     * format eeg data to be meta-data which is cooresponding to that publish before sending
     * for details, go to TestData class
     * @return
     */
    public JSONObject toJSON(float sample_rate) {
        int chunk_size = this.mData.size();        /** get sample size*/
        if (chunk_size < 1) return null;
        /**Construct sample data*/
        Double[][] samples = new Double[chunk_size][this.mDimension];
        for (int i = 0; i < chunk_size; i++) {
            samples[i] = this.mData.get(i);
        }
        /**Construct timestamps*/
        float[] timestamps = new float[chunk_size];
        long now = System.currentTimeMillis();
        for (int i = chunk_size; i > 0; i--) {
            timestamps[chunk_size - i] = (now -TIME0 - i/sample_rate);
        }
        /**construct meta data with samples and timespan*/
        Map<String, Object> eeg_chunk = new HashMap<String, Object>();
        eeg_chunk.put("name", "lizEEG");
        if (Configuration.pack_json_msgpack)/**use json*/
        {
            eeg_chunk.put("samples", ArrayUtils.toJsonArray(samples));
            eeg_chunk.put("timestamps", ArrayUtils.toJsonArray(timestamps));
        }else {
            eeg_chunk.put("samples", samples);
            eeg_chunk.put("timestamps", timestamps);
        }
        JSONObject eegJson = new JSONObject(eeg_chunk);
        // create chunk to be a input stream
        JSONArray chunks = new JSONArray();
        chunks.add(eegJson);
        // create input streams
        Map<String, Object> msg = new HashMap<String, Object>();

        msg.put("streams", chunks);
        JSONObject msgJson = new JSONObject(msg);
//        String str = msgJson.toJSONString();
//        str = msgJson.toString();
        return msgJson;
    }
}
