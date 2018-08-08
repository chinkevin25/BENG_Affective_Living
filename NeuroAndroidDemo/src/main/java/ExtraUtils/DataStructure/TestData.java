package ExtraUtils.DataStructure;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ExtraUtils.ArrayUtils;
import ExtraUtils.Configuration;
import ExtraUtils.DataStructure.NeuroScale.NEChannel;
import ExtraUtils.DataStructure.NeuroScale.NENode;
import ExtraUtils.DataStructure.NeuroScale.NEStream;


/**
 * Created by linzhang on 12/10/2015.
 */
public class TestData {

    /**
     * This funciton is just for test.
     * @return
     */
    private static String[] channel_labels = new String[] { "ch1", "ch2", "ch3",
            "ch4", "ch5", "ch6", "ch7", "ch8" };
    private static float sampling_rate = 220.0f;
    private static String modality = "EEG";
    // in java
//    public static JSONObject ContructionMeta(String pl_id) {
//        /************************* Metadata Begin *****************************************/
//        /************************* Channels array Begin **************************************/
//        JSONArray jsonChannels = new JSONArray();
//        Map<String, Object> channels = new HashMap<String, Object>();
//        for (String channel : channel_labels) {
//            channels.put("label", channel);
//            JSONObject tempJson = new JSONObject(channels);
//            jsonChannels.add(tempJson);
//        }
//        /************************* Channels array End *****************************************/
//        /************************* SingleChannel array Begin **********************************/
//        JSONArray jsonSingleChannel = new JSONArray();
//        Map<String, Object> single_channel = new HashMap<String, Object>();
//        single_channel.put("label", "mylabel");
//        JSONObject tempJson = new JSONObject(single_channel);
//        jsonSingleChannel.add(tempJson);
//        /************************* SingleChannel array End ************************************/
//        /************************* Steams array Begin *****************************************/
//        JSONArray jsonStreams = new JSONArray();
//        // Streams should be a array
//        Map<String, Object> streams = new HashMap<String, Object>();
//        // EEG_Stream
//        streams.put("name", "myeeg");
//        streams.put("type", modality);
//        streams.put("sampling_rate", sampling_rate);
//        streams.put("channels", jsonChannels);
//        tempJson = new JSONObject(streams);
//        jsonStreams.add(tempJson);
//        // marker stream
//        streams.clear();
//        streams.put("name", "mymarkers");
//        streams.put("type", "Markers");
//        streams.put("sampling_rate", 0); // maker stream should be zero,
//        // different from eeg stream
//        streams.put("channels", jsonSingleChannel);
//        tempJson = new JSONObject(streams);
//        jsonStreams.add(tempJson);
//        /************************* Steams array End *****************************************/
//        JSONArray inOutNode = new JSONArray();
//        Map<String, Object> node_decl = new HashMap<String, Object>();
//        node_decl.put("name", "default");
//        node_decl.put("streams", jsonStreams);
//        JSONObject inOut = new JSONObject(node_decl);
//        inOutNode.add(node_decl);
//        /************************* Nodes array Begin **************************************/
//        Map<String, Object> node = new HashMap<String, Object>();
//        node.put("in", inOutNode);
//        node.put("out", inOutNode);
//        Map<String, Object> metadata = new HashMap<String, Object>();
//        metadata.put("nodes", node);
//        /************************* Nodes array End *****************************************/
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("pipeline", pl_id);
//        params.put("metadata", metadata);
//        params.put("encoding", "msgpack");// or json
//        /************************* Metadata End *****************************************/
//        // create json object
//        JSONObject json = new JSONObject(params);
//        return json;
//    }
//    // in android
//    public static JSONObject ContructionMeta(String pl_id) {
//        /************************* Metadata Begin *****************************************/
//        /************************* Channels array Begin **************************************/
//        JSONArray jsonChannels = new JSONArray();
//        Map<String, Object> channels = new HashMap<String, Object>();
//        for (String channel : channel_labels) {
//            channels.put("label", channel);
//            JSONObject tempJson = new JSONObject(channels);
//            jsonChannels.add(tempJson);
//        }
//        /************************* Channels array End *****************************************/
//        /************************* SingleChannel array Begin **********************************/
//        JSONArray jsonSingleChannel = new JSONArray();
//        Map<String, Object> single_channel = new HashMap<String, Object>();
//        single_channel.put("label", "mylabel");
//        JSONObject tempJson = new JSONObject(single_channel);
//        jsonSingleChannel.add(tempJson);
//        /************************* SingleChannel array End ************************************/
//        /************************* Steams array Begin *****************************************/
//        JSONArray jsonStreams = new JSONArray();
//        // Streams should be a array
//        Map<String, Object> streams = new HashMap<String, Object>();
//        // EEG_Stream
//        streams.put("name", "myeeg");
//        streams.put("type", modality);
//        streams.put("sampling_rate", sampling_rate);
//        streams.put("channels", jsonChannels);
//        tempJson = new JSONObject(streams);
//        jsonStreams.add(tempJson);
//        // marker stream
//        streams.clear();
//        streams.put("name", "mymarkers");
//        streams.put("type", "Markers");
//        streams.put("sampling_rate", 0); // maker stream should be zero,
//        // different from eeg stream
//        streams.put("channels", jsonSingleChannel);
//        tempJson = new JSONObject(streams);
//        jsonStreams.add(tempJson);
//        /************************* Steams array End *****************************************/
//        JSONArray inOutNode = new JSONArray();
//        Map<String, Object> node_decl = new HashMap<String, Object>();
//        node_decl.put("name", "default");
//        node_decl.put("streams", jsonStreams);
//        JSONObject inOut = new JSONObject(node_decl);
//        inOutNode.add(node_decl);
//        /************************* Nodes array Begin **************************************/
//        Map<String, Object> node = new HashMap<String, Object>();
//        node.put("in", inOutNode);
//        node.put("out", inOutNode);
//        Map<String, Object> metadata = new HashMap<String, Object>();
//        metadata.put("nodes", node);
//        /************************* Nodes array End *****************************************/
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("pipeline", pl_id);
//        params.put("metadata", metadata);
//        params.put("encoding", "msgpack");// or json
//        /************************* Metadata End *****************************************/
//        // create json object
//        JSONObject json = new JSONObject(params);
//        return json;
//    }

//    //in android
//    public static JSONObject ConstructData()
//    {
//        int chunk_samples = 10;
//        // Construct random data
//        Random random = new Random();
//        float[][] samples = new float[chunk_samples][8];
//        for (int i = 0; i < chunk_samples; i++) {
//            float[] sample = new float[]{random.nextFloat(),// ch1
//                    random.nextFloat(),// ch2
//                    random.nextFloat(),// ch3
//                    random.nextFloat(),// ch4
//                    random.nextFloat(),// ch5
//                    random.nextFloat(),// ch6
//                    random.nextFloat(),// ch7
//                    random.nextFloat() // ch8
//            };
//            samples[i] = sample;
//        }
//        // Construct timestamps
//        float[] timestamps = new float[10];
//        long now = System.currentTimeMillis();
//        for (int i = chunk_samples; i > 0; i--) {
//            timestamps[chunk_samples - i] = now - i / 100.0f;
//        }
//        // construct eeg chunk bluetooth_message
//        Map<String, Object> eeg_chunk = new HashMap<String, Object>();
//        eeg_chunk.put("name", "myeeg");
//        eeg_chunk.put("samples", ArrayUtils.toString(samples, ","));
//        eeg_chunk.put("timestamps",
//                ArrayUtils.toString(timestamps));
//        JSONObject eegJson = new JSONObject(eeg_chunk);
//
//        String[][] markers = new String[3][1];
//        for (int i = 0; i < 3; i++) {
//            String[] marker = new String[]{"marker" + (i + 1)};
//            markers[i] = marker;
//        }
//        float[] marker_stamps = new float[]{
//                (float) (now - 0.025), (float) (now - 0.01),
//                (float) (now - 0.05)};
//        // construct marker chunk bluetooth_message
//        Map<String, Object> marker_chunk = new HashMap<String, Object>();
//        marker_chunk.put("name", "mymarkers");
//        marker_chunk.put("samples", ArrayUtils.toString(markers, ","));
//        marker_chunk.put("timestamps", ArrayUtils.toString(marker_stamps));
//        JSONObject markerJson = new JSONObject(marker_chunk);
//
//        JSONArray chunks = new JSONArray();
//        chunks.add(eegJson);
//        chunks.add(markerJson);
//
//        Map<String, Object> msg = new HashMap<String, Object>();
//        msg.put("streams", chunks);
//        JSONObject msgJson = new JSONObject(msg);
//        return msgJson;
//    }

    /**
     * construct muse meta-data for concentration pipeline,
     * 4 channels in
     * 1 channels out for default node
     * 4 channels out for cleandata node
     * 4 channels out for rawdata node
     * @param pl_id
     * @param sampling_rate
     * @return
     */
    public static JSONObject ConstructMuseMeta(String pl_id, int sampling_rate)
    {
        /*********************************************************************************/
        Map<String, Object> node = new HashMap<String, Object>();

        /**add input JSONObject*/
        JSONArray inNodeArray = new JSONArray();
        inNodeArray.add(createInputNode());
        node.put("in", inNodeArray);
/**add input JSONObject*/
/**
        //outNodeArray.add(createInputNode());
        node.put("in", createInputNode());*/

        /**add output JSONObject*/
        JSONArray outNodeArray = new JSONArray();
        outNodeArray.add(createDefaultOutNode());
        outNodeArray.add(createRawOutput());
        outNodeArray.add(createCleanOutput());
        node.put("out", outNodeArray);
        /**construct meta-data*/
        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("nodes", node);
        JSONObject metaJson = new JSONObject(metadata); // added by zhanglin transfer map to a jsonobject
        /************************* Nodes array End *****************************************/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pipeline", pl_id);
        params.put("metadata", metaJson);
        if (Configuration.pack_json_msgpack){/**using JSON*/
            params.put("encoding", "json");// or json
        } else {/**using MESSAGE_PACK*/
            params.put("encoding", "msgpack");// or messagepack
        }

        /************************* Metadata End *****************************************/
        // create json object
        JSONObject json = new JSONObject(params);
        return json;
    }

    /**
     * construct muse meta-data for concentration pipeline,
     * 4 channels in
     * 1 channels out for default node
     * 4 channels out for cleandata node
     * 4 channels out for rawdata node
     * @param pl_id
     * @param sampling_rate
     * @return
     */
    public static JSONObject ConstructCustomMeta(String pl_id, int sampling_rate, ArrayList<NENode> ouputNodes)
    {
        /*********************************************************************************/
        Map<String, Object> node = new HashMap<String, Object>();
        /**add input JSONObject*/
        JSONArray inNodeArray = new JSONArray();
        inNodeArray.add(createInputNode());
        node.put("in", inNodeArray);
        /**add output JSONObject*/
        JSONArray outNodeArray = new JSONArray();
        for (NENode output: ouputNodes){
            NEStream stream = output.mStreams.get(0);
            outNodeArray.add(createCustomOutNode(output.mName, stream.mName, stream.mSampleRate, stream.mChannels));
        }
        node.put("out", outNodeArray);
        /**construct meta-data*/
        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("nodes", node);
        JSONObject metaJson = new JSONObject(metadata); // added by zhanglin transfer map to a jsonobject
        /************************* Nodes array End *****************************************/
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pipeline", pl_id);
        params.put("metadata", metaJson);
        if (Configuration.pack_json_msgpack){/**using JSON*/
            params.put("encoding", "json");// or json
        } else {/**using MESSAGE_PACK*/
            params.put("encoding", "msgpack");// or messagepack
        }
        /************************* Metadata End *****************************************/
        // create json object
        JSONObject json = new JSONObject(params);
        return json;
    }

    /**
     * create the customed output specific for current pipeline
     * @param chs the info of stream
     * @return
     */
    private static JSONObject createCustomOutNode(String nodeName, String streamName, Double sample_rate, ArrayList<NEChannel> chs)
    {
        /************************* outputChannel array  **********************************/
        JSONArray outChannels = new JSONArray();
        Map<String, Object> channels = new HashMap<String, Object>();
        for (NEChannel channel : chs){
            channels.put("label", channel.mLabelName);
            JSONObject tempJson = new JSONObject(channels);
            outChannels.add(tempJson);
        }
        /************************* outputStream array ************************************/
        // output stream
        JSONArray outputStreams = new JSONArray();
        Map<String, Object> out_streams = new HashMap<String, Object>();
        // Concentration output stream
        out_streams.put("channels", outChannels);
        out_streams.put("type", "EEG");
        out_streams.put("name", streamName);
        out_streams.put("sampling_rate", sample_rate);

        JSONObject tempJson = new JSONObject(out_streams);
        outputStreams.add(tempJson);
        /************************* outputNode array  ************************************/
        Map<String, Object> out_node_decl = new HashMap<String, Object>();
        out_node_decl.put("name", nodeName);
        out_node_decl.put("streams", outputStreams);
        JSONObject inOut = new JSONObject(out_node_decl);

        return inOut;
    }

    /**
     * input node metadata for concentration pipeline
     * @return
     */
    private static JSONObject createInputNode()
    {
        /************************* inputNode Begin *****************************************/
        /************************* Channels array Begin **************************************/
        String[] inputChannel_label = new String[]{"TP9", "FP1", "FP2", "TP10"};
        JSONArray inputChannels = new JSONArray();
        Map<String, Object> channels = new HashMap<String, Object>();
        for (String channel : inputChannel_label) {
            channels.put("label", channel);
            JSONObject tempJson = new JSONObject(channels);
            inputChannels.add(tempJson);
        }
        /************************* Channels array End *****************************************/
        /************************* Steams array Begin *****************************************/
        JSONArray inputStreams = new JSONArray();
        // Streams should be a array
        Map<String, Object> streams = new HashMap<String, Object>();
        // EEG_Stream
        streams.put("channels", inputChannels);
        streams.put("name", "lizEEG");// changed myeeg to default
        streams.put("sampling_rate", sampling_rate);
        streams.put("type", "EEG");

        JSONObject tempJson = new JSONObject(streams);
        inputStreams.add(tempJson);
        /************************* Steams array End *****************************************/
        Map<String, Object> node_decl = new HashMap<String, Object>();
        node_decl.put("name", "default");
        node_decl.put("streams", inputStreams);
        JSONObject inOut = new JSONObject(node_decl);
        /************************* Nodes array Begin **************************************/
        return inOut;
    }

    /**
     * create the default output specific for current pipeline
     * @return
     */
    private static JSONObject createDefaultOutNode()
    {
        /***************************outputNode begin******************************************************/
        /************************* outputChannel array  **********************************/
        JSONArray outputChannel = new JSONArray();
        Map<String, Object> single_channel = new HashMap<String, Object>();
        single_channel.put("label", "Concentration");
        JSONObject tempJson = new JSONObject(single_channel);
        outputChannel.add(tempJson);
        /************************* outputStream array ************************************/
        // output stream
        JSONArray outputStreams = new JSONArray();
        Map<String, Object> out_streams = new HashMap<String, Object>();
        // Concentration output stream
        out_streams.put("channels", outputChannel);
        out_streams.put("type", "EEG");
        out_streams.put("name", "myeeg");
        out_streams.put("sampling_rate", sampling_rate);

        tempJson = new JSONObject(out_streams);
        outputStreams.add(tempJson);
        /************************* outputNode array  ************************************/

        Map<String, Object> out_node_decl = new HashMap<String, Object>();
        out_node_decl.put("name", "default");
        out_node_decl.put("streams", outputStreams);
        JSONObject inOut = new JSONObject(out_node_decl);

        return inOut;
    }

    /**
     * create raw data output specific for current pipeline
     * @return
     */
    private static JSONObject createRawOutput()
    {
        /************************* outputChannel array  **********************************/
        String[] raw_channel_label = new String[]{"1", "2", "3", "4"};
        JSONArray outChannels = new JSONArray();
        Map<String, Object> channels = new HashMap<String, Object>();
        for (String channel : raw_channel_label) {
            channels.put("label", channel);
            JSONObject tempJson = new JSONObject(channels);
            outChannels.add(tempJson);
        }
        /************************* outputStream array ************************************/
        // output stream
        JSONArray outputStreams = new JSONArray();
        Map<String, Object> out_streams = new HashMap<String, Object>();
        // Concentration output stream
        out_streams.put("channels", outChannels);
        out_streams.put("type", "EEG");
        out_streams.put("name", "myeeg");
        out_streams.put("sampling_rate", sampling_rate);

        JSONObject tempJson = new JSONObject(out_streams);
        outputStreams.add(tempJson);
        /************************* outputNode array  ************************************/
        Map<String, Object> out_node_decl = new HashMap<String, Object>();
        out_node_decl.put("name", "rawdata");
        out_node_decl.put("streams", outputStreams);
        JSONObject inOut = new JSONObject(out_node_decl);

        return inOut;
    }

    /**
     * create clean data output specific for current pipeline
     * @return
     */
    private static JSONObject createCleanOutput()
    {
        /************************* outputChannel array  **********************************/
        //JSONArray outputChannel = new JSONArray();

        String[] raw_channel_label = new String[]{"1", "2", "3", "4"};
        JSONArray outChannels = new JSONArray();
        Map<String, Object> channels = new HashMap<String, Object>();
        for (String channel : raw_channel_label) {
            channels.put("label", channel);
            JSONObject tempJson = new JSONObject(channels);
            outChannels.add(tempJson);
        }
        /************************* outputStream array ************************************/
        // output stream
        JSONArray outputStreams = new JSONArray();
        Map<String, Object> out_streams = new HashMap<String, Object>();
        // Concentration output stream
        out_streams.put("channels", outChannels);
        out_streams.put("type", "EEG");
        out_streams.put("name", "myeeg");
        out_streams.put("sampling_rate", sampling_rate);

        JSONObject tempJson = new JSONObject(out_streams);
        outputStreams.add(tempJson);
        /************************* outputNode array  ************************************/
        Map<String, Object> out_node_decl = new HashMap<String, Object>();
        out_node_decl.put("name", "cleandata");
        out_node_decl.put("streams", outputStreams);

        return new JSONObject(out_node_decl);
    }

//    /**
//     * input data for concentration pipeline
//     * @param eeg  4 channels input, eeg data
//     * @return
//     */
//    public static JSONObject InputMuseData(List<MuseEegData> eeg) {
//        if(eeg == null) return null;
//        int chunk_size = eeg.size();        // get sample size
//        if (chunk_size < 1) return null;
//        int chunk_dim = eeg.get(0).Dim();   // get sample dimension, for muse, we use 4 channels
//
//        // Construct random data
//        Double[][] samples = new Double[chunk_size][chunk_dim];
//        for (int i = 0; i < chunk_size; i++) {
//            samples[i] = eeg.get(i).Data();
//        }
//
//        // Construct timestamps
//        float[] timestamps = new float[chunk_size];
//        long now = System.currentTimeMillis();
//        for (int i = chunk_size; i > 0; i--) {
//            timestamps[chunk_size - i] = now - i / 100.0f;
//        }
//
//        Map<String, Object> eeg_chunk = new HashMap<String, Object>();
//        eeg_chunk.put("name", "myeeg");
//        eeg_chunk.put("samples", samples);
//        eeg_chunk.put("timestamps", timestamps);
//        JSONObject eegJson = new JSONObject(eeg_chunk);
//        // create chunk to be a input stream
//        JSONArray chunks = new JSONArray();
//        chunks.add(eegJson);
//        // create input streams
//        Map<String, Object> msg = new HashMap<String, Object>();
//        JSONArray A = new JSONArray();A.add(chunks);
//        msg.put("streams", chunks);
//        JSONObject msgJson = new JSONObject(msg);
//        return msgJson;
//    }
}
