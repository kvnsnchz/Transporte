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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Class main
 * @author Kevin Sanchez
 */
public class Project {
    MySQL mysql;
    double walking_distance;
    double max_time_to_dest;
    ArrayList<Vehicles> vehicles;
    ArrayList<Destination> destinations;

    /**
     * Default Contructor 
     */
    public Project(){
        mysql = new MySQL();
        walking_distance = 600;
        max_time_to_dest = 5400;
        destinations = new ArrayList<>();
        vehicles = new ArrayList<>();
    }
    public static void main(String[] args){
        Project p = new Project();
        p.start();
    }

    /**
     * start project
     */
    public void start(){
        try {
            fillDestination();
            defaultStationAssignment();
            searchWayToDestination();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }
    /**
     * print separators
     */
    public void separators(){
        System.out.println("");
        System.out.println("--------------------------------------------------------");
        System.out.println("");
    }

    /**
     * search the optimal ways of the destination
     * @throws IOException
     */
    public void searchWayToDestination() throws IOException, SQLException{
        vehicles();
        separators();
        System.out.println("Number of destinations: "+destinations.size());
        int cont = 0;
        for (Destination d : destinations) {
            d.startMatrixDistance();
            d.setTorta(d.addBusStopsToTorta(d.getTorta(0)),0);
            separators();
            System.out.println("Destination: "+d.getCoor()+" Bus stop: "+d.getBusStops().size());
            System.out.println("");
            System.out.println("Walking distance with other destinations: ");
            System.out.println("");
            for (int i = 0; i < destinations.size(); i++) {
                if(i != cont){
                    double dist = distanceCoord(d.getLat(), d.getLng(), destinations.get(i).getLat(), destinations.get(i).getLng());
                    System.out.println("distance: "+dist+" m with destination: "+destinations.get(i).getCoor());
                    System.out.println("");
                }
                
            }
            cont++;
            
            for (int i = 0; i < d.getBusStops().size(); i++) {
                for (int j = i+1; j < d.getBusStops().size(); j++) {
                    //double t = tripDistance(d.getLatBusStop(i), d.getLngBusStop(i), d.getLatBusStop(j), d.getLngBusStop(j));
                    double t = distanceCoord(d.getLatBusStop(i), d.getLngBusStop(i), d.getLatBusStop(j), d.getLngBusStop(j));
                    d.addDistance(i, j,t);
                }
            }
        }
        vehicles();
        assignedWays();
        imprimir();
    }
    public void vehicles() throws SQLException{
        mysql.createConnection();
        ResultSet rs = mysql.getValues("tblvehicleparameters");
        while(rs.next()){
            vehicles.add(new Vehicles(rs.getInt("NumberOfSeatMax")));
        }
        mysql.closeConnection();
    }
    public boolean revisarVehicle(int size_passengers){
        for (Vehicles vehicle : vehicles) {
            if(!vehicle.isAssigned()){
                if(vehicle.getNum_seat() >= size_passengers){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean assignedAllVehicle(){
        for (Vehicles vehicle : vehicles) {
            if(!vehicle.isAssigned()){
                return false;
            }
        }
        return true;
    }
    public void assignedWays() throws IOException, SQLException {
        for (Destination d : destinations) {
            System.out.println("Destino: "+d.getCoor());
            mysql.createConnection();
            ResultSet rs_1 = mysql.getValues("tblvehicleparameters");
            while (rs_1.next()) {
                mysql.updateDataVehicles(rs_1.getInt("cateory"), false);
            }
            mysql.closeConnection();
            for (Vehicles vehicle : vehicles) {
                vehicle.setAssigned(false);
            }
            boolean repetir;
            do {
                d.partir();
                d.restartPartir();
                repetir = false;
                if(d.sizeTorta() > vehicles.size() || assignedAllVehicle()){
                    d.setWithout_capacity(true);
                }
                else{
                    for (int i = 0; i < d.getTorta().size(); i++) {
                        if (!d.getTorta(i).isFine() && !d.getTorta(i).isEliminado() && !d.getTorta(i).getBus_stops().isEmpty()) {
                            d.optimalWay(i); 
                            d.way(i);
                            calculateValues(d.getTorta(i), d);
                            if (!revisarVehicle(d.getTorta(i).getSize_passengers()) || d.getTorta(i).getTime_min()> max_time_to_dest) {
                                d.addPartir(i);
                                repetir = true;
                            } else {
                                System.out.println("calculated");
                                d.getTorta().get(i).setFine(true);
                                assignVehicles(d, i);
                            }
                        }

                    }
                }
                
            } while (repetir);
            
        }
        
    }
    public void imprimir(){
        separators();
        System.out.println("Result: ");
        separators();
        for (Destination d : destinations) {
            separators();
            System.out.println("Destination: "+d.getCoor());
            separators();
            if(d.isWithout_capacity()){
                System.out.println("There are not enough vehicles or with the necessary capacity to cover this destination");
            }
            else{
                int cont = 1;
                for (int i = 0; i < d.getTorta().size(); i++) {
                    if(!d.getTorta(i).isEliminado() && !d.getTorta(i).getBus_stops().isEmpty()){
                        System.out.println("");
                        System.out.println("Ruta " + (cont++) + " Assigned Vehicle: " + d.getTorta(i).getVehicle() + " Bus Stop: " + d.getTorta(i).getBus_stops().size()+" Passengers: "+d.getTorta(i).getSize_passengers());
                        System.out.println("");
                        System.out.println("Trip cost: " + d.getTorta(i).getMinimal_price() + " Trip Duration: " + (d.getTorta(i).getTime_min()/60)+" min");
                        System.out.println("");
                        d.imprimirWay(i);
                    }
                    
                }
                separators();
                System.out.println("Buses chosen to cover the route: "+(cont-1));
            }
        }
        separators();
        System.out.println("Bus assigned to each passenger: ");
        separators();
        for (Destination d : destinations) {
           
            if (!d.isWithout_capacity()) {
                 System.out.println("Destination: " + d.getCoor());
                for (Torta t : d.getTorta()) {
                    if (!t.isEliminado() && !t.getBus_stops().isEmpty()) {
                        for (BusStop b : t.getBus_stops()) {
                            for (Passenger p : b.getPassengers()) {
                                System.out.println("Passenger: " + p.getPass_no() + " Bus: " + t.getVehicle());
                            }
                        }
                    }

                }

            }
        }
    }
    public void calculateValues(Torta t, Destination d) throws IOException {
        for (int i = 0; i < t.getWay().size() - 1; i++) {
            double[] values;
            if (t.getWay(i) == -1) {
                values = values(d.getLat(), d.getLng(), d.getLatBusStop(t.getWay(i + 1)), d.getLngBusStop(t.getWay(i + 1)));
            } else {
                if (t.getWay(i + 1) == -1) {
                    values = values(d.getLatBusStop(t.getWay(i)), d.getLngBusStop(t.getWay(i)), d.getLat(), d.getLng());
                } else {
                    values = values(d.getLatBusStop(t.getWay(i)), d.getLngBusStop(t.getWay(i)), d.getLatBusStop(t.getWay(i + 1)), d.getLngBusStop(t.getWay(i + 1)));
                }
            }
            t.addDistance_min(values[0]);
            t.addTime_min(values[1]);
        }

    }

    public void assignVehicles(Destination d,int j) throws SQLException{
        mysql.createConnection();
        ResultSet rs = mysql.getValues("tblvehicleparameters");
        ArrayList<Boolean> assigned = new ArrayList<>();
        while(rs.next()){
            assigned.add(rs.getBoolean("assigned"));
        }
        rs.beforeFirst();
        int i = 0;
        while (rs.next()) {
            if (rs.getInt("NumberOfSeatMax") >= d.getTorta(j).getSize_passengers() && !assigned.get(i)) {
                d.calculatePrice(rs.getInt("cateory"), rs.getDouble("BasicCost"), rs.getDouble("EveryAdditionalKm"), rs.getDouble("EveryAdditional30min"),j);
            }
            i++;
        }
        rs.beforeFirst();
        i = 0;
        while (rs.next()) {
            if (rs.getInt("cateory") == d.getTorta(j).getVehicle()) {
                mysql.updateDataVehicles(d.getTorta(j).getVehicle(), d.getTorta(j).getMinimal_price(), d.getTorta(j).getMinimal_price() / rs.getInt("NumberOfSeatMax"), d.getTorta(j).getTime_min(), d.getTorta(j).getDistance_min(), true);
                assigned.set(i, true);
            }
            i++;
        }
        mysql.closeConnection();
    }
    /**
     * Assign vehicles to the optimal way the destination
     * @throws SQLException
     */
    public void assignVehicles() throws SQLException{
        mysql.createConnection();
        ResultSet rs_1 = mysql.getValues("tblvehicleparameters");
        while(rs_1.next()){
            mysql.updateDataVehicles(rs_1.getInt("cateory"), false);
        }
        mysql.closeConnection();
        mysql.createConnection();
        ResultSet rs = mysql.getValues("tblvehicleparameters");
        separators();
        System.out.println("Assign Vehicles to Destination");
        ArrayList<Boolean> assigned = new ArrayList<>();
        while(rs.next()){
            assigned.add(rs.getBoolean("assigned"));
        }
        for (Destination d : destinations) {
            separators();
            System.out.println("Destination: "+d.getCoor()+" Bus Stops: "+d.getBusStops().size()+" Passengers: "+d.getSize_passengers());
            if(d.getTime() > max_time_to_dest){
                System.out.println("The destination exceeds the maximum trip time");
            }
            else{
                boolean without_capacity = true;
                int i = 0;
                while (rs.next()) {
                    if (rs.getInt("NumberOfSeatMax") >= d.getSize_passengers() && !assigned.get(i)) {
                        without_capacity = false;
                        d.calculatePrice(rs.getInt("cateory"), rs.getDouble("BasicCost"), rs.getDouble("EveryAdditionalKm"), rs.getDouble("EveryAdditional30min"));
                    }
                    i++;
                }
                rs.beforeFirst();
                if (without_capacity) {
                    System.out.println("there are no vehicles with this seating capacity");
                }
                else{
                    i = 0;
                   while(rs.next()){
                       if(rs.getInt("cateory") == d.getVehicle()){
                           mysql.updateDataVehicles(d.getVehicle(), d.getMinimal_price(), d.getMinimal_price()/rs.getInt("NumberOfSeatMax"), d.getTime(), d.getDistance(),true);
                           System.out.println("Assigned vehicle: "+d.getVehicle()+" trip cost: "+rs.getDouble("trip_cost")+" seat cost: "+rs.getDouble("seat_cost")+" trip duration: "+rs.getDouble("trip_duration")+" trip length:"+rs.getDouble("trip_length"));
                           assigned.set(i, true);
                       }
                       i++;
                   }
                   rs.beforeFirst();
                }
            }
        }
        mysql.closeConnection();
    }
    
    /**
     * Fill destination in the array destinations
     * @throws SQLException
     */
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

    /**
     * Add bus stop to the specific destination, if the bus stop is already added then add passengers
     * @param dest_lat latitude the destination
     * @param dest_lng longitude the destination
     * @param buss_lat latitude the bus stop
     * @param buss_lng longitude the bus stop
     * @param trip_distance trip distance from bus stop to destination
     * @param pass_no pass_no
     */
    public void addBusStop(double dest_lat,double dest_lng,double buss_lat,double buss_lng,double trip_distance, int pass_no){
        int i = Destination.indexArray(destinations, dest_lat, dest_lng);
        int j = destinations.get(i).containedInArrayBusStops(buss_lat, buss_lng);
        if(destinations.get(i).busStopsIsEmpty() || j==-1){
            destinations.get(i).addBusStop(new BusStop(buss_lat, buss_lng, destinations.get(i).getLat(), destinations.get(i).getLng(), trip_distance, new Passenger(pass_no)));
        }
        else{
            destinations.get(i).addPassenger(j,new Passenger(pass_no));
        }
    }

    /**
     * Return the distance between the two coordinates
     * @param lat1 latitude the coordinate 1
     * @param lng1 longitude the coordinate 1
     * @param lat2 latitude the coordinate 2
     * @param lng2 longitude the coordinate 2
     * @return distance between the two coordinates
     */
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

    /**
     * Default station assignment to all the passengers
     * @throws SQLException
     * @throws IOException
     */
    public void defaultStationAssignment() throws SQLException, IOException{
        separators();
        System.out.println("Default Station Assignment");
        separators();
        mysql.createConnection();
        ResultSet rs_pass = mysql.getValues("passengers");
        ResultSet rs_bs = mysql.getValues("busstop");
        while (rs_pass.next()) {
            int id = rs_pass.getInt("pass_no");
            String default_s = "";
            double distance_ds = 0;
            double distance;
            System.out.println("pass no.: " + id);
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
                System.out.println("Default Station: "+default_s);
                String[] string3 = default_s.split(",");
                double lat3 = Double.parseDouble(string3[0]);
                double lng3 = Double.parseDouble(string3[1]);
                //double trip_distance = 0;
                double trip_distance = tripDistance(lat3, lng3, lat2, lng2, rs_pass.getString("eta"));
                System.out.println("trip distance: " + trip_distance);
                addBusStop(lat2, lng2, lat3, lng3, trip_distance, id);
            }
            else{
                System.out.println("Default Station: there is not a bus stop that meets the requirements");
                System.out.println("trip distance: none");
            }
            
        }
        mysql.closeConnection();
            
    }

    /**
     * Return ETA in seconds
     * @param eta ETA
     * @return distance between the two coordinates
     */
    public long seconds_eta(String eta){
        String []string = eta.split(":");
        return Long.parseLong(string[0])*3600+Long.parseLong(string[1])*60;
    }

    /**
     * Return the seconds from January 1 of 1970 to ETA
     * @param eta ETA in seconds
     * @return the seconds from January 1 of 1970 to ETA
     */
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

    /**
     * Calculate the trip distance between two coordinates according to ETA using the Google map API 
     * if the values give zero, it is because the free quota of the key was exceeded
     * @param lat1 latitude the coordinate 1
     * @param lng1 longitude the coordinate 1
     * @param lat2 latitude the coordinate 2
     * @param lng2 longitude the coordinate 2
     * @param eta ETA as a String
     * @return trip distance between two coordinates according to ETA 
     * @throws MalformedURLException
     * @throws IOException
     */
    public double tripDistance(double lat1,double lng1,double lat2,double lng2,String eta) throws MalformedURLException, IOException{
        long sec_eta = seconds_eta(eta);
        long sec_date = secondsSinceJanuary_1_1970(sec_eta);
        String key = "AIzaSyDNtMV5Da2yIa_8vgrzf39idtk4Ub4oWGE";
       // String key = "AIzaSyAnlqk06SoqZKMIZV-t3aGVdy__YayqhQ4";
        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+lat1+","+lng1+"&destinations="+lat2+","+lng2+"&arrival_time="+sec_date+"&key="+key);
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

    /**
     * Return the distance and duration the trip between two coordinates using the Google map API
     * if the values give zero, it is because the free quota of the key was exceeded
     * @param lat1 latitude the coordinate 1
     * @param lng1 longitude the coordinate 1
     * @param lat2 latitude the coordinate 2
     * @param lng2 longitude the coordinate 2
     * @return distance and duration the trip between two coordinates
     * @throws MalformedURLException
     * @throws IOException
     */
    public double[] values(double lat1,double lng1,double lat2,double lng2) throws MalformedURLException, IOException{
        double []values= new double[2];
        String key = "AIzaSyAPFuHKFzWSEOiWFbf7AT51bjaZeNUEluo";
        //String key = "AIzaSyB_kQOjIY37xkb5MUaFQZ-Cu9rtz50_0fQ";
        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+lat1+","+lng1+"&destinations="+lat2+","+lng2+"&key="+key);  
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
                values[cont] = Double.parseDouble(string2[1]);
                cont++;
                if(cont == 2){
                    return values;
                }
            }
            
        }
        values[0] = 0;
        values[1] = 0;
        return values;
    }
 
    
}
