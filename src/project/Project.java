/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import sun.net.www.content.audio.wav;



/**
 *
 * @author Kevin Sanchez
 */
public class Project {
    MySQL mysql;
    int walking_distance;
    ArrayList<Destination> destinations;
    public Project(){
        mysql = new MySQL();
        walking_distance = 600;
        destinations = new ArrayList<>();
    }
    public static void main(String[] args) throws SQLException, IOException {
        Project p = new Project();
        p.probrar();
        //p.fillDestination();
       // p.defaultStationAssignment();
        
    }
    public void probrar(){
        Destination dest = new Destination("30,40");
        dest.addBusStop(new BusStop(30, 40, 10, null));
        dest.addBusStop(new BusStop(30, 40, 15, null));
        dest.addBusStop(new BusStop(30, 40, 4, null));
        dest.addBusStop(new BusStop(30, 40, 3, null));
        dest.addBusStop(new BusStop(30, 40, 4, null));
        dest.startMatrixDistance();
        dest.addDistance(0,1,2);
        dest.addDistance(0,2,5);
        dest.addDistance(0,3,6);
        dest.addDistance(0,4,1);
        dest.addDistance(1,2,3);
        dest.addDistance(1,3,1);
        dest.addDistance(1,4,5);
        dest.addDistance(2,3,8);
        dest.addDistance(2,4,7);
        dest.addDistance(3,4,3);
        dest.matriz();
        dest.optimalWay();
    }
    public void fillDestination() throws SQLException{
        mysql.createConnection();
        ResultSet rs_pass = mysql.getValues("passengers");
        while(rs_pass.next()){
            Destination dest = new Destination(rs_pass.getString("destination_coordinates"));
            if(destinations.isEmpty() || !dest.containedInArray(destinations)){
                destinations.add(dest);
            }
        }
        mysql.closeConnection();
    }
    public void addBusStop(double dest_lat,double dest_lng,double buss_lat,double buss_lng,double trip_distance, int pass_no){
        int i = Destination.indexArray(destinations, dest_lat, dest_lng);
        if(destinations.get(i).busStopsIsEmpty() || !destinations.get(i).containedInArrayBusStops(buss_lat, buss_lng)){
            destinations.get(i).addBusStop(new BusStop(buss_lat, buss_lng, trip_distance, new Passenger(pass_no)));
        }
    }
    public  double distanceCoord(double lat1,double lng1, double lat2, double lng2) {
        double radioEarth = 6371000;//en km  
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));  
        double distance = radioEarth * va2;  
        return distance;  
    } 
    public void defaultStationAssignment() throws SQLException, IOException{
        mysql.createConnection();
        ResultSet rs_pass = mysql.getValues("passengers");
        ResultSet rs_bs = mysql.getValues("busstop");
        while (rs_pass.next()) {
            int id = rs_pass.getInt("pass_no");
            String default_s = "";
            double distance_ds = 0;
            double distance;
            System.out.println("id: " + id);
            String []string = rs_pass.getString("address_coordinates").split(",");
            double lat1 = Double.parseDouble(string[0]);
            double lng1 = Double.parseDouble(string[1]);
            String []string2 = rs_pass.getString("destination_coordinates").split(",");
            double lat2 = Double.parseDouble(string2[0]);
            double lng2 = Double.parseDouble(string2[1]);
            while (rs_bs.next()) {
                if ((distance = distanceCoord(lat1, lng1, rs_bs.getDouble("lat"), rs_bs.getDouble("lng"))) <= walking_distance) {
                    if ((default_s.isEmpty()) || (walking_distance - distance) < (walking_distance - distance_ds)) {
                        distance_ds = distance;
                        default_s = rs_bs.getDouble("lat")+","+rs_bs.getDouble("lng");
                    }
                }
            }
            //System.out.println("default: "+default_s);
            if(!rs_pass.getString("default_station").equals(default_s)){
                mysql.updateDataPassengers(id, default_s);
            }
            rs_bs.beforeFirst();
            default_s = rs_pass.getString("default_station");
            if(!default_s.isEmpty()){
                String[] string3 = default_s.split(",");
                double lat3 = Double.parseDouble(string3[0]);
                double lng3 = Double.parseDouble(string3[1]);
                double trip_distance = tripDistance(lat3, lng3, lat2, lng2, rs_pass.getString("eta"));
                System.out.println("trip distance: " + trip_distance);
                addBusStop(lat2, lng2, lat3, lng3, trip_distance, id);
            }
            
        }
        mysql.closeConnection();
        for (Destination d : destinations) {
            System.out.println("Destination: "+d.getCoor());
            for (BusStop b : d.getBus_stops()) {
                System.out.println("Bus Stop: "+b.getCoor());
            }
        }
            
    }
    public long seconds_eta(String eta){
        String []string = eta.split(":");
        return Long.parseLong(string[0])*3600+Long.parseLong(string[1])*60;
    }
    public long secondsSinceJanuary_1_1970(long eta){
        final long MILLSECS_PER_SECONDS =1000*60*60*24;
        java.util.Date today = new Date(); 
        int year = 1970;
        int month = 1;
        int day = 1; //Fecha anterior 
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        java.sql.Date date = new java.sql.Date(calendar.getTimeInMillis());

        long diferencia = (today.getTime() - date.getTime()) / MILLSECS_PER_SECONDS;
        return (diferencia*24*60*60) + eta;
    }
    public double tripDistance(double lat1,double lng1,double lat2,double lng2,String eta) throws MalformedURLException, IOException{
        long sec_eta = seconds_eta(eta);
        long sec_date = secondsSinceJanuary_1_1970(sec_eta);
        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+lat1+","+lng1+"&destinations="+lat2+","+lng2+"&arrival_time="+sec_date+"&key=AIzaSyDebkks63KOCCwxOYZ5QLsrKxi-qksFkg0");
        URLConnection con = url.openConnection();
        Authenticator au = new Authenticator() {
            @Override
            protected PasswordAuthentication
                    getPasswordAuthentication() {
                return new PasswordAuthentication("usuario", "clave".toCharArray());
            }
        };
        Authenticator.setDefault(au);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String linea;
        int cont=0;
        while ((linea = in.readLine()) != null) {
            if(linea.contains("value")){
                String []string = linea.split(":");
                String []string2= string[1].split(" ");
                return Double.parseDouble(string2[1]);
            }
            
        }
        return 0;
    }
    
}
