package ExtraUtils.GUI;

import android.content.Context;
import android.widget.Spinner;

import com.interaxon.libmuse.Muse;
import com.liz.neuroscale.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinZh on 2/19/2016.
 */
public class GUIMuseDevice extends GuiSelectableBase {

    List<Muse> mPairedMuse;
    /**
     * define a interface
     */
    public interface MuseCallback
    {
        // This is just a regular method so it can return something or
        // take arguments if you like.
        public void onChanged(int position);
    }
    public MuseCallback mMuseCallback;

    public GUIMuseDevice(Context context, Spinner widgt)
    {
        super(context, widgt, R.layout.plugin_console_list);
        super.mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPairedMuse = new ArrayList<>();
    }

    @Override
    public void OnItemSelectedChanged(int position) {
        if (mMuseCallback != null){
            mMuseCallback.onChanged(position);
        }
    }

    /**
     * return current selected output string
     * @return
     */
    public String SelectedMuseString()
    {
        return this.SelectedItem;
    }

    /**
     * return current selected output index
     * @return
     */
    public int SelectedMuseIndex()
    {
        return this.SelectedPos;
    }

    /**
     * get the selected muse
     * @return
     */
    public Muse SelectedMuse()
    {
        return this.mPairedMuse.get(this.SelectedPos);
    }

    /**
     * validation of current selected output node
     * @return
     */
    public boolean ValidMuse()
    {
        return this.isValid();
    }

    /**
     * append a node to current
     * @param muse
     */
    public void AppendMuse(Muse muse){
        String dev_id = muse.getName() + "-" + muse.getMacAddress();
        //Log.i("MuseLib Headband", dev_id);
        //spinnerItems.add(dev_id);
        this.Append(dev_id);
        this.mPairedMuse.add(muse);
    }

    /**
     * append a muse list
     * @param muses
     */
    public void AppendMuse(List<Muse> muses){
        for (Muse muse : muses) {
            AppendMuse(muse);
        }
    }

    /**
     * Clear all the output node
     */
    public void Clear()
    {
        super.Clear();
        this.mPairedMuse.clear();
    }
}
