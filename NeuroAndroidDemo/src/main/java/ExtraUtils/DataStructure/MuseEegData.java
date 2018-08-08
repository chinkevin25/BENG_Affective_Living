package ExtraUtils.DataStructure;

/**
 * Created by LinZh on 1/3/2016.
 */
public class MuseEegData {
    private String[] label = new String[]{"TP9", "FP1", "FP2", "TP10"};
    public MuseEegData(Double[] data){
        if (data.length != label.length)
            return;
        mData = data;
    }
    private Double[] mData;
    /**
     * get the label of muse data
     * @param pos
     * @return
     */
    private String label(int pos){
        return this.label[pos];
    }
    public Double[] Data(){
        return this.mData;
    }
    public int Dim(){
        return this.mData.length;
    }
}
