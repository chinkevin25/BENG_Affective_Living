package ExtraUtils.GUI;

import android.content.Context;
import android.widget.Spinner;

import com.liz.neuroscale.android.R;

/**
 * Created by LinZh on 2/19/2016.
 */
public class GUIOutputNode extends GuiSelectableBase {

    public GUIOutputNode(Context context, Spinner widgt)
    {
        super(context, widgt, R.layout.plugin_console_list);
        super.mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void OnItemSelectedChanged(int position) {
        if (mOutputChangedCallback != null){
            mOutputChangedCallback.onOuputChanged(position);
        }
    }

    public OuputChangedCallback mOutputChangedCallback;
    /**
     * return current selected output string
     * @return
     */
    public String SelectedOutputString()
    {
        return this.SelectedItem;
    }

    /**
     * return current selected output index
     * @return
     */
    public int SelectedOutputIndex()
    {
        return this.SelectedPos;
    }

    /**
     * validation of current selected output node
     * @return
     */
    public boolean ValidOutputNode()
    {
        return this.isValid();
    }

    /**
     * append a node to current
     * @param node
     */
    public void AppendOutputNode(String node){
        this.Append(node);
    }

    /**
     * Clear all the output node
     */
    public void ClearOutputNode()
    {
        super.Clear();
    }


//    /** Called when the user clicks the Download button */
//    public void preformDownload(View view)
//    {
//    /* just like define a variable, implement that callback function, then assign to the class's attribute */
//        OuputCallback outputCallback = new OuputCallback()
//        {
//            public void updateProgress(long currentSize, long totalSize){
//            /* do what you want to do here */
//            }
//        };
//
//        songtaste.stDownloadFromUrl(strSongUrl, fullFilename, outputCallback);
//    }

    /**
     * define a interface
     */
    public interface OuputChangedCallback
    {
        // This is just a regular method so it can return something or
        // take arguments if you like.
        public void onOuputChanged(int position);
    }
//
//    public Boolean downlodFile(String url, File fullFilename, HttpParams headerParams, OuputCallback updateProgressCallbak)
//    {
//        //...
//        // call this function as normal
//        updateProgressCallbak.updateProgress(downloadedSize, totalSize);
//        //...
//    }
}
