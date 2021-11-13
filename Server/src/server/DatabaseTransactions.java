/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.*;
import java.sql.*;

/**
 *
 * @author Sanele
 */
public class DatabaseTransactions {
    public Connection databaseConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Animalia;user=sa;password=password");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
         
    public String[] rowData(String strQuery) {
        String[] arrData = null;
        Connection conConnection = databaseConnection();
        
        try(Statement stStatement = conConnection.createStatement(); 
                ResultSet rs = stStatement.executeQuery(strQuery)) {
            ResultSetMetaData rsmt = rs.getMetaData();
            arrData = new String[rsmt.getColumnCount()];
            
            while(rs.next()) {
                for(int i = 0; i < arrData.length; i++) {
                    arrData[i] = rs.getString(i + 1);
                }
            }
            for(String str: arrData) {
                System.out.println(str);
            }
            conConnection.close();
            stStatement.close();
            rs.close();
            
        } catch(SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
            
        } finally {
            try{ 
                conConnection.close();
            } catch(SQLException | NullPointerException e) {
            }
        }
        return arrData;
    }
    
    public boolean checkData(String strQuery) {
        Connection conConnection = databaseConnection();
        boolean isExisting = false;
        
        try(Statement stStatement = conConnection.createStatement()) {
            
            
            isExisting = stStatement.executeQuery(strQuery).next();
            
            stStatement.close();
            conConnection.close();
            
            return isExisting;
        } catch(SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        
        } finally {
            try{
                conConnection.close();
            } catch(SQLException | NullPointerException e) {
            }
        }
        return isExisting;
    }
    
    public boolean executeQueries(String strQuery) {
        Connection conConnection = databaseConnection();
        boolean isExecuted = false;
        
        try(Statement stStatement = conConnection.createStatement()) {
            
            
            stStatement.executeUpdate(strQuery);          
            isExecuted = true;
  
            stStatement.close();
            conConnection.close();
            
            return isExecuted;
        } catch(SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        
        } finally {
            try{
                conConnection.close();
            } catch(SQLException | NullPointerException e) {
            }
        }
        return isExecuted;
    }
    
    public String[] getField(String strQuery) {
        Connection conConnection = databaseConnection();
        String[] arrField = null;
        List<String> values = new ArrayList<>();
        
        try(Statement stStatement = conConnection.createStatement();
                ResultSet rs = stStatement.executeQuery(strQuery)) {
                while(rs.next()) {
                    values.add(rs.getString(1));
                }
                arrField = values.toArray(new String[values.size()]);
                
        } catch(SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        
        } finally {
            try{
                conConnection.close();
            } catch(SQLException | NullPointerException e) {
            }
        }
        return arrField;
    }
}
