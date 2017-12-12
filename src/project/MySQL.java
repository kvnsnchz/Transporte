/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Sanchez
 */
public class MySQL {
    Connection connection;
    public MySQL() {

    }

    void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/maccabiabusproject", "root", "");
            System.out.println("Database Connection Success");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
            System.out.println("The connection to the server has been terminated");
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertDataPassengers(int pass_no, String first_name, String family, String city, String street,int number,String a_coor,String destination,String d_coor,String d_station,String eta,String etd) {
        try {
            String Query = "INSERT INTO passengers VALUES("
                    + "\"" + pass_no + "\", "
                    + "\"" + first_name + "\", "
                    + "\"" + family + "\", "
                    + "\"" + city + "\", "
                    + "\"" + street + "\", "
                    + "\"" + number + "\", "
                    + "\"" + a_coor + "\", "
                    + "\"" + destination + "\", "
                    + "\"" + d_coor + "\", "
                    + "\"" + d_station + "\", "
                    + "\"" + eta + "\", "
                    + "\"" + etd + "\")";
                    
            Statement st = connection.createStatement();
            st.executeUpdate(Query);
            System.out.println("Data stored successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error in data storage");
        }
    }
    public void updateDataPassengers(int pass_no,String default_s) {
        try {
            String Query = "UPDATE passengers SET default_station=\"" + default_s + "\" WHERE pass_no=" + pass_no; 
            Statement st = connection.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error in data update");
        }
    }
    public ResultSet getValues(String table_name) {
        try {
            String Query = "SELECT * FROM " + table_name;
            Statement st = connection.createStatement();
            return st.executeQuery(Query);

        } catch (SQLException ex) {
            System.out.println("Error in data acquisition");
        }
        return null;
    }

    public void deleteRecord(String table_name, String ID) {
        try {
            String Query = "DELETE FROM " + table_name + " WHERE ID = \"" + ID + "\"";
            Statement st = connection.createStatement();
            st.executeUpdate(Query);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error erasing the specified record");
        }
    }
}
