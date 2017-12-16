/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;

/**
 *  Class of bus stops, it contains the passengers assigned to that stop that go to the same destination.
 * @author Kevin Sanchez
 */
public class BusStop {
    private double lat;
    private double lng;
    private double trip_distance;
    private int destination;
    private ArrayList<Passenger> passengers;

    /**
     * Constructor with parameters
     * @param lat latitude of the bus stop
     * @param lng longitude of the bus stop
     * @param trip_distance trip distance from the bus stop to the destination
     * @param pass first passenger added to the bus stop
     */
    public BusStop(double lat, double lng,double trip_distance,Passenger pass){
        this.lat = lat;
        this.lng = lng;
        this.trip_distance = trip_distance;
        passengers = new ArrayList<>();
        passengers.add(pass);
        destination = 0;
    }

    /**
     * Get the destination
     * @return next bus stop or destination
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Set the destination
     * @param destination next bus stop or destination
     */
    public void setDestination(int destination) {
        this.destination = destination;
    }

    /**
     *  Returns latitude and longitude in the form of String separated by ','
     * @return coordenates
     */
    public String getCoor() {
        return lat+","+lng;
    }

    /**
     * Get latitude
     * @return latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     *  Set latitude
     * @param lat latitude of bus stop
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Get longitude 
     * @return longitude
     */
    public double getLng() {
        return lng;
    }

    /**
     *  Set longitude
     * @param lng longitude of the bus stop
     */
    public void setLng(double lng) {
        this.lng = lng;
    }
    
    /**
     * Get trip distance from the bus stop to the destination
     * @return trip_distance
     */
    public double getTrip_distance() {
        return trip_distance;
    }
    
    /**
     * Set trip distance from the bus stop to the destination
     * @param trip_distance trip distance from the bus stop to the destination
     */
    public void setTrip_distance(double trip_distance) {
        this.trip_distance = trip_distance;
    }

    /**
     *  get Array of Passengers
     * @return array of passengers
     */
    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }
    
    /**
     * Add new Passenger
     * @param pass new Passenger
     */
    public void addPassenger(Passenger pass){
        passengers.add(pass);
    }

    /**
     * Returns -1 if the lat and lng do not belong to any bus stop in the bus_Stop array
     * if found, return the index in the array.
     * @param bus_stops array of Bus Stop
     * @param lat latitude of the Bus stop wanted
     * @param lng longitude of the Bus stop wanted
     * @return index in the Array
     */
    public static int containedinArray(ArrayList<BusStop> bus_stops,double lat,double lng){
        int i = 0;
        for (BusStop b : bus_stops) {
            if(b.getLat()==lat && b.getLng()==lng){
                return i;
            }
            i++;
        }
        return -1;
    }
}
