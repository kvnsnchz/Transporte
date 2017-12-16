/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;

/**
 * Class of destination, contains the bus stops that are required to travel to reach this destination.
 * @author Kevin Sanchez
 */
public class Destination {
    private double lat;
    private double lng;
    private ArrayList<BusStop> bus_stops;
    private ArrayList<Integer> way;
    private double [][]distances;
    private double minimal_distance;
    private String minimal_way;
    private int destination;
    private double distance,time;
    private int size_passengers;
    private double minimal_price;
    private int vehicle;

    /**
     * Contructor with parameters
     * @param coor a String with latitude and longitude separated by one ','
     */
    public Destination(String coor){
        String []string = coor.split(",");
        lat = Double.parseDouble(string[0]);
        lng = Double.parseDouble(string[1]);
        bus_stops = new ArrayList<>();
        way = new ArrayList<>();
        minimal_distance = -1;
        destination = 0;
        distance = 0;
        time = 0;
        size_passengers=0;
        minimal_price = -1;
        vehicle = -1;
    }

    /**
     * Get the assigned vehicle 
     * @return assigned vehicle
     */
    public int getVehicle() {
        return vehicle;
    }
    
    /**
     * Get the minimal price
     * @return minimal_price
     */
    public double getMinimal_price() {
        return minimal_price;
    }
    
    /**
     * Get size the all the array passengers
     * @return size_passengers
     */
    public int getSize_passengers() {
        return size_passengers;
    }
    
    /**
     * Get optimal distance
     * @return distance
     */
    public double getDistance() {
        return distance;
    }
    
    /**
     * Sum to the minimal distance the value of distance
     * @param distance value to add
     */
    public void addDistance(double distance){
        this.distance += distance;
    }
    
    /**
     * Set optimal distance
     * @param distance new optimal distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Get minimal time
     * @return time
     */
    public double getTime() {
        return time;
    }

    /**
     * Sum to the minimal time the value of time
     * @param time value to add
     */
    public void addTime(double time){
        this.time += time;
    }

    /**
     * Set minimal time
     * @param time new minimal time
     */
    public void setTime(double time) {
        this.time = time;
    }
    
    /**
     * Get latitude of the destination
     * @return lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * Set latitude of the destination
     * @param lat new latitude
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Get longitude of the destination
     * @return lng
     */
    public double getLng() {
        return lng;
    }

    /**
     *  Set longitude of the destination
     * @param lng new londitude
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * Returns latitude and longitude in the form of String separated by ','
     * @return coordenates
     */
    public String getCoor(){
        return lat+","+lng;
    }

    /**
     * Get the array of ways
     * @return way
     */
    public ArrayList<Integer> getWay() {
        return way;
    }
    
    /**
     * Get an element of the way
     * @param i index of the element
     * @return element of the way
     */
    public int getWay(int i){
        return way.get(i);
    }

    /**
     * Return true if bus stop is empty
     * else return false
     * @return bus_stops.isEmpty();
     */
    public boolean busStopsIsEmpty(){
        return bus_stops.isEmpty();
    }

    /**
     *  Call the method containedinArray() the BusStop
     * @param lat latitude the bus stop wanted
     * @param lng longitude the bus stop wanted
     * @return result the containedinArray the BusStop
     */
    public int containedInArrayBusStops(double lat,double lng){
        return BusStop.containedinArray(bus_stops, lat, lng);
    }

    /**
     * Add a new bus stop to array bus_stops
     * @param bus_stop bus stop to add
     */
    public void addBusStop(BusStop bus_stop){
        bus_stops.add(bus_stop);
        size_passengers++;
    }

    /**
     * Get a bus stop the array bus_stops
     * @param i index the bus stop
     * @return bus stop 
     */
    public BusStop getBusStop(int i){
        return bus_stops.get(i);
    }

    /**
     * Add a new passenger to array and increase size_passengers
     * @param i index the bus stop
     * @param pass passenger to add
     */
    public void addPassenger(int i,Passenger pass){
        bus_stops.get(i).addPassenger(pass);
        size_passengers++;
    }

    /**
     * Get the array bus_stops
     * @return bus_stops
     */
    public ArrayList<BusStop> getBusStops() {
        return bus_stops;
    }

    /**
     * Get latitude the specific bus stop 
     * @param i index the bus stop
     * @return latitude the bus stop specific
     */
    public double getLatBusStop(int i){
        return bus_stops.get(i).getLat();
    }
    
    /**
     * Get longitude the specific bus stop 
     * @param i index the bus stop
     * @return longitude the bus stop specific
     */
    public double getLngBusStop(int i){
        return bus_stops.get(i).getLng();
    }
    
    /**
     * Give memory to the matrix of distances
     */
    public void startMatrixDistance(){
        distances = new double[bus_stops.size()][];
        for(int i = 0; i < bus_stops.size(); i++){
            distances[i] = new double[bus_stops.size()];
        }
        for (int i = 0; i < bus_stops.size(); i++) {
            for (int j = 0; j < bus_stops.size(); j++) {
                distances[i][j] = 0;
            }
        }
    }
    
    /**
     * Add new distance to the matrix the distances
     * @param i row index
     * @param j column index
     * @param distance value to distance
     */
    public void addDistance(int i,int j,double distance){
        distances[i][j] = distance;
        distances[j][i] = distance;
    }

    /**
     * Returns true if the lat and lng do not belong to destination in the destinations array
     * if found, return false.
     * @param destinations array of Destination
     * @return boolean value
     */
    public boolean containedInArray(ArrayList<Destination> destinations){
        for (Destination d: destinations) {
            if(d.getLat() == lat && d.getLng() == lng){
                return true;
            }
        }
        return false;
    }

    /**
     * Return the index the destination in the array, if not found return -1
     * @param destinations array of Destination
     * @param lat latitude the destination wanted
     * @param lng longitude the destination wanted
     * @return index
     */
    public static int indexArray(ArrayList<Destination> destinations,double lat,double lng){
        int cont=0;
        for (Destination d: destinations) {
            if(d.getLat() == lat && d.getLng() == lng){
                return cont;
            }
            cont++;
        }
        return -1;
    }

    /**
     * find the most approximate optimal way, through insertions
     */
    public void optimalWay() {
        ArrayList<Edge> edges = new ArrayList<>();
        bus_stops.get(0).setDestination(2);
        bus_stops.get(1).setDestination(0);
        bus_stops.get(2).setDestination(1);
        edges.add(new Edge(0, 2, distances[0][2]));
        edges.add(new Edge(2, 1, distances[2][1]));
        edges.add(new Edge(1, 0, distances[1][0]));
        double distance = distances[1][0] + distances[2][1] + distances[2][1];
        for (int i = 3; i < bus_stops.size(); i++) {
            double distance_min_bridge = -1;
            int min_edge = 0;
            int cont = 0;
            for (Edge edge : edges) {
                double distance_bridge = edge.distanceWithBridge(distances, i, distance);
                if (distance_min_bridge == -1 || distance_bridge < distance_min_bridge) {
                    distance_min_bridge = distance_bridge;
                    min_edge = cont;
                }
                cont++;
            }
            distance = distance_min_bridge;
            int ext_a = edges.get(min_edge).getExtremeA();
            int ext_b = edges.get(min_edge).getExtremeB();
            bus_stops.get(ext_a).setDestination(i);
            bus_stops.get(i).setDestination(ext_b);
            edges.add(new Edge(ext_a, i, distances[ext_a][i]));
            edges.add(new Edge(i, ext_b, distances[i][ext_b]));
            edges.remove(min_edge);
        }
        double distance_min_bridge = -1;
        int min_edge = 0;
        int cont = 0;
        for (Edge edge : edges) {
            double distance_bridge = edge.distanceWithBridge(bus_stops, distance);
            if (distance_min_bridge == -1 || distance_bridge < distance_min_bridge) {
                distance_min_bridge = distance_bridge;
                min_edge = cont;
            }
            cont++;
        }
        distance = distance_min_bridge;
        int ext_a = edges.get(min_edge).getExtremeA();
        int ext_b = edges.get(min_edge).getExtremeB();
        bus_stops.get(ext_a).setDestination(-1);
        destination = ext_b;
        edges.add(new Edge(ext_a, -1, bus_stops.get(ext_a).getTrip_distance()));
        edges.add(new Edge(-1, ext_b, bus_stops.get(ext_b).getTrip_distance()));
        edges.remove(min_edge);
        way = way();
        /*System.out.println("Optimal Way: " + minimal_way + ",destination");
        System.out.println("Distance: " + minimal_distance);*/
    }

    /**
     * Print the optim way and return it
     * @return optimal way 
     */
    public ArrayList<Integer> way(){
        ArrayList<Integer> way = new ArrayList<>();
        System.out.println("Way");
        System.out.println("Destination: "+getCoor()+" -> ");
        int d = destination;
        way.add(-1);
        do {  
            way.add(d);
            System.out.println("Bus Stop: "+bus_stops.get(d).getCoor()+ "->");
            d = bus_stops.get(d).getDestination();
        } while (d!=-1);
        way.add(-1);
        System.out.println("Destination: "+getCoor());
        return way;
    }

    /**
     * Calculate the price of the vehicle with the parameters sent, and see if it is the most optimal
     * @param id id the vehicle
     * @param basic_c basic cost the vehicle
     * @param additional_km price the every additional 15 km the vehicle
     * @param additional_30_min price the every additional 30 min the vehicle
     */
    public void calculatePrice(int id,double basic_c,double additional_km,double additional_30_min){
        double price;
        if(distance == 0 || time == 0){
            price = 0;
        }
        else{
            price = basic_c;
        }
        
        double km = ((distance)/1000)-15;
        double min = (time/60)-30;
        int km_i = (int)km;
        if(km_i > 0){
            price += (((double)km_i)* additional_km);
        }
        int min_30 = (int)(min/30);
        if(min_30 > 0){
            price += (((double)min_30)*additional_30_min);
        }
        if(minimal_price == -1 || price < minimal_price){
            minimal_price = price;
            vehicle = id;
        }
    }
}
