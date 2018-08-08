package ExtraUtils.MQTT;

import org.json.simple.JSONObject;

/**
 * Created by linzhang on 12/10/2015.
 */
public abstract class WorkerBase {
    private Thread mThread;
    private boolean isStarted = false;
    private JSONObject mParas = new JSONObject();
    protected boolean mParaModified = true;  /**This variable is special used for real-time and non-repeatable data transfer*/
    //private boolean isReateable = false;     /**This is sepcial with mParaModified, and used by publish*/
    public WorkerBase() {

    }

    /**
     * stop current worker
     */
    public void Stop()
    {
        this.isStarted = false;
        if (mThread!=null)
        {
            mThread.interrupt();
            mThread=null;
        }
    }

    /**
     * start current work
     */
    public void Start()
    {
        if (this.isStarted) return;
        this.isStarted = true;
        if(mThread == null)
        {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isStarted) {/**if reaptable*/
                        if (mParaModified) {
                            RunWork(mParas);
                            mParaModified = false;
                        }
                        /**sleep for a while*/
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        mThread.start();
    }

    public abstract void RunWork(JSONObject mParas);

    /**
     * reset the parameters for resending to NeuroScale
     * @param para
     */
    public synchronized void SetParams(JSONObject para){
        this.mParas = para;
        this.mParaModified = true;
    }

    /**
     * keep it updated using the previous parameters, or no parameters
     */
    public synchronized void SetUpdate()
    {
        this.mParaModified = true;
    }
}

