package ExtraUtils.ChartPlot;

import android.graphics.Color;
import android.util.Log;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by LinZh on 2/21/2016.
 */
public class APChartPlotter {
    public static final int[] mColors = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53),

            Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(42, 109, 130),

            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209),

            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80),

            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
    };

    private ZoomPanXYPlot mPlot;
    /**private multi-series for a smaple */
    private ArrayList<APDataSeries> mDataSeries;
    /**private Thread myThread;*/
    private Redrawer redrawer;
    /**the windowsize*/
    private int mWindowSize = 100;
    private Double mRangeMin = -200.0;
    private Double mRangeMax = 2000.0;
    /**refresh rate of plot UI*/
    private float maxRefreshRate = 100.0f;
    public APChartPlotter(ZoomPanXYPlot plot, int windowSize)
    {
        this.mPlot = plot;
        this.mDataSeries = new ArrayList<>();
        this.mWindowSize = windowSize;

        this.mPlot.setZoomVertically(true);
        this.mPlot.setZoomHorizontally(true);
        mPlot.setRangeBoundaries(mRangeMin, mRangeMax, BoundaryMode.FIXED);
        //       mPlot.setRangeBoundaries(mRangeMin, mRangeMax, BoundaryMode.GROW);
        mPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, (mRangeMax-mRangeMin)/10);

        mPlot.setDomainBoundaries(0, windowSize, BoundaryMode.FIXED);
        mPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, windowSize / 10);
        mPlot.setTicksPerRangeLabel(3);
        mPlot.setDomainLabel("Time");
        mPlot.getDomainLabelWidget().pack();
        mPlot.setRangeLabel("Data");
        mPlot.getRangeLabelWidget().pack();

        mPlot.setPlotMargins(0, 0, 0, 0);
        mPlot.setPadding(0, 0, 0, 0);

        mPlot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
        mPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);

        mPlot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
        mPlot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);

        mPlot.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.WHITE);
        mPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.WHITE);

//        mPlot.getGraphWidget().setSize(new SizeMetrics(0, SizeLayoutType.FILL,
//                0, SizeLayoutType.FILL));

        mPlot.getLegendWidget().getTextPaint().setColor(Color.WHITE);
//        mPlot.getLegendWidget().getBorderPaint().setColor(Color.BLACK);

        mPlot.setRangeValueFormat(new DecimalFormat("#.#"));
        mPlot.setDomainValueFormat(new DecimalFormat("#.#"));
        /**redraw*/
        redrawer = new Redrawer(Arrays.asList(new Plot[]{mPlot}), maxRefreshRate, false);
    }

    /**
     * add a new series to plot
     * @param title
     */
    public void addSeries(String title)
    {
        APDataSeries series = new APDataSeries(title, this.mWindowSize, mColors[this.mDataSeries.size()]);
        this.mDataSeries.add(series);
        this.mPlot.addSeries(series.Series(), series.Formatter());
//        this.mPlot.refreshDrawableState();
//        this.mPlot.redraw();
    }

    /**
     * Resume plot
     */
    public void onResume() {
        redrawer.start();
    }

    /**
     * Pause plot
     */
    public void onPause() {
        redrawer.pause();
    }

    /**
     * Destroy plot
     */
    public void onDestroy() {
        redrawer.finish();
    }

//    /**
//     * update the boundary
//     * @param min
//     * @param max
//     */
//    public void SetBoundary(Double min, Double max)
//    {
//        this.mPlot.setRangeBoundaries(min, max, BoundaryMode.FIXED);
//    }

    /**
     * add data to plot
     * @param datas, multichannel data, the length of data indicates the channel numbers
     */
    public void addData(Double[] datas) {
        if (datas.length != this.mDataSeries.size())
        {
            Log.i("CustomedZL:","APChartPlotter::addData, dimension doesn't match");
            return;
        }

        /**get the average height*/
        int sumHeight = 0;
        for (int i=0; i<this.mDataSeries.size(); i++)
        {
            sumHeight += this.mDataSeries.get(i).Height();
        }
        sumHeight = sumHeight/this.mDataSeries.size();
        /**add data with offset*/
        for (int i = 0; i < this.mDataSeries.size(); i++) {
            this.mDataSeries.get(i).AppendData(datas[i] + i*(sumHeight/2));
        }
        /**set the plot boundaries*/
        if (this.mDataSeries.size() == 0)return;
        this.mRangeMin = this.mDataSeries.get(0).Min();
        this.mRangeMax = this.mDataSeries.get(this.mDataSeries.size()-1).Max();
        this.mPlot.setRangeBoundaries(mRangeMin, mRangeMax, BoundaryMode.FIXED);
    }

    /**
     * Remove all series
     */
    public void removeAll()
    {
        if(this.mPlot != null) {
            this.mDataSeries.clear();
            this.mPlot.clear();
            //this.onDestroy();
        }
//        this.mPlot.refreshDrawableState();
//        this.mPlot.redraw();
    }
}
