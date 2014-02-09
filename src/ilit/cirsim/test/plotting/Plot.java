package ilit.cirsim.test.plotting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Plot extends ApplicationFrame
{
    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;

    private XYSeries series;
    private String graphTitle, xTitle, yTitle;

    public Plot(String windowsTitle, String lineLegend, String graphTitle,
                String xTitle, String yTitle)
    {
        super(windowsTitle);

        this.graphTitle = graphTitle;
        this.xTitle = xTitle;
        this.yTitle = yTitle;

        series = new XYSeries(lineLegend);
    }

    public void add(double x, double y)
    {
        series.add(x, y);
    }

    public void start()
    {
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                graphTitle, xTitle, yTitle, data, PlotOrientation.VERTICAL,
                true, true, false);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(CHART_WIDTH, CHART_HEIGHT));
        setContentPane(chartPanel);

        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }
}
