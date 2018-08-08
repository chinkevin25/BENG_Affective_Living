package ExtraUtils.GUI;

import android.content.Context;
import android.widget.ListView;
import android.widget.Spinner;

import com.liz.neuroscale.android.R;

import ExtraUtils.DataStructure.NeuroInstance;
import ExtraUtils.DataStructure.NeuroPipeLine;


/**
 * Created by linzhang on 12/8/2015.
 */
public class PublishGUIPanel {
    private GuiPipeLine mGuiPipeline;
    private GuiInstance mGuiInstance;
    private GuiList mGuiList;
    //private TextView mConsole;
    private final String TAG = "GUIPanel Info:";

    // Construction
    public PublishGUIPanel(Context context, Spinner pipelineSpinner, Spinner instancSpinner, ListView lv)
    {
        this.mGuiInstance = new GuiInstance(context, instancSpinner);       // this should be laid first
        this.mGuiPipeline = new GuiPipeLine(context, pipelineSpinner);
        this.mGuiList = new GuiList(context, lv);
    }

    // Construction
    public PublishGUIPanel(Context context, Spinner pipelineSpinner,
                           Spinner instancSpinner, ListView lv,
                           GuiPipeLine.PipelineCallback pipelineCallback)
    {
        this.mGuiInstance = new GuiInstance(context, instancSpinner);       // this should be laid first
        this.mGuiPipeline = new GuiPipeLine(context, pipelineSpinner);
        this.mGuiList = new GuiList(context, lv);
        this.mGuiPipeline.mPipelineCallback = pipelineCallback;
    }

    /**
     * set enable of current gui
     * @param isEnable
     */
    public void SetInstanceEnable(boolean isEnable){
        this.mGuiInstance.SetEnable(isEnable);
    }

    public void SetPipeEnable(boolean isEnable){
        this.mGuiPipeline.SetEnable(isEnable);
    }

    public void SetListEnable(boolean isEnable){
        this.mGuiList.SetEnable(isEnable);
    }

    public NeuroPipeLine SelectPipeline(){
        String[] str = this.mGuiPipeline.SelectedItem.split(":");
        String id = str[str.length-1];
        String name = str[str.length-2];
        return new NeuroPipeLine(id, name);
    }

    public NeuroInstance SelectInstance(){
        String[] str = this.mGuiInstance.SelectedItem.split(":");
        String id = str[str.length-1];
        return new NeuroInstance(id);
    }
    // is valid
    public boolean ValidPipeline(){return this.mGuiPipeline.isValid();}
    public boolean ValidInstance(){return this.mGuiInstance.isValid();}
    /**
     * whether the current selectec item is "Create new".
     * @return
     */
    public boolean isNewInstance()
    {
        return this.mGuiInstance.isNewInstance();
    }
    /**
     * append pipeline to current spinner
     * @param pipeline
     */
    public void AppendPipeline(NeuroPipeLine pipeline){
        this.mGuiPipeline.Append(pipeline.getName()+":"+pipeline.getID());
    }

    /**
     * append instance to current spinner
     * @param instance
     */
    public void AppendInstance(NeuroInstance instance){
        if (instance.getName() != null && !instance.getName().equals(""))
            this.mGuiInstance.Append(instance.getName() + ":" + instance.getID());
        else
            this.mGuiInstance.Append(instance.getID());
    }

    /**
     * append new consloe message to listview
     * @param string
     */
    public void AppendMessage(String string)
    {
        this.mGuiList.Append(string);
    }

    // clear items in spinner
    public void ClearInstance(){ this.mGuiInstance.Clear(); }
    public void ClearPipeline(){
        this.mGuiPipeline.Clear();
    }
    public void ClearMessage(){this.mGuiList.Clear();}

    public boolean RemoveSelectedInstance(){return this.mGuiInstance.RemoveSelected();}
//    public boolean RemoveSelectedPipeline(){return this.mGuiPipeline.RemoveSelected();}
//
//    public void SetPipelineSelection(int position) { this.mGuiPipeline.SetSelection(position); }
    public void SetInstanceSelection(int position){this.mGuiInstance.SetSelection(position); }
    public void SetLastInstance(){this.mGuiInstance.SetLast();}

    public int InstanceCount(){return this.mGuiInstance.mDisplayData.size();}
    public int PipelineCount(){return this.mGuiPipeline.mDisplayData.size();}
}
