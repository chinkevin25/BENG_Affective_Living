package ExtraUtils.GUI;

import android.content.Context;
import android.widget.Spinner;

import com.liz.neuroscale.android.R;


/**
 * Created by linzhang on 12/8/2015.
 */
public class GuiPipeLine extends GuiSelectableBase  {
    /**
     * define a interface
     */
    public interface PipelineCallback
    {
        // This is just a regular method so it can return something or
        // take arguments if you like.
        public void onChanged(int position);
    }
    public PipelineCallback mPipelineCallback;

    public GuiPipeLine(Context context, Spinner widgt) {
        super(context, widgt, R.layout.plugin_spinner_text);
        super.mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void OnItemSelectedChanged(int position) {
//        GlobalRefreshPipeline();
        if (this.mPipelineCallback != null)
        {
            mPipelineCallback.onChanged(position);
        }
    }

//    private void GlobalRefreshPipeline()
//    {
//        HttpManager mHttpMnager = null;
//        PublishGUIPanel mControl = null;
//
//        // determine which controlpannel and http manager should be used
//        switch ((int)Configuration.PageIdentifer)
//        {
//            case 1: //(Configuration.ID_Configure):
//                break;
//            case 2:
//                mHttpMnager = PublishFragment.mHttpMnager;
//                mControl = PublishFragment.mControl;
//                break;
//            case 3:
//                mHttpMnager = SubscribeFragment.mHttpMnager;
//                mControl = SubscribeFragment.mControl;
//                break;
//            case 4:
//                break;
//            default:
//                break;
//        }
//        // filter instance from pipeline
//        if(mHttpMnager != null && mControl != null)
//        {
//            mControl.ClearInstance();
//            ArrayList<NeuroInstance> instances = mHttpMnager.GetInstances();
//            for (NeuroInstance instance:instances)
//            {
//                String str = instance.getPipeline();
//                String [] str1 = this.SelectedItem.split(":");
//                String curPipeline = str1[1];
//                if (str.equals(curPipeline))
//                {
//                    mControl.AppendInstance(instance);
//                }
//            }
//            if(mControl.InstanceCount()>0)
//                mControl.SetInstanceSelection(0);
//        }
//    }
}
