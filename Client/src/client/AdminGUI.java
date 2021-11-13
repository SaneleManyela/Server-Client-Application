/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.swing.*;

/**
 *
 * @author Sanele
 */
public class AdminGUI extends javax.swing.JPanel {

    /**
     * Creates new form AdminGUI
     */
    public AdminGUI() {
        initComponents();
    }

    private final Species species = new Species();
    private final Animal animal = new Animal();
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSpecies = new javax.swing.JButton();
        btnAnimals = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        btnSpecies.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSpecies.setText("Species");
        btnSpecies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpeciesActionPerformed(evt);
            }
        });

        btnAnimals.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAnimals.setText("Animals");
        btnAnimals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimalsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                .addComponent(btnAnimals, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAnimals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(110, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSpeciesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpeciesActionPerformed
        JDialog speciesGUI = new JDialog(Client.currentInstance(), "Species", JDialog.ModalityType.APPLICATION_MODAL);
        speciesGUI.add(this.species);
        speciesGUI.pack();
        speciesGUI.setLocationRelativeTo(null);
        speciesGUI.setVisible(true);
    }//GEN-LAST:event_btnSpeciesActionPerformed

    private void btnAnimalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnimalsActionPerformed
        JDialog animalGUI = new JDialog(Client.currentInstance(), "Animals", JDialog.ModalityType.APPLICATION_MODAL);
        animalGUI.add(this.animal);
        animal.populate(evt);
        animalGUI.pack();
        animalGUI.setLocationRelativeTo(null);
        animalGUI.setVisible(true);
    }//GEN-LAST:event_btnAnimalsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnimals;
    private javax.swing.JButton btnSpecies;
    // End of variables declaration//GEN-END:variables
}
