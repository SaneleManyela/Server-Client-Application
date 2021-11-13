/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Sanele
 */
public class Server extends JFrame {
    private ServerSocket serverSocket;
    private Session session;
    
    public Server() {
        super("Server Application");
        Server.this.setSize(400, 300);
        JButton btnStartUp = new JButton("Start");
        JButton btnShutdown = new JButton("Shutdown");
        JPanel jpServerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        jpServerPanel.setBackground(Color.white);
        jpServerPanel.add(btnStartUp);
        jpServerPanel.add(btnShutdown);
        
        Server.this.setLocationRelativeTo(null);
        Server.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Server.this.add(jpServerPanel);
        Server.this.setVisible(true);
        
        btnStartUp.addActionListener(this::start);
        
        btnShutdown.addActionListener(this::stop);
    }
    
    private void start(ActionEvent e) {
        try {
            
            serverSocket = new ServerSocket(7777);
            serverSocket.setReuseAddress(true);
                    
            listener.execute();
            
            JOptionPane.showMessageDialog(Server.this, "Server started and running.",
                    "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(Server.this, ioe.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void stop(ActionEvent e) {
        try {
            if(session.socket != null && session.objInput != null && 
                    session.objOutput != null) {
                session.socket.close();
                session.objInput.close();
                session.objOutput.close();
            } 
            serverSocket.close();
            JOptionPane.showMessageDialog(Server.this, "Server shutdown.",
                    "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | NullPointerException ex) {
            JOptionPane.showMessageDialog(Server.this, ex.getMessage(), "ERROR",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    SwingWorker<Void, Void> listener = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            while(true) {
                session = new Session(serverSocket.accept());
                Thread thread = new Thread(session);
                thread.start();
            }
        }
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Login login = new Login();
    }
    
}

class Session implements Runnable {
    Socket socket;
    ObjectInputStream objInput;
    ObjectOutputStream objOutput;
    
    Session(Socket s) {
        socket = s;
        
        try{
            objInput = new ObjectInputStream(
                    socket.getInputStream()); // Causes this t
            objOutput = new ObjectOutputStream(
                    socket.getOutputStream());
            objOutput.writeObject("Welcome");
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {  
            String[] input;
            if(!socket.isClosed()) {
                
                while((input = (String[])objInput.readObject()) != null) {   
                    objOutput.writeObject(Protocol.processInput(input));
                }
            }    
        } catch(ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException ie) {
        }
 
        try{
            Thread.sleep(10);
        } catch (InterruptedException ie) {
        }
    }
}

class Protocol {
    public static String[] processInput(String[] arrInput) {
        DatabaseTransactions databaseTransactions = new DatabaseTransactions();
    
        String[] arrOutput = null;
        switch(arrInput[1]) {
            case "Check":
                arrOutput = new String[]{String.valueOf(databaseTransactions.checkData(arrInput[0]))};
                break;
                
            case "Execute":
                arrOutput = new String[]{String.valueOf(databaseTransactions.executeQueries(arrInput[0]))};
                break;
                
            case "Get Field":
                arrOutput = databaseTransactions.getField(arrInput[0]);
                break;
                
            case "Row":
                arrOutput = databaseTransactions.rowData(arrInput[0]);
                break;
        }
        return arrOutput;
    }
}

class Login extends JDialog {
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        
        DatabaseTransactions databaseTransactions = new DatabaseTransactions();
        
        public Login() {
            super(null, "Login", ModalityType.APPLICATION_MODAL);
            Login.this.setLayout(new BorderLayout(10, 0));
            Login.this.setSize(250, 200);
            Login.this.setLocationRelativeTo(null);
            
            Login.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            
            Login.this.addWindowListener( new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            
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
            String strPassword = "";
            
            for(char ch : txtPassword.getPassword()) {
                strPassword += ch;
            }
            if(databaseTransactions.checkData(
                    "SELECT Username, Password FROM Users WHERE Username ='"+
                            txtUsername.getText().trim() + "' AND Password ='"+
                            strPassword.trim()+"'")) {
                Login.this.dispose();
                new Server().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Login Attempt Failed!", "WARNING",
                        JOptionPane.WARNING_MESSAGE);
            }           
        }
    }