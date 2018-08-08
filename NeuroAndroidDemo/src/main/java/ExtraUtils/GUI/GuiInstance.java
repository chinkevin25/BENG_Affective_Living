package ExtraUtils.GUI;

import android.content.Context;
import android.widget.Spinner;

import com.liz.neuroscale.android.R;

/**
 * Created by linzhang on 12/8/2015.
 */
public class GuiInstance extends GuiSelectableBase /*implements Spinner.OnItemSelectedListener*/ {

    public GuiInstance(Context context, Spinner widgt)
    {
        super(context, widgt, R.layout.plugin_spinner_text);
        super.mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }



    @Override
    public void OnItemSelectedChanged(int position) {

    }

    /**
     * is the selected item create new item
     * @return
     */
    public boolean isNewInstance()
    {
        if(this.isValid() &&
                this.SelectedItem.equals(getContext().getString(R.string.TAG_CREATE_NEW))){
            return true;
        }
        return false;
    }
}
