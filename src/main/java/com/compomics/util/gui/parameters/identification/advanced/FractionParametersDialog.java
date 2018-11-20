package com.compomics.util.gui.parameters.identification.advanced;

import com.compomics.util.gui.error_handlers.HelpDialog;
import com.compomics.util.parameters.identification.advanced.FractionParameters;
import java.awt.Dialog;
import java.awt.Toolkit;

/**
 * FractionSettingsDialog.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class FractionParametersDialog extends javax.swing.JDialog {

    /**
     * Empty default constructor
     */
    public FractionParametersDialog() {
    }

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
     * Creates a new FractionSettingsDialog with a frame as owner.
     *
     * @param parentFrame a parent frame
     * @param fractionSettings the fraction settings
     * @param editable boolean indicating whether the settings can be edited by
     * the user
     */
    public FractionParametersDialog(java.awt.Frame parentFrame, FractionParameters fractionSettings, boolean editable) {
        super(parentFrame, true);
        this.parentFrame = parentFrame;
        this.editable = editable;
        initComponents();
        setUpGui();
        populateGUI(fractionSettings);
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    /**
     * Creates a new FractionSettingsDialog with a dialog as owner.
     *
     * @param owner the dialog owner
     * @param parentFrame a parent frame
     * @param fractionSettings the fraction settings
     * @param editable boolean indicating whether the settings can be edited by
     * the user
     */
    public FractionParametersDialog(Dialog owner, java.awt.Frame parentFrame, FractionParameters fractionSettings, boolean editable) {
        super(owner, true);
        this.parentFrame = parentFrame;
        this.editable = editable;
        initComponents();
        setUpGui();
        populateGUI(fractionSettings);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    /**
     * Sets up the GUI.
     */
    private void setUpGui() {
        proteinMwSpinner.setEnabled(editable);
    }

    /**
     * Fills the GUI with the given settings.
     *
     * @param fractionSettings the fraction settings to display
     */
    private void populateGUI(FractionParameters fractionSettings) {
        proteinMwSpinner.setValue(fractionSettings.getProteinConfidenceMwPlots());
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
     * Returns the fraction settings as set by the user.
     *
     * @return the fraction settings as set by the user
     */
    public FractionParameters getFractionSettings() {
        FractionParameters fractionSettings = new FractionParameters();
        fractionSettings.setProteinConfidenceMwPlots((Double) proteinMwSpinner.getValue());
        return fractionSettings;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgrounPanel = new javax.swing.JPanel();
        fractionsPanel = new javax.swing.JPanel();
        proteinMwLabel = new javax.swing.JLabel();
        proteinMwSpinner = new javax.swing.JSpinner();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        helpJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fraction Analysis");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        backgrounPanel.setBackground(new java.awt.Color(230, 230, 230));

        fractionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Fractions (Beta)"));
        fractionsPanel.setOpaque(false);

        proteinMwLabel.setText("Protein Confidence MW (%)");
        proteinMwLabel.setToolTipText("<html>\nThe minium protein confidence required to be included in the<br>\naverage molecular weight analysis in the Fractions tab.\n</html>");

        proteinMwSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));

        javax.swing.GroupLayout fractionsPanelLayout = new javax.swing.GroupLayout(fractionsPanel);
        fractionsPanel.setLayout(fractionsPanelLayout);
        fractionsPanelLayout.setHorizontalGroup(
            fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fractionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(proteinMwLabel)
                .addGap(18, 18, 18)
                .addComponent(proteinMwSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addContainerGap())
        );
        fractionsPanelLayout.setVerticalGroup(
            fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fractionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fractionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proteinMwLabel)
                    .addComponent(proteinMwSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
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

        javax.swing.GroupLayout backgrounPanelLayout = new javax.swing.GroupLayout(backgrounPanel);
        backgrounPanel.setLayout(backgrounPanelLayout);
        backgrounPanelLayout.setHorizontalGroup(
            backgrounPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgrounPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fractionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgrounPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(helpJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        backgrounPanelLayout.setVerticalGroup(
            backgrounPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgrounPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fractionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgrounPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(helpJButton)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgrounPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgrounPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        new HelpDialog(parentFrame, getClass().getResource("/helpFiles/FractionPreferences.html"),
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/help.GIF")),
            Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
            "Fraction Analysis - Help");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_helpJButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgrounPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel fractionsPanel;
    private javax.swing.JButton helpJButton;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel proteinMwLabel;
    private javax.swing.JSpinner proteinMwSpinner;
    // End of variables declaration//GEN-END:variables

}