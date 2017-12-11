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
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



/**
 *
 * @author Kevin Sanchez
 */
public class Project {
    MySQL mysql;
    public Project(){
        mysql = new MySQL();
    }
    public static void main(String[] args) throws SQLException, IOException {
        Project p = new Project();
        p.mysql.createConnection();
        System.out.println("distancia: "+Project.distanciaCoord(32.02602, 34.778409,31.80037, 34.778239));
        p.mysql.closeConnection();
        System.out.println(p.tripDistance(32.02602,34.778409,31.80037,34.778239,"9:02"));
       /* p.mysql.createConnection();
        p.mysql.updateData("countries","8","Alemania", 0);
        p.mysql.closeConnection();*/
    }
  
    public double tripDistance(double lat1,double lng1,double lat2,double lng2,String eta) throws MalformedURLException, IOException{
        long sec_eta = seconds_eta(eta);
        System.out.println("eta en segundos: "+sec_eta);
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
            cont++;
            if(cont==10){
                String []string = linea.split(":");
                String []string2= string[1].split(" ");
                return Double.parseDouble(string2[1]);
            }
            
        }
        return 0;
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
    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {   
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
    
}
