/*
 * LAITS Project
 * Arizona State University
 * 
 * @author: rptiwari
 */
package edu.asu.laits.gui.nodeeditor;

import edu.asu.laits.model.Graph;
import edu.asu.laits.model.Task;
import edu.asu.laits.model.Vertex;
import java.awt.*;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *
 * @author Lishan
 */
public class GraphsPanelView extends javax.swing.JPanel {

    JPanel grafica;
    //the width and height of the panel
    int width, height;
    private NodeEditor nodeEditor;
    
    /**
     * Logger *
     */
    private static Logger logs = Logger.getLogger(GraphsPanelView.class);

    public GraphsPanelView(NodeEditor ne) {
        initComponents();
        nodeEditor = ne;

    }

    public void loadGraph() {
        userAnswerPanel.removeAll();
        
        Vertex currentVertex = nodeEditor.getCurrentVertex();
        updateDescription();
        Task task = Task.getInstance();
        Dimension d = new Dimension(575, 300);

        if (currentVertex.getCorrectValues().isEmpty()) {
            currentVertex.genRandomValues();
        }

        PlotPanel plotPanel = new PlotPanel(currentVertex, task.getStartTime(), task.getEndTime(), task.getUnits(), d);
        this.userAnswerPanel.setLayout(new java.awt.GridLayout(1, 1));
        this.userAnswerPanel.add(plotPanel);
    }

    /**
     * This method is used when the user selects a name and description for the
     * node currently being edited
     */
    public void updateDescription() {
        descriptionLabel.setText("<html><b>Description:</b> <br/>" +nodeEditor.getCurrentVertex().getCorrectDescription()+ "</html>");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nodeDescriptionLabel = new javax.swing.JLabel();
        allGraphsPanel = new javax.swing.JPanel();
        userGraphLabel = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(15, 32767));
        userAnswerPanel = new javax.swing.JPanel();
        correctGraphLabel1 = new javax.swing.JLabel();
        correctGraphLabel2 = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nodeDescriptionLabel.setText("<html></html>");
        add(nodeDescriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(746, 101, -1, -1));

        allGraphsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userGraphLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        userGraphLabel.setText("Author's Graph:");
        allGraphsPanel.add(userGraphLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));
        allGraphsPanel.add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 303, 447, 41));

        userAnswerPanel.setMaximumSize(new java.awt.Dimension(575, 300));
        userAnswerPanel.setPreferredSize(new java.awt.Dimension(575, 300));

        javax.swing.GroupLayout userAnswerPanelLayout = new javax.swing.GroupLayout(userAnswerPanel);
        userAnswerPanel.setLayout(userAnswerPanelLayout);
        userAnswerPanelLayout.setHorizontalGroup(
            userAnswerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );
        userAnswerPanelLayout.setVerticalGroup(
            userAnswerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        allGraphsPanel.add(userAnswerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 575, 300));

        correctGraphLabel1.setText("     ");
        allGraphsPanel.add(correctGraphLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 391, -1, -1));

        correctGraphLabel2.setText("                   ");
        allGraphsPanel.add(correctGraphLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 369, -1, -1));

        add(allGraphsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 79, -1, -1));

        descriptionLabel.setText("<html><b>Description:</b></html>");
        descriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        add(descriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 20, 450, 41));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel allGraphsPanel;
    private javax.swing.JLabel correctGraphLabel1;
    private javax.swing.JLabel correctGraphLabel2;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel nodeDescriptionLabel;
    private javax.swing.JPanel userAnswerPanel;
    private javax.swing.JLabel userGraphLabel;
    // End of variables declaration//GEN-END:variables
}