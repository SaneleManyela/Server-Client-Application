/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Sanele
 */
public class ChooseRecord extends JDialog{
    public ChooseRecord(String strQuery) {
        super(Client.currentInstance(), "Select Record", ModalityType.APPLICATION_MODAL);
        ChooseRecord.this.setSize(400, 200);
        ChooseRecord.this.setResizable(false);
        ChooseRecord.this.setLocationRelativeTo(null);
        ChooseRecord.this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                
        JComboBox cbo = new JComboBox(Client.currentInstance().contactServer(
                new String[]{strQuery, "Get Field"}));
               
        JButton btnOk = new JButton("Okay");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strOption = cbo.getSelectedItem().toString();
                ChooseRecord.this.dispose();
            }
        });
        
        JPanel jpDialogPanel = new JPanel(new BorderLayout(0, 20));
        jpDialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        jpDialogPanel.add(new JLabel("Select Option:"), BorderLayout.NORTH);
        jpDialogPanel.add(cbo);
        jpDialogPanel.add(btnOk, BorderLayout.SOUTH);
        
        jpDialogPanel.setBackground(new Color(255, 255, 255));
        ChooseRecord.this.add(jpDialogPanel);
    }
    
    private static String strOption;
    
    public static String getOption() {
        return strOption;
    }
}
