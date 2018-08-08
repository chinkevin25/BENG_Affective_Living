package ExtraUtils.ChartPlot;

import android.graphics.Paint;
import android.util.Log;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;

/**
 * Created by LinZh on 2/21/2016.
 */
public class APDataSeries {
    private int mGraphWidth;
    private SimpleXYSeries mSeries;
    private LineAndPointFormatter mLineFormatter;
    private Double mMin = 0.0;
    private Double mMax = 0.0;

    public APDataSeries(String title, int windowSize, int color)
    {
        // this.mColor = color;
        this.mGraphWidth = windowSize;
        this.mSeries = new SimpleXYSeries(title);
        this.mSeries.useImplicitXVals();
        //
        this.mLineFormatter = new LineAndPointFormatter(color, null, null, null);
        this.mLineFormatter.getLinePaint().setStrokeWidth(3);
        this.mLineFormatter.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        //
        this.mAverage= 0.0;
        this.mMax = 0.0;
        this.mMin = 0.0;
    }

    /**
     * get the current data series
     * @return
     */
    public SimpleXYSeries Series()
    {
        return this.mSeries;
    }

    /**
     * return the current line and point formatter
     * @return
     */
    public LineAndPointFormatter Formatter()
    {
        return this.mLineFormatter;
    }
    /**
     * update mDisplayData of curve
     * @param data
     */
    private Double mAverage = 0.0;
    public void AppendData(Double data)
    {
        /** add the latest history sample:*/
        this.mSeries.addLast(null, data);
        /** Series rid the oldest sample in history:*/
        if (this.mSeries.size() > mGraphWidth) {
            this.mSeries.removeFirst();
        }

//        /**calculate all average value of current data series*/
//        int n = this.mSeries.size();
//        this.mAverage = (this.mAverage * n + data)/(n+1);
        /**calculate maximal and minimal value*/
        if (data > this.mMax)
            this.mMax = data;
        /**get the min temporary value*/
        if (data < this.mMin)
            this.mMin = data;
    }

    /**
     * return the range height of all the datas in current series
     * @return
     */
    public Double Height()
    {
        return (this.mMax - this.mMin)/2;
    }

    /**
     * return the minimal value of current series
     * @return
     */
    public Double Min()
    {
        return this.mMin;
    }

    /**
     * return maximal value of current series
     * @return
     */
    public Double Max(){
        return this.mMax;
    }

    /**
     * returen the average value of current data series
     * @return
     */
//    private Double Average()
//    {
//        return this.mAverage;
//    }

//    /**
//     * calculate all the requirement
//     */
//    private void Calculate()
//    {
//        int n = this.mSeries.size();
//
////        Double sum = 0.0;
////        Double max = (Double)this.mSeries.getY(0);
////        Double min = max;//(Double)this.mSeries.getY(0);
////        try {
////            for (int i = 0; i < this.mSeries.size(); i++) {
////                sum += (Double) this.mSeries.getY(i);
////                /**get the max temporary value, but here maybe some exceptations about out of the bound index**/
////                Double max_temp = (Double) this.mSeries.getY(i);
////                if (max_temp > max)
////                    max = max_temp;
////                /**get the min temporary value**/
////                Double min_temp = (Double) this.mSeries.getY(i);
////                if (min_temp < min)
////                    min = min_temp;
////            }
////        }catch (Exception e)
////        {
////            Log.i("Calculate",e.toString());
////        }
////        this.mMax = max;
////        this.mMin = min;
////        this.mAverage = sum/this.mSeries.size();
//    }
}
