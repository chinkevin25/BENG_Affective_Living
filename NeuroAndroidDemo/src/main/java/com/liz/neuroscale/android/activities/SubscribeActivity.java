package com.liz.neuroscale.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.liz.neuroscale.android.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import ExtraUtils.ChartPlot.APChartPlotter;
import ExtraUtils.ChartPlot.ZoomPanXYPlot;
import ExtraUtils.Configuration;
import ExtraUtils.DataStructure.NeuroInstance;
import ExtraUtils.DataStructure.NeuroPipeLine;
import ExtraUtils.DataStructure.NeuroScale.NEChannel;
import ExtraUtils.DataStructure.NeuroScale.NENode;
import ExtraUtils.DataStructure.NeuroScale.NEParser;
import ExtraUtils.DataStructure.NeuroScale.NEStream;
import ExtraUtils.GUI.GUIOutputNode;
import ExtraUtils.GUI.GuiPipeLine;
import ExtraUtils.GUI.PublishGUIPanel;
import ExtraUtils.HTTP.HttpManager;
import ExtraUtils.MQTT.MQTTSubscribe;
import ExtraUtils.URLUtils;

/**
 * NeuroScale data subscribe
 */
public class SubscribeActivity extends Activity {
    private APChartPlotter plot = null;
    private HttpManager mHttpMnager;
    private PublishGUIPanel mControl;
    private GUIOutputNode mOutputNodeList;

    private ImageView ivSubscribeSwitch;
    /**worker for subscribing*/
    private MQTTSubscribe mMQTTSubscribe;
    /**indicates the status of subscribe*/
    private boolean isSubscribing = false;
    /** thread for parse and plot*/
    private Thread mParseThread = null;
    private boolean isParse = false;
    private Thread mPlotThread = null;
    private boolean isPlot = false;
    private ArrayList<Double[]> mBuffer = new ArrayList<>();
    /**parse data from neuroscale*/
    private NEParser parser = null;

    Bulb bu = new Bulb();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subscribe);
        InitButtons();
        /**setup the APR History plot:*/
        plot = new APChartPlotter((ZoomPanXYPlot) findViewById(R.id.plotNeuroMuse), 200);
        /**HTTP manager and control panel*/
        this.mHttpMnager = new HttpManager();
        this.mControl = new PublishGUIPanel(this,
                (Spinner) findViewById(R.id.spPipeline),
                (Spinner) findViewById(R.id.spInstance),
                (ListView) findViewById(R.id.listView_Message),
                new GuiPipeLine.PipelineCallback() {
                    @Override
                    public void onChanged(int position) {
                        /**when the selected item is changed*/
                        if (mControl.ValidPipeline()) {
                            mControl.ClearInstance();   //clear instance list
                            String curPipeline = mControl.SelectPipeline().getID();// get current selected pipline
                            /**get all the instance*/
                            ArrayList<NeuroInstance> instances = mHttpMnager.GetInstances();
                            for (NeuroInstance instance : instances) {
                                String str = instance.getPipeline();    // get the pipeline of current instance
                                if (str.equals(curPipeline))            //
                                {
                                    mControl.AppendInstance(instance);
                                }
                            }
                            /**select the first instance*/
                            if (mControl.InstanceCount() > 0)
                                mControl.SetInstanceSelection(0);
                        }
                        else { /**if there is no pipelines at all, then refresh it */
                            RefreshPipeline();
                        }
                    }
                });

        mOutputNodeList = new GUIOutputNode(this, (Spinner)findViewById(R.id.spOutputNode));
        mOutputNodeList.mOutputChangedCallback = new GUIOutputNode.OuputChangedCallback() {
            @Override
            public void onOuputChanged(int position) {
                if (isSubscribing) {
                    OnSwitchChannel();
                }
            }
        };
        /**refresh pipeline*/
        if (!isSubscribing)
            RefreshPipeline();
    }

    //private boolean isSubscribing = false;

    /**
     * Init all the buttons in current activity
     */
    private void InitButtons() {
        ivSubscribeSwitch = (ImageView)findViewById(R.id.imageviewSubscribe);
        ivSubscribeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubscribing){
                    OnDisconnect();
                } else {
                    OnConnect();
                }
            }
        });
    }

    /**
     * switch the channel, and plot
     */
    private void OnSwitchChannel()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (parser == null) return;
                if (!mOutputNodeList.ValidOutputNode()) {
                    AppendMessage("Select a valid output node");
                    return;
                }

                /** Unsubscribe from current MQTT topic **/
                mMQTTSubscribe.unsubscribe(mMQTTSubscribe.getEndpointTopic()
                        + "/" + Configuration.output_nodes.get(Configuration.curNodeIdx).mName);

                /**set the current node index*/
                Configuration.curNodeIdx = mOutputNodeList.SelectedOutputIndex();
                NENode curNode = Configuration.output_nodes.get(Configuration.curNodeIdx);
                /**begin switching*/
                synchronized (this) {
                    /**stop parsing*/
                    isParse = false;
                    mParseThread.interrupt();
                    mParseThread = null;
                    /**stop plotting*/
                    isPlot = false;
                    mPlotThread.interrupt();
                    mPlotThread = null;
                    mBuffer.clear();
                    /**reconstruct plot widget*/
                    plot.removeAll();
                    for (NEStream stream : curNode.mStreams) {
                        for (NEChannel channel : stream.mChannels) {
                            //plot.CreateDataSet(channel.mLabelName);
                            plot.addSeries(channel.mLabelName);
                        }
                    }
                    /** subscribe to new MQTT topic **/
                    mMQTTSubscribe.SetOutputNode(curNode.mName);
                    mMQTTSubscribe.ClearData();
                    mMQTTSubscribe.subscribe(mMQTTSubscribe.getEndpointTopic() + "/" +  curNode.mName,mMQTTSubscribe.getQoS());

                    //mMQTTSubscribe.Stop();
                   // mMQTTSubscribe = null;
                   // mMQTTSubscribe = new MQTTSubscribe(Configuration.read_endpoint, "LinZhangSubscribe", 0);

                    mMQTTSubscribe.SetUpdate();
                   // mMQTTSubscribe.Start();
                    /**rebuild parsing thread*/
                    mParseThread = new Thread(ParseRunable);
                    mParseThread.start();
                    /**rebuild ploting thread*/
                    mPlotThread = new Thread(PlotRunnable);
                    mPlotThread.start();
                }
            }
        }).start();
    }
    /**
     * stop all the thread related to pasre data
     */
    private void OnDisconnect()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bu.disconnect();
                isSubscribing = false;
                isParse = false;
                isPlot = false;

                if(mPlotThread != null){
                    mParseThread.interrupt();
                    mPlotThread = null;
                }

                if (mMQTTSubscribe != null) {
                    mMQTTSubscribe.Stop();
                    mMQTTSubscribe = null;
                }

                if (mParseThread != null) {
                    mParseThread.interrupt();
                    mParseThread = null;
                }
                ChangeStatus(false);
                AppendMessage("Disconnect");
            }
        }).start();
    }

    /**
     * find pipelines
     */
    private void RefreshPipeline()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppendMessage("Get pipeline...");
                mControl.ClearPipeline();
                ArrayList<NeuroPipeLine> pipeLines = mHttpMnager.GetPipelines();
                if (pipeLines == null) return;
                if (pipeLines.size()>0){
                    //add pipelines
                    for (NeuroPipeLine pipeLine : pipeLines) {
                        mControl.AppendPipeline(pipeLine);
                    }
                    AppendMessage("Pipeline updated");
                }else {
                    AppendMessage("No valid pipeline");
                }
            }
        }).start();
    }

    private NeuroInstance mInstance = null;
    /**
     * OnConnect server for retrieving data
     */
    private void OnConnect()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOutputNodeList.ClearOutputNode();
                /**verify the validation of instance*/
                if (!mControl.ValidPipeline() || !mControl.ValidInstance()) {
                    AppendMessage("please chose a valid instance and pipeline");
                    return;
                }
                /************************************Get current selected pipeline**************************/
                String pipeline = mControl.SelectPipeline().getID();
                String instance = mControl.SelectInstance().getID();
                try {
                    AppendMessage("waiting for instance to come up...");

                    //instance = mControl.SelectInstance().getID();
                    JSONObject json = null;
                    String last_state = "";
                    while (!last_state.equals("running")) {
                        String jsonString = mHttpMnager.mGerneralHttp.AsynGet(
                                Configuration.api_url + "/v1/instances/" + instance, Configuration.access_token);
                        json = HttpManager.Str2JSON(jsonString);
                        String state = (String) json.get("state");
                        //String jsonSTr = json.toJSONString();
                        if (!state.equals(last_state)) {
                            AppendMessage(state + "...");
                            last_state = state;
                        }
                        Thread.sleep(100);
                    }
                    Configuration.read_endpoint = URLUtils.GetEndpoints(json, "read");
                    /*******************************************************************/
                    /**
                     * added Feb 12, 2016, zhanglin
                     * function: retrieve all the output node information and restore it
                     * to Configuration
                     * */
                    /**add current instance to list*/
                    mInstance = new NeuroInstance(instance, "running", pipeline);

                    parser = new NEParser(json);
                    Configuration.output_nodes = parser.mMetaData.mOutNode;
                    /*******************************************************************/
                    /**
                     * added Feb 18, 2016, zhanglin
                     * function: display all the output node in the spinner box
                     * */
                    AppendMessage("finding output node...");
                    for (NENode node : Configuration.output_nodes)
                        mOutputNodeList.AppendOutputNode(node.mName);
                    /*******************************************************************/
                    AppendMessage("reconstructing chart...");
                    plot.removeAll();
                    for (NEStream stream : Configuration.output_nodes.get(Configuration.curNodeIdx).mStreams)
                    {
                        for (NEChannel channel : stream.mChannels)
                            plot.addSeries(channel.mLabelName);
                    }
                    /*******************************************************************/
                    AppendMessage("subscribing...");
                    if (mMQTTSubscribe == null)
                        mMQTTSubscribe = new MQTTSubscribe(Configuration.read_endpoint,
                                "LinZhangSubscribe", 0);
                    mMQTTSubscribe.SetUpdate();
                    mMQTTSubscribe.Start();
                    /**Run parsing */
                    mParseThread = new Thread(ParseRunable);
                    mParseThread.start();
                    /**Run ploting*/
                    mPlotThread = new Thread(PlotRunnable);
                    mPlotThread.start();
                    isSubscribing = true;
                    ChangeStatus(true);
                } catch (Exception e) {
                    AppendMessage(e.toString());
                }
            }
        }).start();
    }

    /**
     * set the
     * @param isConnected
     */
    private void ChangeStatus(boolean isConnected)
    {
        isSubscribing = isConnected;
        final int src;
        if(isConnected){
            src = R.drawable.button_blue_pause;
        } else {
            src = R.drawable.button_blue_play;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivSubscribeSwitch.setImageResource(src);
            }
        });
//        mControl.SetPipeEnable(!isConnected);
//        mControl.SetInstanceEnable(!isConnected);
    }
    /**
     * print message
     * @param str
     */
    public void AppendMessage(String str){
        mControl.AppendMessage(str);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(plot != null)
        plot.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(plot != null)
        plot.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(plot != null)
        plot.onDestroy();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * parse data and plot
     */
    private Runnable ParseRunable = new Runnable()
    {
        @Override
        public void run() {
            isParse = true;
            while (isParse) {
                if (mMQTTSubscribe != null){
                    ArrayList<JSONObject> data = null;
                    /**get data*/
                    if(mMQTTSubscribe.mData == null) return;
                    synchronized (mMQTTSubscribe.mData) {
                        //data = new ArrayList<>(mMQTTSubscribe.GetData());
                        data = mMQTTSubscribe.GetData();
                    }

                    /**begin parsing////////////////////////////////////////////////////*/
                    if (data != null && data.size() > 0) {
                        //Pause(100);
                        /**parse every JSONObject in the buffer*/
                        for (JSONObject temp : data){
                            JSONArray streams = (JSONArray) temp.get("streams");
                            for (Object stream : streams) {
                                /**get the stream*/
                                //JSONObject stream1 = (JSONObject) stream;
                                //String name = (String) stream1.get("name");
                                /**try parse every double */
                                if (plot != null) {
                                    /**sample channels*/
                                    JSONArray samples = (JSONArray) ((JSONObject) stream).get("samples");
                                    /**sample timestamps*/
                                    //JSONArray stamps = (JSONArray) ((JSONObject) stream).get("timestamps");
                                    /**process all sample*/
                                    for (int i = 0; i < samples.size(); i++) {
                                        /**get time*/
                                        //timeList.add((Double) (stamps.get(i)));
                                        /**for every channel of sample*/
                                        //JSONArray b = ((JSONArray) samples.get(i));
                                        /**extract every sample and convert to Double data*/
                                        Double[] eegData = new Double[((JSONArray) samples.get(i)).size()];
                                        for (int j = 0; j < ((JSONArray) samples.get(i)).size(); j++) {
                                            eegData[j] = (Double) ((JSONArray) samples.get(i)).get(j);
                                        }
                                        /**add data to buffer*/
                                        synchronized (mBuffer) {
                                            mBuffer.add(eegData);
                                        }
                                        bu.addData(eegData[0]);
                                    }
                                } else {
                                    mMQTTSubscribe.ClearData();
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private int mPlotBufferSize = 1;
    /**
     * Runnable for ploting library
     */
    Runnable PlotRunnable = new Runnable() {
        @Override
        public void run() {
            isPlot = true;
            while (isPlot){
                if (mBuffer.size()>=mPlotBufferSize) {
                    ArrayList<Double[]> datas = null;
                    synchronized (mBuffer) {
                        datas = new ArrayList<>(mBuffer);
                        mBuffer.clear();        /**clear buffer to release memeory*/
                    }
                    /**add all the datas*/
//                    for (Double[] data : datas){
//                        plot.addData(data);
//                    }
                    for (int i=0; i<datas.size(); i+=5){
                        plot.addData(datas.get(i));
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        //outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
