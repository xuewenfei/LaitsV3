/**
 * (c) 2013, Arizona Board of Regents for and on behalf of Arizona State
 * University. This file is part of LAITS.
 *
 * LAITS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * LAITS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LAITS. If not, see <http://www.gnu.org/licenses/>.
 */
package edu.asu.laits.gui.nodeeditor;

import edu.asu.laits.editor.ApplicationContext;
import edu.asu.laits.model.Vertex;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTaskPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;

public class PlotPanel extends JXTaskPane {

    private String units;
    private static Logger logs = Logger.getLogger("DevLogs");
    private static Logger activityLogs = Logger.getLogger("ActivityLogs");
    private static Color transparent = new Color(0, 0, 0, 0);

    public PlotPanel(Vertex vertex, int x0, String units) {
        logs.info("Initializing Plot Panel. Vertex: "+vertex.getName()+
                "  Initial Point: "+x0+" Units: "+units);
        List<Vertex> v = new ArrayList<Vertex>();
        v.add(vertex);
        this.units = units;

        if (ApplicationContext.isStudentMode() || 
                ApplicationContext.isCoachedMode()) {
            Vertex correctVertex = ApplicationContext.getCorrectSolution()
                    .getSolutionGraph().getVertexByName(vertex.getName());
            v.add(correctVertex);
        }

        Dimension d = new Dimension(575, 190);
        init(v, x0, d);
    }

    private void init(List<Vertex> vertices, int x0, Dimension d) {
        this.setTitle(vertices.get(0).getName());
        this.setBackground(transparent);
        this.setOpaque(false);

        XYDataset dataset = createSolutionDataset(vertices, x0);
        double start = x0;
        double end = vertices.get(0).getCorrectValues().size() + start - 1;
        double tick = (end - start) / 20;
        tick = Math.ceil(tick);
        JFreeChart chart = createChart(dataset, vertices.get(0).getName(), start, end, tick);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));

        // The size of the panel depends on the size of the GraphDialog panel

        chartPanel.setPreferredSize(d);
        chartPanel.setSize(d);
        chartPanel.setMinimumSize(d);

        add(chartPanel);

    }

    /**
     * This method creates a solution dataset used to create the solution graphs
     *
     * @param taskName
     * @return
     */
    private XYDataset createSolutionDataset(List<Vertex> vertices, int x0) {
        logs.info("Creating Solution Dataset for PlotPanel");
        XYSeriesCollection dataset = new XYSeriesCollection();
        String legends[] = new String[2];
        legends[0] = "Your Graph";
        legends[1] = "Target Graph";

        if (ApplicationContext.isAuthorMode()) {
            legends[0] = "Author's Graph";
        }
        int legendIndex = 0;

        for (Vertex v : vertices) {
            int start = x0;
            XYSeries series = new XYSeries(legends[legendIndex++]);
            List<Double> correctValues = v.getCorrectValues();


            double end = correctValues.size() + start - 1;
            double tick = (end - start) / 20;
            tick = Math.ceil(tick);
            
            for (int i = 0; i < correctValues.size(); i += tick) {
                series.add(start, correctValues.get(i));
                start += tick;
            }

            dataset.addSeries(series);
        }

        return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset, String vertex, double start, double end, double tick) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                vertex, // chart title
                "Time (in " + units + ")", // x axis label
                vertex, // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setRange(start, end);
        domain.setTickUnit(new NumberTickUnit(tick));
        domain.setVerticalTickLabels(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;

            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
            renderer.setSeriesShape(0, ShapeUtilities.createDownTriangle(3.5f));
            renderer.setSeriesShape(1, ShapeUtilities.createUpTriangle(3.5f));
        }

        return chart;
    }
}
