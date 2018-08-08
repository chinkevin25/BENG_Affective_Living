package ExtraUtils.DataStructure;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by linzhang on 12/8/2015.
 */
public class NeuroInstance extends DataObject {
    private String mState;
    private String mPipelineID;//relationship with pipeline
    /**
     * Constructor
     * @param id
     */
    public NeuroInstance(String id) {
        super(id);
    }

    public NeuroInstance(String id, String state, String pipeline_id) {
        super(id);
        this.mPipelineID = pipeline_id;
        this.mState = state;
    }

    public NeuroInstance(String id, String tag, String state, String pipeline_id) {
        super(id,tag);
        this.mPipelineID = pipeline_id;
        this.mState = state;
    }

    public void setState(String state){this.mState = state;}
    public void setPipeline(NeuroPipeLine pipeline){this.mPipelineID = pipeline.getID();}

    public String getState(){return this.mState;}
    public String getPipeline(){return this.mPipelineID;}

}
