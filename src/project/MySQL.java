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
 * Class for the management of the database
 * @author Kevin Sanchez
 */
public class MySQL {
    Connection connection; 
    /**
     * Start connection with the database
     */
    public void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/maccabiabusproject", "root", "");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Finalize connection with the database
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Check if the column exists if it is not like that.
     * @param table_name table name
     * @param column column to check
     * @param type type 
     */
    public void check_column(String table_name,String column,String type){
        ResultSet rs = getValues(table_name);
        try {
            rs.next();
            rs.getDouble(column);
           
        } catch (SQLException ex) {
            if(ex.getLocalizedMessage().equals("Column '"+column+"' not found.")){
               addColumn(table_name, column, type);
            }
        }
        
    }

    /**
     * Add column to specific table
     * @param table_name table name
     * @param new_column new column to add
     * @param type type
     */
    public void addColumn(String table_name,String new_column,String type){
        type = type.toUpperCase();
         try {
            String Query = "ALTER TABLE "+table_name+" ADD "+new_column+" "+type+" NOT NULL"; 
            Statement st = connection.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error add column");
        }
    }

    /**
     * Update data of specific row the table passenger
     * @param pass_no pass_no
     * @param default_s default station
     */
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

    /**
     * Update data of specific row the table vehicles
     * @param id id
     * @param trip_cost trip_cost
     * @param seat_cost seat_cost
     * @param trip_duration trip_duration
     * @param trip_length trip_length
     * @param assigned assigned
     */
    public void updateDataVehicles(int id,double trip_cost,double seat_cost,double trip_duration,double trip_length,boolean assigned) {
        check_column("tblvehicleparameters","trip_cost","decimal(18,2)");
        check_column("tblvehicleparameters","seat_cost","decimal(18,2)");
        check_column("tblvehicleparameters","trip_duration","decimal(18,2)");
        check_column("tblvehicleparameters","trip_length","decimal(18,2)");
        check_column("tblvehicleparameters","assigned","boolean");
        try {
            String Query = "UPDATE tblvehicleparameters SET "
                    + "trip_cost=" + trip_cost + ", "
                    + "seat_cost=" + seat_cost + ", "
                    + "trip_duration=" + trip_duration + ", "
                    + "trip_length=" + trip_length + ", "
                    + "assigned="+assigned+" "
                    + " WHERE cateory=" + id; 
            Statement st = connection.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error in data update");
        }
    }

    /**
     * Update data of specific row the table vehicles
     * @param id id
     * @param assigned assigned
     */
    public void updateDataVehicles(int id,boolean assigned) {
        check_column("tblvehicleparameters","assigned","boolean");
        try {
            String Query = "UPDATE tblvehicleparameters SET "
                    + "assigned="+assigned+" "
                    + " WHERE cateory=" + id; 
            Statement st = connection.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error in data update");
        }
    }

    /**
     * Return values the table specific
     * @param table_name table name
     * @return values the table specific
     */
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

    /**
     * Delete row the specific table
     * @param table_name table name
     * @param ID id
     */
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
