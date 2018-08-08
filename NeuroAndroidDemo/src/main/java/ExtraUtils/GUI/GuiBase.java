package ExtraUtils.GUI;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

/**
 * Created by linzhang on 12/8/2015.
 */
public class GuiBase {
    //protected String DefaultTag;
    protected Object mWidgts;
    private Context mContext;
    protected ArrayList<String> mDisplayData;   // this data used for display
    protected ArrayAdapter<String> mAdapter;
    protected String SelectedItem;
    protected int SelectedPos;

    protected Context getContext()
    {
        return this.mContext;
    }
    /**
     * Constructor
     * @param context
     * @param sampleResurces
     * @param widgt
     */
    public GuiBase(Context context, Object widgt, int sampleResurces)
    {
        this.mContext = context;
        this.mWidgts = widgt;
        this.SelectedItem = null;
        this.SelectedPos= -1;

        this.mDisplayData = new ArrayList<>();
//        this.mDisplayData.add(this.DefaultTag);
        this.mAdapter = new ArrayAdapter<String>(this.mContext, sampleResurces, this.mDisplayData);

        if (this.mWidgts == null) return;
               ((AdapterView)this.mWidgts).setAdapter(this.mAdapter);

    }

    /**
     * Append
     * @param str
     */
    public void Append(final String str)
    {
        if (this.mWidgts == null) return;

        ((AdapterView)this.mWidgts).post(new Runnable() {
            @Override
            public void run() {
                if (mDisplayData != null) {
                    mDisplayData.add(str);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Clear
     */
    public void Clear()
    {
        if (this.mWidgts == null) return;
        ((AdapterView)this.mWidgts).post(new Runnable() {
            @Override
            public void run() {
                if (mDisplayData != null && mDisplayData.size()>0) {
                    mDisplayData.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Remove at
     */
    public void RemoveAt(final int pos) {
        if (this.mWidgts == null) return;

        ((AdapterView)this.mWidgts).post(new Runnable() {
            @Override
            public void run() {
                if ((!mDisplayData.isEmpty()) && (pos<=mDisplayData.size())) {
                    mDisplayData.remove(pos);
                    mAdapter.notifyDataSetChanged();
                    ((AdapterView)mWidgts).invalidate();
                }
            }
        });
    }

    /**
     * set enable of current GUI
     * @param isEnable
     */
    public void SetEnable(final boolean isEnable){
        if (this.mWidgts == null) return;

        ((AdapterView)this.mWidgts).post(new Runnable() {
            @Override
            public void run() {
                ((AdapterView) mWidgts).setEnabled(isEnable);
            }
        });
    }

    /**
     * return current size of list
     * @return
     */
    public int Count(){
        if (this.mWidgts != null)
            return mDisplayData.size();

        return 0;
    }
}
