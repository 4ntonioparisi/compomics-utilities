package com.compomics.util.gui.parameters.identification_parameters;

import com.compomics.util.experiment.identification.Advocate;
import com.compomics.util.experiment.identification.psm_scoring.PsmScore;
import com.compomics.util.gui.error_handlers.HelpDialog;
import com.compomics.util.preferences.PsmScoringPreferences;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import no.uib.jsparklines.extra.TrueFalseIconRenderer;

/**
 * Dialog for the edition of the PSM scoring settings.
 *
 * @author Marc Vaudel
 */
public class PsmScoringSettingsDialog extends javax.swing.JDialog {

    /**
     * The parent frame.
     */
    private java.awt.Frame parentFrame;
    /**
     * Boolean indicating whether the user canceled the editing.
     */
    private boolean canceled = false;
    /**
     * Boolean indicating whether the settings can be edited by the user.
     */
    private boolean editable;
    /**
     * The scores used to score the spectrum matches for every advocate in a
     * map: advocate index &gt; list of score indexes.
     */
    private HashMap<Integer, HashSet<Integer>> spectrumMatchingScores = null;
    /**
     * The scores to use by default.
     */
    private HashSet<Integer> defaultScores;
    /**
     * The table column header tooltips.
     */
    private ArrayList<String> psmScoresTableToolTips;
    /**
     * List of the advocates in the table.
     */
    private ArrayList<String> advocateNames;
    /**
     * List of the scores in the table.
     */
    private ArrayList<String> scoresNames;

    /**
     * Creates a new PsmScoringSettingsDialog with a frame as owner.
     *
     * @param parentFrame a parent frame
     * @param psmScoringPreferences the scoring preferences to display
     * @param editable boolean indicating whether the settings can be edited by
     * the user
     */
    public PsmScoringSettingsDialog(java.awt.Frame parentFrame, PsmScoringPreferences psmScoringPreferences, boolean editable) {
        super(parentFrame, true);
        this.parentFrame = parentFrame;
        this.editable = editable;
        initComponents();
        setUpGui();
        populateGUI(psmScoringPreferences);
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    /**
     * Creates a new PsmScoringSettingsDialog with a dialog as owner.
     *
     * @param owner the dialog owner
     * @param parentFrame a parent frame
     * @param psmScoringPreferences the scoring preferences to display
     * @param editable boolean indicating whether the settings can be edited by
     * the user
     */
    public PsmScoringSettingsDialog(Dialog owner, java.awt.Frame parentFrame, PsmScoringPreferences psmScoringPreferences, boolean editable) {
        super(owner, true);
        this.parentFrame = parentFrame;
        this.editable = editable;
        initComponents();
        populateGUI(psmScoringPreferences);
        setUpGui();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    /**
     * Set up the GUI.
     */
    private void setUpGui() {
        setTableProperties();
    }

    /**
     * Sets the table properties.
     */
    private void setTableProperties() {
        TableColumnModel tableColumnModel = psmScoresJTable.getColumnModel();
        tableColumnModel.getColumn(0).setMaxWidth(30);
        psmScoresJScrollPane.getViewport().setOpaque(false);
        for (int i = 0; i < PsmScore.values().length; i++) {
            tableColumnModel.getColumn(i + 2).setCellRenderer(new TrueFalseIconRenderer(
                    new ImageIcon(this.getClass().getResource("/icons/selected_green.png")),
                    null,
                    "On", "Off"));
        }
    }

    /**
     * Fills the GUI with the given settings.
     *
     * @param psmScoringPreferences the scoring preferences to display
     */
    private void populateGUI(PsmScoringPreferences psmScoringPreferences) {

        // get all implemented scores
        PsmScore[] psmScores = PsmScore.values();
        scoresNames = new ArrayList<String>(psmScores.length);
        for (PsmScore psmScore : psmScores) {
            scoresNames.add(psmScore.name);
        }

        // get scores for each algorithm
        Set<Integer> advocates = psmScoringPreferences.getAdvocates();
        spectrumMatchingScores = new HashMap<Integer, HashSet<Integer>>(advocates.size());
        for (Integer advocate : advocates) {
            HashSet<Integer> scores = new HashSet<Integer>(psmScoringPreferences.getScoreForAlgorithm(advocate));
            spectrumMatchingScores.put(advocate, scores);
        }
        defaultScores = new HashSet<Integer>(psmScoringPreferences.getDefaultScores());

        // make an ordered list of the selected algorithms
        advocateNames = new ArrayList<String>(spectrumMatchingScores.size());
        for (Integer advocateId : advocates) {
            Advocate advocate = Advocate.getAdvocate(advocateId);
            advocateNames.add(advocate.getName());
        }
        Collections.sort(advocateNames);

        // populate table
        psmScoresJTable.setModel(new AlgorithmScoresTableModel());

        // set scores
        PsmScore[] implementedScores = PsmScore.values();
        psmScoresTableToolTips = new ArrayList<String>(implementedScores.length);
        psmScoresTableToolTips.add(null);
        psmScoresTableToolTips.add("Score Name");
        for (PsmScore psmScore : implementedScores) {
            psmScoresTableToolTips.add(psmScore.description);
        }
    }

    /**
     * Updates the content of the table.
     */
    private void updateTableContent() {
        ((DefaultTableModel) psmScoresJTable.getModel()).fireTableDataChanged();
    }

    /**
     * Returns the PSM scoring preferences as set by the user.
     *
     * @return the PSM scoring preferences as set by the user
     */
    public PsmScoringPreferences getPsmScoringPreferences() {
        PsmScoringPreferences psmScoringPreferences = new PsmScoringPreferences();
        psmScoringPreferences.clearAllScores();
        for (Integer algorithm : spectrumMatchingScores.keySet()) {
            HashSet<Integer> scores = spectrumMatchingScores.get(algorithm);
            for (Integer score : scores) {
                psmScoringPreferences.addScore(algorithm, score);
            }
        }
        return psmScoringPreferences;
    }

    /**
     * Indicates whether the user canceled the editing.
     *
     * @return a boolean indicating whether the user canceled the editing
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        psmScoresPopupMenu = new javax.swing.JPopupMenu();
        addJMenuItem = new javax.swing.JMenuItem();
        removeJMenuItem = new javax.swing.JMenuItem();
        backgroundPanel = new javax.swing.JPanel();
        scoresSelectionJPanel = new javax.swing.JPanel();
        psmScoresJScrollPane = new javax.swing.JScrollPane();
        psmScoresJTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        return (String) psmScoresTableToolTips.get(realIndex);
                    }
                };
            }
        };
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpJButton = new javax.swing.JButton();

        addJMenuItem.setText("Add");
        addJMenuItem.setEnabled(false);
        addJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJMenuItemActionPerformed(evt);
            }
        });
        psmScoresPopupMenu.add(addJMenuItem);

        removeJMenuItem.setText("Remove");
        removeJMenuItem.setEnabled(false);
        removeJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJMenuItemActionPerformed(evt);
            }
        });
        psmScoresPopupMenu.add(removeJMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PSM Scoring");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));
        backgroundPanel.setToolTipText("PSM Scoring");

        scoresSelectionJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PSM Scores Selection"));
        scoresSelectionJPanel.setOpaque(false);

        psmScoresJTable.setModel(new AlgorithmScoresTableModel());
        psmScoresJTable.getTableHeader().setReorderingAllowed(false);
        psmScoresJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                psmScoresJTableMouseReleased(evt);
            }
        });
        psmScoresJScrollPane.setViewportView(psmScoresJTable);

        javax.swing.GroupLayout scoresSelectionJPanelLayout = new javax.swing.GroupLayout(scoresSelectionJPanel);
        scoresSelectionJPanel.setLayout(scoresSelectionJPanelLayout);
        scoresSelectionJPanelLayout.setHorizontalGroup(
            scoresSelectionJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scoresSelectionJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(psmScoresJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );
        scoresSelectionJPanelLayout.setVerticalGroup(
            scoresSelectionJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scoresSelectionJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(psmScoresJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addContainerGap())
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        helpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help.GIF"))); // NOI18N
        helpJButton.setToolTipText("Help");
        helpJButton.setBorder(null);
        helpJButton.setBorderPainted(false);
        helpJButton.setContentAreaFilled(false);
        helpJButton.setFocusable(false);
        helpJButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        helpJButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        helpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                helpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                helpJButtonMouseExited(evt);
            }
        });
        helpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(helpJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(scoresSelectionJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoresSelectionJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(helpJButton)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Close the dialog.
     *
     * @param evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Cancel the dialog.
     *
     * @param evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        canceled = true;
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Cancel the dialog.
     *
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        canceled = true;
    }//GEN-LAST:event_formWindowClosing

    /**
     * Update the scores.
     * 
     * @param evt 
     */
    private void psmScoresJTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_psmScoresJTableMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON1) {
            int row = psmScoresJTable.getSelectedRow();
            int column = psmScoresJTable.getSelectedColumn();
            if (column > 1 && row < advocateNames.size()) {
                String advocateName = advocateNames.get(row);
                Advocate advocate = Advocate.getAdvocate(advocateName);
                Integer advocateIndex = advocate.getIndex();
                HashSet<Integer> scores = spectrumMatchingScores.get(advocateIndex);
                String scoreName = scoresNames.get(column - 2);
                PsmScore psmScore = PsmScore.getScore(scoreName);
                Integer scoreIndex = psmScore.index;
                if (scores.contains(scoreIndex)) {
                    scores.remove(scoreIndex);
                } else {
                    scores.add(scoreIndex);
                }
                if (scores.isEmpty()) {
                    scores.add(PsmScore.native_score.index);
                }
                updateTableContent();
            }
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            psmScoresJTable.setRowSelectionInterval(psmScoresJTable.rowAtPoint(evt.getPoint()), psmScoresJTable.rowAtPoint(evt.getPoint()));
            psmScoresPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_psmScoresJTableMouseReleased

    /**
     * Select a score.
     * 
     * @param evt 
     */
    private void addJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJMenuItemActionPerformed
        // TODO: let the user select a score
    }//GEN-LAST:event_addJMenuItemActionPerformed

    /**
     * Remove a score.
     * 
     * @param evt 
     */
    private void removeJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeJMenuItemActionPerformed
        int row = psmScoresJTable.getSelectedRow();
        String advocateName = advocateNames.get(row);
        Advocate advocate = Advocate.getAdvocate(advocateName);
        Integer advocateIndex = advocate.getIndex();
        spectrumMatchingScores.remove(advocateIndex);
    }//GEN-LAST:event_removeJMenuItemActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void helpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void helpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void helpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(parentFrame, getClass().getResource("/helpFiles/PsmScoringPreferences.html"),
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/help.GIF")),
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
            "PSM Scoring - Help");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addJMenuItem;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton helpJButton;
    private javax.swing.JButton okButton;
    private javax.swing.JScrollPane psmScoresJScrollPane;
    private javax.swing.JTable psmScoresJTable;
    private javax.swing.JPopupMenu psmScoresPopupMenu;
    private javax.swing.JMenuItem removeJMenuItem;
    private javax.swing.JPanel scoresSelectionJPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Table model for the scores table.
     */
    private class AlgorithmScoresTableModel extends DefaultTableModel {

        /**
         * Constructor.
         */
        public AlgorithmScoresTableModel() {

        }

        @Override
        public int getRowCount() {
            if (advocateNames == null) {
                return 0;
            }
            return advocateNames.size() + 1;
        }

        @Override
        public int getColumnCount() {
            if (scoresNames == null) {
                return 0;
            }
            return scoresNames.size() + 2;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return " ";
            }
            if (column == 1) {
                return "  ";
            }
            return scoresNames.get(column - 2);
        }

        @Override
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:
                    return row + 1;
                case 1:
                    if (row < advocateNames.size()) {
                        return advocateNames.get(row);
                    }
                    return "Default";
                default:
                    String scoreName = scoresNames.get(column - 2);
                    PsmScore psmScore = PsmScore.getScore(scoreName);
                    Integer scoreIndex = psmScore.index;
                    if (row < advocateNames.size()) {
                        String name = advocateNames.get(row);
                        Advocate advocate = Advocate.getAdvocate(name);
                        Integer index = advocate.getIndex();
                        HashSet<Integer> algorithmScores = spectrumMatchingScores.get(index);
                        if (algorithmScores == null || algorithmScores.isEmpty()) {
                            return false;
                        }
                        return algorithmScores.contains(scoreIndex);
                    }
                    return defaultScores.contains(scoreIndex);
            }
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            for (int i = 0; i < getRowCount(); i++) {
                if (getValueAt(i, columnIndex) != null) {
                    return getValueAt(i, columnIndex).getClass();
                }
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (advocateNames == null) {
                return false;
            }
            return columnIndex > 1 && rowIndex < advocateNames.size();
        }
    }
}
