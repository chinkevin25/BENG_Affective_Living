package ExtraUtils.GUI;

import android.content.Context;
import android.widget.ListView;

import com.liz.neuroscale.android.R;

/**
 * Created by linzhang on 12/8/2015.
 */
public class GuiList extends GuiBase {

    public GuiList(Context context, ListView widgt)
    {
        //super(context, widgt,android.R.layout.simple_list_item_1, "Start a new console");
        super(context, widgt, R.layout.plugin_console_list);
    }

    public void Append(String str)
    {
        super.Append(str);
        ((ListView)super.mWidgts).setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }
}
