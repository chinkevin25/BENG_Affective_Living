package com.liz.neuroscale.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Eeg;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;
import com.interaxon.libmuse.MuseVersion;
import com.liz.neuroscale.android.R;

import org.json.simple.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ExtraUtils.ChartPlot.APChartPlotter;
import ExtraUtils.ChartPlot.ZoomPanXYPlot;
import ExtraUtils.Configuration;
import ExtraUtils.DataStructure.NeuroInstance;
import ExtraUtils.DataStructure.NeuroPipeLine;
import ExtraUtils.DataStructure.NeuroScale.InputData.MuseEEGData;
import ExtraUtils.DataStructure.NeuroScale.NENode;
import ExtraUtils.DataStructure.NeuroScale.NEParser;
import ExtraUtils.DataStructure.TestData;
import ExtraUtils.GUI.GUIMuseDevice;
import ExtraUtils.GUI.GuiPipeLine;
import ExtraUtils.GUI.PublishGUIPanel;
import ExtraUtils.HTTP.HttpManager;
import ExtraUtils.MQTT.MQTTPublish;
import ExtraUtils.URLUtils;

/**
 * activity for neuroscal publication
 */
public class PublishActivity extends Activity {
    /**Publish variables*/
    APChartPlotter plot = null;
    public HttpManager mHttpMnager;
    public PublishGUIPanel mControl;
    public GUIMuseDevice mMuseDevice;
//    private ImageButton ibtPublishSwitch;
    private ImageView ivPublishSwitch;
    /***/
    private MuseFileWriter fileWriter = null;
    /**private String mCosoleMsg*/
    private Thread mProcessThread = null;
    private MQTTPublish mPublishWoker;
    /**Muse data collection and plot*/
    private Muse muse = null;
    private ConnectionListener connectionListener = null;
    private DataListener dataListener = null;
    private boolean dataTransmission = false;
    private MuseEEGData mEEGData = new MuseEEGData(4);

    public boolean isPublishing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_publish);
        InitButtons();
        plot = new APChartPlotter((ZoomPanXYPlot)findViewById(R.id.plotNeuroMuse), 200);
        this.mHttpMnager = new HttpManager();
        this.mControl = new PublishGUIPanel(this,
                (Spinner) findViewById(R.id.spPipeline),
                (Spinner) findViewById(R.id.spInstance),
                (ListView) findViewById(R.id.listView_Message),
                new GuiPipeLine.PipelineCallback() {
                    @Override
                    public void onChanged(int position) {
                        /**when the selected item is changed*/
                        if (mControl.ValidPipeline())
                        {
                            mControl.ClearInstance();   //clear instance list
                            mControl.AppendInstance(new NeuroInstance(getString(R.string.TAG_CREATE_NEW)));
                            String curPipeline = mControl.SelectPipeline().getID();// get current selected pipline
                            /**get all the instance*/
                            ArrayList<NeuroInstance> instances = HttpManager.GetInstances();
                            for (NeuroInstance instance:instances)
                            {
                                String str = instance.getPipeline();    // get the pipeline of current instance
                                if (str.equals(curPipeline))            //
                                {
                                    mControl.AppendInstance(instance);
                                }
                            }
                            /**select the first instance*/
                            if(mControl.InstanceCount()>1)
                                mControl.SetInstanceSelection(1);
                        }
                        else { /**if there is no pipelines at all, then refresh it */
                            Log.i("CustomeZL","No pipeline, updating...");
                            if(!isPublishing){
                                RefreshMuse();
                                RefreshPipeline();
                            }
                        }
                    }
                });
        InitMuse();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        //outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * initialize all the plot data
     */
    private void InitPlot()
    {
        if (plot != null)
            plot.removeAll();
        //EEG data
        plot.addSeries("TP9");
        plot.addSeries("FP1");
        plot.addSeries("FP2");
        plot.addSeries("TP10");
    }

    /**
     * InitButtons();
     */
    private void InitButtons()
    {
        ivPublishSwitch = (ImageView)findViewById(R.id.imageviewPublish);
        ivPublishSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPublishing) {
                    OnDisconnect();
                } else {
                    OnConnect();
                }
            }
        });
    }

    /**
     *
     * @param isConnected
     */
    private void ChangeStatus(boolean isConnected)
    {
        /**change indication*/
        isPublishing = isConnected;
        if (isConnected) {
            /**change the icon*/
            final int src = R.drawable.button_blue_pause;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivPublishSwitch.setImageResource(src);
                    //mControl.SetEnable();
                }
            });
        } else {
            /**change the icon*/
            final int src = R.drawable.button_blue_play;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivPublishSwitch.setImageResource(src);
                }
            });
        }
//        mControl.SetPipeEnable(!isConnected);
//        mControl.SetListEnable(!isConnected);
//        mControl.SetInstanceEnable(!isConnected);
    }
    /**
     * init muse related ui and device
     */
    private void InitMuse(){
        /**muse spinner initialization*/
        mMuseDevice = new GUIMuseDevice(this, (Spinner)findViewById(R.id.spMuse));
        mMuseDevice.mMuseCallback = new GUIMuseDevice.MuseCallback() {
            @Override
            public void onChanged(int position) {
                /*****************************************************/
                /**do something related to change current connected muse device*/
            }
        };

        WeakReference<Activity> weakActivity = new WeakReference<Activity>(this);
        connectionListener = new ConnectionListener(weakActivity);
        dataListener = new DataListener();

        File dir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        fileWriter = MuseFileFactory.getMuseFileWriter(new File(dir, "new_muse_file.muse"));
        //Log.i("MuseLib Headband", "libmuse version=" + LibMuseVersion.SDK_VERSION);
        dataListener.setFileWriter(fileWriter);
        fileWriter.addAnnotationString(1, "MainActivity onCreate");
        /**refresh all the pipeline and instance list*/
        if (!isPublishing) {
            RefreshPipeline();
            RefreshMuse();
        }
    }
    private boolean isStartProcess = false;
    private int sample_rate = 220;
    /**
     * connect to neuroscale and muse
     */
    private void OnConnect()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**get the valid neuroscale pipeline*/
                AppendMessage("Connecting NeuroScale...");
                if (!mControl.ValidPipeline()) {
                    AppendMessage("Should first chose a valid pipeline");
                    return;
                }

                /*********************************************************/
                mControl.AppendMessage("Connecting muse...");
                /** begin connect with muse via bluetooth*/
                if (!mMuseDevice.ValidMuse()) {
                    Log.w("MuseLib Headband", "There is nothing to connect to");
                    AppendMessage("There is nothing to connect to");
                } else {
                    muse = mMuseDevice.SelectedMuse();
                    ConnectionState state = muse.getConnectionState();
                    if (state == ConnectionState.CONNECTED ||
                            state == ConnectionState.CONNECTING) {
                        AppendMessage("Do not repeat connecting to the same muse");
                        Log.w("MuseLib Headband",
                                "doesn't make sense to connect second time to the same muse");
//                        Configuration.PublishState = Configuration.NEState.MUSE_CONNECTED;
                        return;
                    }
                    configureLibrary();
                    fileWriter.open();
                    fileWriter.addAnnotationString(1, "Connect clicked");
                    /**
                     * In most cases libmuse native library takes care about
                     * exceptions and recovery mechanism, but native code still
                     * may throw in some unexpected situations (like bad bluetooth
                     * connection). Print all exceptions here.
                     */
                    try {
                        muse.runAsynchronously();
                        //Configuration.PublishState = Configuration.NEState.MUSE_CONNECTED;
                    } catch (Exception e) {
//                        Configuration.PublishState = Configuration.NEState.DISCONNECTED;
                        AppendMessage("Muse error");
                        Log.e("MuseLib Headband", e.toString());
                    }
                }
                /********************************************************/
                /**waiting for muse running*/
                ConnectionState muse_state = muse.getConnectionState();
                AppendMessage("Waiting for muse connecting...");
                while (muse_state != ConnectionState.CONNECTED) {
                    muse_state = muse.getConnectionState();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                /*********************************************************/
                InitPlot();
                /*********************************************************/
                /**begin process*/
                mProcessThread = new Thread(processRunable);
                mProcessThread.start();
                /*********************************************************/
                String pipeline = mControl.SelectPipeline().getID();
                /*********************************************************/
                /**before construct meta data, fixed meta should be queried first*/
//                String urlMeta = Configuration.api_url + "/v1/pipelines/" + pipeline;
//                String strQueryMeta = mHttpMnager.mGerneralHttp.AsynGet(urlMeta, Configuration.access_token);
//                JSONObject jsonMeta = HttpManager.Str2JSON(strQueryMeta);
//                NEParser parserMeta = new NEParser(jsonMeta);
//                ArrayList<NENode> nodeMeta = parserMeta.mMetaData.mOutNode;
//                JSONObject json = TestData.ConstructCustomMeta(pipeline, sample_rate, nodeMeta);

                JSONObject json = TestData.ConstructMuseMeta(pipeline, sample_rate);
                /**JSONObject json = TestData.ContructionMeta(pipeline);*/
                String instance;
                String url = Configuration.api_url + "/v1/instances";
                boolean isNewIntance = false;
                try {
                    String response;
                    if (mControl.isNewInstance()) {/**if create a new instance is chosen, create a new instance*/
                        AppendMessage("Posting ...");
                        response = mHttpMnager.mGerneralHttp.AsynPost(url, Configuration.access_token, json);
                        isNewIntance = true;
                    } else {/**otherwise, patch current instance*/
                        AppendMessage("Patching ...");
                        instance = mControl.SelectInstance().getID();
                        response = mHttpMnager.mGerneralHttp.AsynPatch(url, instance, Configuration.access_token, json);
                        isNewIntance = false;
                    }
                    /** Get the instance ID*/
                    JSONObject jsonObject = HttpManager.Str2JSON(response);
                    instance = (String) jsonObject.get("id");

                    /**add current instance to list*/
                    mInstance = new NeuroInstance(instance, "running", pipeline);
                    if(isNewIntance) {
                        mControl.AppendInstance(mInstance);
                        mControl.SetLastInstance();
                    }

                    AppendMessage("waiting for instance to come up...");
                    /** wait for program running*/
                    String last_state = "";
                    while (!last_state.equals("running")) {
                        String jsonString = mHttpMnager.mGerneralHttp.AsynGet(
                                Configuration.api_url + "/v1/instances/" + instance, Configuration.access_token);
                        json = HttpManager.Str2JSON(jsonString);
                        String state = (String) json.get("state");
                        if (!state.equals(last_state)) {
                            AppendMessage(state + "...");
                            last_state = state;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    /** find endpoint of publish*/
                    URLUtils write_endpoint = URLUtils.GetEndpoints(json, "write");
                    AppendMessage("publishing...");
                    /******************************************/
                    if (mPublishWoker == null)
                        mPublishWoker = new MQTTPublish(write_endpoint, "LizhangPushlish", 0);//at least once
                    mPublishWoker.SetUpdate();
                    mPublishWoker.Start();
                    ChangeStatus(true);
                } catch (Exception e) {
                    AppendMessage(e.toString());
                }
            }
        }).start();
    }
    private NeuroInstance mInstance = null;
    /**
     * Disconnect the connection
     */
    private void OnDisconnect()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Configuration.PublishState = Configuration.NEState.DISCONNECTED;
                isStartProcess = false;

                if(mPublishWoker!=null) {
                    mPublishWoker.Stop();
                    mPublishWoker = null;
                }

                if (mProcessThread != null)
                {
                    mProcessThread.interrupt();
                    mProcessThread = null;
                }

                if(muse != null) {
                    muse.disconnect(true);
                    fileWriter.addAnnotationString(1, "Disconnect clicked");
                    fileWriter.flush();
                    fileWriter.close();
                    //dataTransmission = false;
                }

                mControl.AppendMessage("Deleting instance...");
                // verify the validation of instance
                if (mInstance != null && mInstance.getState().equals("running")) {
                    String url = Configuration.api_url + "/v1/instances"; //aimed at create a new workder
                    int responseCode = mHttpMnager.mGerneralHttp.AsynDelete(url, Configuration.access_token, mInstance.getID());
                    if (responseCode == 204) {
                        if (mControl.RemoveSelectedInstance())
                            mControl.AppendMessage("instance been deleted successfully, code " + responseCode);
                        else
                            mControl.AppendMessage("Instance deleted successfully, while UI update failed");
                    }
                }
                AppendMessage("Disconnected");
                ChangeStatus(false);
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
                ArrayList<NeuroPipeLine> pipeLines = HttpManager.GetPipelines();
                if (pipeLines.size()>0){
                    //add pipelines
                    for (NeuroPipeLine pipeLine : pipeLines) {
                        mControl.AppendPipeline(pipeLine);
                    }
                    AppendMessage("Pipeline updated");
                    /**add the create new item*/
                    mControl.AppendInstance(new NeuroInstance(getString(R.string.TAG_CREATE_NEW)));
                } else {
                    AppendMessage("No valid pipeline");
                }
            }
        }).start();
    }
    /**
     * refresh the spinner list of muse
     */
    private void RefreshMuse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MuseManager.refreshPairedMuses();
                mMuseDevice.AppendMuse(MuseManager.getPairedMuses());   //add all the paired muse
            }
        }).start();
        AppendMessage("Muse updated!");
    }

    private Runnable processRunable = new Runnable() {
        @Override
        public void run() {
            isStartProcess = true;
            while (isStartProcess) {
                /**publish data to neuroscale*/
                if (mPublishWoker != null /*&&  mEEGData.Size() > 0*/)
                {
//                    MuseEEGData dataList = null;
//                    synchronized (mEEGData) {
//                        dataList = new MuseEEGData(mEEGData);
//                        mEEGData.Clear();
//                    }
//                    if (dataList == null) return;
//                    /**Set parameters to be published*/
//                    //JSONObject obje = dataList.toJSON();
//                    mPublishWoker.SetParams(dataList.toJSON(sample_rate));
//                    /**plot data*/
//                    if (plot == null) return;
//                    for (int i=0; i< dataList.Size(); i+=10)
//                    {
//                        plot.addData(dataList.Get(i));
//                    }
                    /**Set parameters to be published*/
                    //JSONObject obje = dataList.toJSON();
                    mPublishWoker.SetParams(mEEGData.toJSON(sample_rate));
                    /**plot data*/
                    if (plot == null) return;
                    for (int i=0; i< mEEGData.Size(); i+=10)
                    {
                        plot.addData(mEEGData.Get(i));
                    }
                    /**clear data buffer*/
                    synchronized (mEEGData) {
                        mEEGData.Clear();
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
    /**
     * print message
     * @param str
     */
    public void AppendMessage(String str){
        Log.i("CustotmedZl",str);
        mControl.AppendMessage(str);
    }

    /**
     * configuration of muse library
     */
    private void configureLibrary() {
        dataTransmission = true;
        muse.registerConnectionListener(connectionListener);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.ACCELEROMETER);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.EEG);
//        muse.registerDataListener(dataListener,
//                MuseDataPacketType.ALPHA_RELATIVE);
//        muse.registerDataListener(dataListener,
//                MuseDataPacketType.ARTIFACTS);
//        muse.registerDataListener(dataListener,
//                MuseDataPacketType.BATTERY);
        muse.setPreset(MusePreset.PRESET_14);
        muse.enableDataTransmission(dataTransmission);
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
//        super.onBackPressed();
        moveTaskToBack(true);
    }

    class ConnectionListener extends MuseConnectionListener {

        final WeakReference<Activity> activityRef;

        ConnectionListener(final WeakReference<Activity> activityRef) {
            this.activityRef = activityRef;
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
            final ConnectionState current = p.getCurrentConnectionState();
            final String status = p.getPreviousConnectionState().toString() +
                    " -> " + current;
            final String full = "MuseLib " + p.getSource().getMacAddress() +
                    " " + status;
            AppendMessage("MuseLib Headband" + full);
            //Log.i("Muse", version);
            Activity activity = activityRef.get();
            // UI thread is used here only because we need to update
            // TextView values. You don't have to use another thread, unless
            // you want to run disconnect() or connect() from connection packet
            // handler. In this case creating another thread is required.
            if (activity != null) {
                AppendMessage(status);

                if (current == ConnectionState.CONNECTED)
                {
                    MuseVersion museVersion = muse.getMuseVersion();
                    String version = museVersion.getFirmwareType() +
                            " - " + museVersion.getFirmwareVersion() +
                            " - " + Integer.toString(
                            museVersion.getProtocolVersion());
                    AppendMessage("Muse:"+version); /**show version of muse*/
                } else {
                    AppendMessage("Muse:" + "Unknown");
                }

//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AppendMessage(status);
//
//                        if (current == ConnectionState.CONNECTED) {
//                            MuseVersion museVersion = muse.getMuseVersion();
//                            String version = museVersion.getFirmwareType() +
//                                    " - " + museVersion.getFirmwareVersion() +
//                                    " - " + Integer.toString(
//                                    museVersion.getProtocolVersion());
//                            //museVersionText.setText(version);
//                            Log.i("Muse", version);
////                            Configuration.PublishState = Configuration.NEState.MUSE_CONNECTED;
//                        } else {
////                            museVersionText.setText(R.string.undefined);
//                            Log.i("Muse", "UnKnown");
////                            Configuration.PublishState = Configuration.NEState.DISCONNECTED;
//                        }
//                    }
//                });
            }
        }
    }
    /**
     * Data listener will be registered to listen for: Accelerometer,
     * Eeg and Relative Alpha bandpower packets. In all cases we will
     * update UI with new values.
     * We also will log message if Artifact packets contains "blink" flag.
     * DataListener methods will be called from execution thread. If you are
     * implementing "serious" processing algorithms inside those listeners,
     * consider to create another thread.
     */
    class DataListener extends MuseDataListener {
        private MuseFileWriter fileWriter;
//        final WeakReference<Activity> activityRef;
//
//        DataListener(final WeakReference<Activity> activityRef) {
//            this.activityRef = activityRef;
//        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket p) {
            switch (p.getPacketType()) {
                case EEG:
                    updateEeg(p.getValues());
                    break;
                case ACCELEROMETER:
                    updateAccelerometer(p.getValues());
                    break;
                case ALPHA_RELATIVE:
                    updateAlphaRelative(p.getValues());
                    break;
                case BATTERY:
                    fileWriter.addDataPacket(1, p);
                    // It's library client responsibility to flush the buffer,
                    // otherwise you may Series memory overflow.
                    if (fileWriter.getBufferedMessagesSize() > 8096)
                        fileWriter.flush();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
            if (p.getHeadbandOn() && p.getBlink()) {
                Log.i("Artifacts", "blink");
            }
        }

        /**
         * Acceleration data
         * @param data
         */
        private void updateAccelerometer(final ArrayList<Double> data) {
//            synchronized (mACCList)
//            {
//                mACCList.add(new MuseAccData(new Double[]{
//                        data.get(Accelerometer.FORWARD_BACKWARD.ordinal()).doubleValue(),
//                        data.get(Accelerometer.UP_DOWN.ordinal()).doubleValue(),
//                        data.get(Accelerometer.LEFT_RIGHT.ordinal()).doubleValue()
//                }));
//            }
        }

        /**
         * EEG data
         * @param data
         */
        private void updateEeg(final ArrayList<Double> data) {
            /**get the EEG data*/
            Double[] d = new Double[]{
                    data.get(Eeg.TP9.ordinal()).doubleValue(),
                    data.get(Eeg.FP1.ordinal()).doubleValue(),
                    data.get(Eeg.FP2.ordinal()).doubleValue(),
                    data.get(Eeg.TP10.ordinal()).doubleValue()};
            /**insert data into buffer*/
            synchronized (mEEGData) {
                mEEGData.Append(d);
            }
            //Log.i("Muse:", mEEGList.get(0).Data()[0].toString());
        }

        /**
         * Alpha relative data
         * @param data
         */
        private void updateAlphaRelative(final ArrayList<Double> data) {
            /**don't collect this data in this program*/
        }

        public void setFileWriter(MuseFileWriter fileWriter) {
            this.fileWriter  = fileWriter;
        }
    }
}
