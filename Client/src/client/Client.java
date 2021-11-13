/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.*;
import static java.awt.Frame.ICONIFIED;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Sanele
 */
public class Client extends JFrame implements ActionListener{
    private final JPanel jpMainPanel = new JPanel();
    
    private final JMenu mnuFile = new JMenu("File");
    
    private final JMenuItem mnuItemClose = new JMenuItem("Close");
        
    private final JMenuItem mnuItemLogout = new JMenuItem("Logout");
    
    private final JMenuItem mnuItemAdminGUI = new JMenuItem("Administrator");
    
    private final JMenuItem mnuItemGeneralUser = new JMenuItem("User");
    
    private final JRadioButtonMenuItem rbMnuItemWindows = new JRadioButtonMenuItem("Windows");
    private final JRadioButtonMenuItem rbMnuItemMotif = new JRadioButtonMenuItem("Motif");
    private final JRadioButtonMenuItem rbMnuItemMetal = new JRadioButtonMenuItem("Metal");
        
    private final JMenuItem mnuItemHelp = new JMenuItem("Help");
    
    private final JSeparator separator = new JSeparator();
    
    private final AdminGUI admin = new AdminGUI();
    private final UserGUI user = new UserGUI();
    
    private Socket clientSocket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    
    private static Client clientInstance; 
    
    public Client() {
        super("Animalia");
        Client.this.setSize(500, 500);
        Client.this.setLocationRelativeTo(null);
        Client.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        clientInstance = this;
        
        Client.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closing();
            }
        });
        
        JMenuBar mbMenuBar = new JMenuBar();
        Client.this.setJMenuBar(mbMenuBar);
                                
        mnuItemClose.addActionListener(Client.this);
        mnuItemLogout.addActionListener(Client.this);
          
        mnuFile.add(mnuItemClose);
        
        JMenu mnuUsers = new JMenu("Users");
        mnuItemGeneralUser.addActionListener(Client.this);
        mnuUsers.add(mnuItemGeneralUser);
        mnuItemAdminGUI.addActionListener(Client.this);
        mnuUsers.add(mnuItemAdminGUI);
        
        ButtonGroup bgMenuItems = new ButtonGroup();
        
        rbMnuItemWindows.addActionListener(Client.this);
        rbMnuItemMotif.addActionListener(Client.this);
        rbMnuItemMetal.addActionListener(Client.this);
        
        bgMenuItems.add(rbMnuItemWindows);
        bgMenuItems.add(rbMnuItemMotif);
        bgMenuItems.add(rbMnuItemMetal);
        
        JMenu mnuLookAndFeel = new JMenu("Look and Feel");
        mnuLookAndFeel.add(rbMnuItemWindows);
        mnuLookAndFeel.add(separator);
        mnuLookAndFeel.add(rbMnuItemMotif);
        mnuLookAndFeel.add(separator);
        mnuLookAndFeel.add(rbMnuItemMetal);
               
        JMenu mnuHelp = new JMenu("Help");
        mnuItemHelp.addActionListener(Client.this);
        mnuHelp.add(mnuItemHelp);
        
        mbMenuBar.add(mnuFile);
        mbMenuBar.add(mnuUsers);
        mbMenuBar.add(mnuLookAndFeel);
        mbMenuBar.add(mnuHelp);
        
        jpMainPanel.setBackground(new Color(255, 255, 255));
        Client.this.add(jpMainPanel);
    }
    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Client client = new Client();
        client.setVisible(true);
        
        try {
            client.clientSocket = new Socket("127.0.0.1", 7777);
            
            client.out = new ObjectOutputStream(
                    client.clientSocket.getOutputStream());
            client.in = new ObjectInputStream(
                    client.clientSocket.getInputStream());
            
            JOptionPane.showMessageDialog(client, client.in.readObject(),
                    "SERVER_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    
        } catch(UnknownHostException e) {
            System.err.println("Don't know about host: 127.0.0.1");
            System.exit(1);
        
        } catch(IOException e) {
            JOptionPane.showMessageDialog(client,
                    "Couldn't get I/O for the connection to: 127.0.0.1",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
    }
    
    public static Client currentInstance() {
        return clientInstance;
    }
    
    public String[] contactServer(String[] arr) {
        try {
            
            out.writeObject(arr);
            
            String[] fromServer;
            if((fromServer = (String[])in.readObject()) != null) {
                    
                return fromServer;
            }
            
        } catch(HeadlessException | IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(mnuItemLogout)) {
            
            jpMainPanel.remove(admin);
            mnuFile.remove(separator);
            mnuFile.remove(mnuItemLogout);
            
            Client.this.setSize(500, 500);
            this.setLocationRelativeTo(null);
            SwingUtilities.updateComponentTreeUI(this);
            
        } else if(e.getSource().equals(mnuItemClose)) {
            closing();
            
        } else if(e.getSource().equals(mnuItemGeneralUser)) {
            if(admin.isShowing() ||
                    mnuFile.isMenuComponent(mnuItemLogout)) {
                jpMainPanel.remove(admin);
                mnuFile.remove(separator);
                mnuFile.remove(mnuItemLogout);
            }
            jpMainPanel.add(user);
            
            this.pack();
            this.setLocationRelativeTo(null);
            SwingUtilities.updateComponentTreeUI(this);
            
        } else if(e.getSource().equals(mnuItemAdminGUI)) {
            if(mnuFile.isMenuComponent(mnuItemLogout)) {
                jpMainPanel.add(admin);
                Client.this.pack();
                SwingUtilities.updateComponentTreeUI(this);
            } else {
                new Login();
            }
            
        } else if(e.getSource().equals(mnuItemHelp)) {
            JDialog dialogAbout = new JDialog(Client.this, "Help");
            JTextArea taAboutArea = new JTextArea();
            taAboutArea.setWrapStyleWord(true);
            taAboutArea.setEditable(false);
            String strAbout = "This application serves as an educational platform\n"
                    + "where a user can learn about different animals and\n"
                    + "the species they are classified under in the kingdom animalia.\n"
                    + "The system serves as a scientific tool with wealth of information\n"
                    + "that users can use for research."
                    + "\nDeveloped By: Sanele Manyela 2021";
            taAboutArea.setText(strAbout);
            dialogAbout.add(taAboutArea);
            dialogAbout.pack();
            dialogAbout.setLocationRelativeTo(null);
            dialogAbout.setVisible(true);
            
        } else if(e.getSource().equals(rbMnuItemWindows)) {
            try {
                UIManager.setLookAndFeel(
                       ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
                SwingUtilities.updateComponentTreeUI(Client.this);
                
            } catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if(e.getSource().equals(rbMnuItemMotif)) {
            try {
                UIManager.setLookAndFeel(
                       ("com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
                SwingUtilities.updateComponentTreeUI(Client.this);
                
            } catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if(e.getSource().equals(rbMnuItemMetal)) {
            try {
                UIManager.setLookAndFeel(
                       ("javax.swing.plaf.metal.MetalLookAndFeel"));
                SwingUtilities.updateComponentTreeUI(Client.this);
                
            } catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }
    
    private void closing() {
        String[] arrOptions = {"Minimise", "Exit", "Cancel"};
        int intResponse = JOptionPane.showOptionDialog(null,
                "Select Action", "User Actions", 0,
                    JOptionPane.QUESTION_MESSAGE, null, arrOptions,
                        arrOptions[0]);
        
        switch (intResponse) {
            case 0:
                Client.this.setState(ICONIFIED);
                break;
            case 1:
                try {
                    out.close();
                    in.close();
                    clientSocket.close();
                    
                } catch(IOException io) {
                    JOptionPane.showMessageDialog(this, io.getMessage(),
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
                break;
            case 2:
                // Do nothing
                break;
            default:
                break;
        }
    }
    
    private class Login extends JDialog {
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        
        public Login() {
            super(Client.this, "Login");
            Login.this.setLayout(new BorderLayout(10, 0));
            Login.this.setSize(250, 200);
            Login.this.setLocationRelativeTo(null);
            
            JPanel jpDialogPanel = new JPanel(new BorderLayout(0, 20));
            jpDialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JPanel jpInputPanel = new JPanel(new GridLayout(2, 1, 0, 10));
            jpInputPanel.add(mCreateLine("Username", txtUsername));
            jpInputPanel.add(mCreateLine("Password", txtPassword));
            
            
            btnLogin.addActionListener(this::login);
            
            jpDialogPanel.add(setPanelBackground(jpInputPanel), BorderLayout.NORTH);
            jpDialogPanel.add(btnLogin);
            Login.this.add(setPanelBackground(jpDialogPanel));
            
            Login.this.setVisible(true);
        }
        
        private JPanel mCreateLine(String str, JTextField txt) {

            JPanel jpPanel = new JPanel(new BorderLayout());

            jpPanel.setBackground(new Color(255, 255, 255));

            JLabel lblLabel = new JLabel(str);

            lblLabel.setPreferredSize(new Dimension(90, 22));

            jpPanel.add(lblLabel, BorderLayout.WEST);

            jpPanel.add(txt);

            return jpPanel;

        }
        
        private JPanel setPanelBackground(JPanel pnl) {
            pnl.setBackground(new Color(255, 255, 255));
            return pnl;
        }
        
        private void login(ActionEvent e) {
            String[] fromServer;
            String strPassword = "";
            for(char ch : txtPassword.getPassword()) {
                strPassword += ch;
            }
            
            fromServer = contactServer(new String[]{"SELECT Username, Password FROM Users WHERE Username ='"+
                    txtUsername.getText().trim() + "' AND Password ='"+
                    strPassword.trim()+"'", "Check"});
            
            if(fromServer.length != 0) {
                
                if(fromServer[0].equals("true")) {
                    
                    mnuFile.add(separator);
                    mnuFile.add(mnuItemLogout);
                    
                    // if logged in
                    if(user.isShowing()) {
                        jpMainPanel.remove(user);
                    }
                    jpMainPanel.add(admin);
                    Client.this.setLocationRelativeTo(null);
                    Client.this.pack();
                    SwingUtilities.updateComponentTreeUI(Client.this);
                    Login.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login Attempt Failed!", "WARNING",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}