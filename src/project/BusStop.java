/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;

/**
 *
 * @author Kevin Sanchez
 */
public class BusStop {
    private double lat;
    private double lng;
    private double trip_distance;
    private boolean marked;
    private ArrayList<Passenger> passengers;
    public BusStop(double lat, double lng,double trip_distance,Passenger pass){
        this.lat = lat;
        this.lng = lng;
        this.trip_distance = trip_distance;
        passengers = new ArrayList<>();
        passengers.add(pass);
        marked = false;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    
    public String getCoor() {
        return lat+","+lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    
    public double getTrip_distance() {
        return trip_distance;
    }
    
    public void setTrip_distance(double trip_distance) {
        this.trip_distance = trip_distance;
    }
    public void addPassenger(Passenger pass){
        passengers.add(pass);
    }
    public static boolean containedinArray(ArrayList<BusStop> bus_stops,double lat,double lng){
        for (BusStop b : bus_stops) {
            if(b.getLat()==lat && b.getLng()==lng){
                return true;
            }
        }
        return false;
    }
}
