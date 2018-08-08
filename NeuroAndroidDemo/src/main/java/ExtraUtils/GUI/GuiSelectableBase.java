package ExtraUtils.GUI;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

/**
 * Created by linzhang on 12/9/2015.
 */
public abstract class GuiSelectableBase extends GuiBase implements AdapterView.OnItemSelectedListener {
    /**
     * Constructor
     *
     * @param context
     * @param widgt
     * @param sampleResurces
     */
    public GuiSelectableBase(Context context, Object widgt, int sampleResurces) {
        super(context, widgt, sampleResurces);
        if (this.mWidgts == null) return;
        ((AdapterView) this.mWidgts).setSelection(0);
        ((AdapterView) this.mWidgts).setOnItemSelectedListener(this);
    }

    /**
     * setEnable
     * @param isEnable
     */
    public void SetEnable(final boolean isEnable){
        if (this.mWidgts == null) return;
        ((AdapterView) mWidgts).post(new Runnable() {
            @Override
            public void run() {
                ((AdapterView) mWidgts).setEnabled(isEnable);
            }
        });
    }

    /**
     * set the current selection, selection should greater than 0 and less than size
     * @param position
     */
    public void SetSelection(final int position) {
        if (this.mWidgts == null) return;
        ((AdapterView) mWidgts).post(new Runnable() {
            @Override
            public void run() {
                if ((position >= 0) && (position < mDisplayData.size())) {
                    ((AdapterView) mWidgts).clearFocus();
                    ((AdapterView) mWidgts).setSelection(position);

//                    ((AdapterView) mWidgts).invalidate();
//                    ((AdapterView) mWidgts).requestFocusFromTouch();

                    SelectedPos = position;
                    SelectedItem = mDisplayData.get(position);
                }
            }
        });
    }

    /**
     * set the last item to be the current selected item
     */
    public void SetLast(){
        SetSelection(this.Count()-1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (this.mDisplayData.size() > 0) {
            this.SelectedItem = mDisplayData.get(position);
            this.SelectedPos = position;
            OnItemSelectedChanged(position);
        }
    }

    // do something when changed selection
    public abstract void OnItemSelectedChanged(int position);

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Clear() {
        super.Clear();
    }

    public boolean isValid() {
        if (this.mDisplayData.size() > 0 && this.SelectedItem != null)/**added by zhanglin*/
            return true;
        else return false;
    }

    /**
     * Remove the selected item.
     *
     * @return
     */
    public boolean RemoveSelected() {
        try {
            this.RemoveAt(this.SelectedPos);
        } catch (Exception e) {
            Log.i("RemoveSelected", e.toString());
            return false;
        }
        return true;
    }
}
