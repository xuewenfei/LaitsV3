/*
 * LAITS Project
 * Arizona State University
 * (c) 2013, Arizona Board of Regents for and on behalf of Arizona State University.
 * This file is part of LAITS.
 *
 * LAITS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LAITS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with LAITS.  If not, see <http://www.gnu.org/licenses/>.
 * @author: rptiwari
 */

package edu.asu.laits.gui;

import edu.asu.laits.editor.ApplicationContext;
import edu.asu.laits.model.PlotPanel;
import edu.asu.laits.model.Edge;
import edu.asu.laits.model.Graph;
import edu.asu.laits.model.TablePanel;
import edu.asu.laits.model.Task;
import edu.asu.laits.model.Vertex;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;

/**
 *
 * @author ramayantiwari
 * 
 */
public class GraphViewPanel{
    private Graph<Vertex, Edge> currentGraph;
    private JXTaskPaneContainer chartContainer;
    private JDialog parent;
    private JSlider testSlider;
    private Mode mode;
    
    private static Logger logs = Logger.getLogger("DevLogs");
    
    public GraphViewPanel(Graph<Vertex, Edge> graph, JDialog parent, Mode mode){
        currentGraph = graph;
        this.parent = parent;
        this.mode = mode;
        initializeComponents();

        JScrollPane panelScroll = new JScrollPane(chartContainer);
        parent.add(panelScroll);
    }
     
    private void initializeComponents(){
        chartContainer = new JXTaskPaneContainer();
        addCharts();
        addSliders();
    }
    
    private void addSliders() {
        Task t = ApplicationContext.getCurrentTask();
        DoubleJSlider newSlider;
        JLabel sliderLabel;
        JFormattedTextField sliderAmount;
        
        for(Vertex currentVertex : currentGraph.vertexSet()) {
            if(currentVertex.getVertexType().equals(Vertex.VertexType.CONSTANT)) {
                newSlider = addSlider(currentVertex);
                newSlider.setPaintTicks(true);
                newSlider.setPaintLabels(true);
                chartContainer.add(newSlider);
                sliderLabel = new JLabel(currentVertex.getName());
                chartContainer.add(sliderLabel);
                sliderAmount = new JFormattedTextField();
                sliderAmount.setText(String.valueOf(newSlider.getDoubleValue()));
                chartContainer.add(sliderAmount);
                newSlider.addChangeListener(new SliderListener(sliderAmount, currentVertex, this));
            }
        }
    }
    
    private DoubleJSlider addSlider(Vertex vertex){
        //handle negative values
        if(vertex.getInitialValue() > 0)
            return new DoubleJSlider(0, 5*vertex.getInitialValue(), vertex.getInitialValue());
        else
            return new DoubleJSlider(vertex.getInitialValue()-1 , -5*vertex.getInitialValue(), vertex.getInitialValue());
            
    }
    
    private void addCharts(){
        Task t = ApplicationContext.getCurrentTask();
        
        //if Mode is Graph Mode, Plot Graph against each not constant node
        if(mode.equals(Mode.Graph)){
            for(Vertex currentVertex : currentGraph.vertexSet()){
                logs.debug("Attempting to add chart for " + currentVertex.getName());
                JXTaskPane plotPanel = addChart(currentVertex);
                if(plotPanel != null){
                    chartContainer.add(plotPanel);
                    map.put(plotPanel, currentVertex);
                }
            }
        }else{
           logs.debug("Attempting to plot table for given graph");
           JXTaskPane tablePanel = addTable(currentGraph);
           if(tablePanel != null){
               chartContainer.add(tablePanel);
           }
        }        
    }
    
    private JXTaskPane addTable(Graph currentGraph){
        TablePanel tablePanel = new TablePanel(currentGraph);
        return tablePanel;
    }
    
    private JXTaskPane addChart(Vertex vertex){
        PlotPanel plotPanel = null;
        
        if(!vertex.getVertexType().equals(Vertex.VertexType.CONSTANT)){
            plotPanel = new PlotPanel(vertex);
        }
        
        return plotPanel;
    }
      
    // What are these used for ???
    int count1 = 0;
    int count2 = 0;
            
    public void repaintCharts(Graph<Vertex,Edge> clonnedGraph){
        Component[] components = chartContainer.getComponents();
        for(Component c : components){
            if(c instanceof PlotPanel){
                logs.info("repaint check succeeded, attempting to repaint " + map.get(c).getName());
             ((PlotPanel)c).updateChartAfterSliderChange(clonnedGraph,map.get(c));
            }
        }
        //restore oringal 
       // PlotPanel.restoreOrignal(currentGraph, vertexValues);
   }
    
    private void repaintTable(Graph<Vertex,Edge> clonnedGraph) {        
        Component[] components = chartContainer.getComponents();
        for(Component c : components){
            if(c instanceof TablePanel){
                logs.debug("updating table ");
                ((TablePanel)c).updateTableData(clonnedGraph);
            }
        }
    }
    
    public enum Mode{
        Graph,
        Table
    }    
    
    Map<JComponent, Vertex> map = new HashMap<JComponent, Vertex>();
    /**
     * Custom Listener for Slider Change event
     */
    private class SliderListener implements ChangeListener {        
        private JFormattedTextField textSource;
        private Vertex vertex;
        private GraphViewPanel panel;
        
        public SliderListener(JFormattedTextField amount, Vertex v, GraphViewPanel g) {
            textSource = amount;
            vertex = v;
            panel = g;
        } 

        @Override
        public void stateChanged(ChangeEvent e) {
            DoubleJSlider source = (DoubleJSlider)e.getSource();
            textSource.setText(String.valueOf(source.getDoubleValue()));
            Graph<Vertex,Edge> clonnedGraph = (Graph<Vertex,Edge>) currentGraph.clone();
            //modify value in new vertex
            clonnedGraph.getVertexByName(vertex.getName()).setInitialValue(source.getDoubleValue());
            
            if(mode.equals(Mode.Graph))
                panel.repaintCharts(clonnedGraph);
            else
                panel.repaintTable(clonnedGraph);
        }
    }
}
