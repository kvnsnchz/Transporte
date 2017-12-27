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
    private ArrayList<Way> way;
    private double [][]distances;
    private double minimal_distance;
    private String minimal_way;
    private ArrayList<Integer> destination;
    private double distance,time;
    private int size_passengers;
    private double minimal_price;
    private int vehicle;
    private ArrayList<Torta> torta;
    private ArrayList<Integer> partir;
    private boolean without_capacity;
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
        destination = new ArrayList<>();
        distance = 0;
        time = 0;
        size_passengers=0;
        minimal_price = -1;
        vehicle = -1;
        without_capacity = false;
        partir = new ArrayList<>();
        torta = new ArrayList<>();
        Torta t = new Torta(0,360);
        torta.add(t);
        
        
    }

    public boolean isWithout_capacity() {
        return without_capacity;
    }

    public void setWithout_capacity(boolean without_capacity) {
        this.without_capacity = without_capacity;
    }
    
    public void partir(){
        ArrayList<Integer> par = new ArrayList<>();
        ArrayList<Double> mitad = new ArrayList<>();
        ArrayList<Double> inicio = new ArrayList<>();
        ArrayList<Double> fin = new ArrayList<>();
        for (int p : partir) {
            if (p != -1) {
                par.add(p);
                mitad.add(torta.get(p).mitad());
                inicio.add(torta.get(p).getInicio_torta());
                fin.add(torta.get(p).getFin_torta());
            }

        }
        for (int i = 0; i < par.size(); i++) {
            Torta t1 = new Torta(inicio.get(i),mitad.get(i));
            t1 = addBusStopsToTorta(t1);
            Torta t2 = new Torta(mitad.get(i), fin.get(i));
            t2 = addBusStopsToTorta(t2);
            torta.add(t1);
            torta.add(t2);
            torta.get(partir.get(i)).setEliminado(true);
            torta.remove(par.get(i));
        }
    }
    public void restartPartir(){
        partir.clear();
        for (int p : partir) {
            p = -1;
        }
        
    }
    public void addPartir(int i){
        partir.add(i);
    }
    public ArrayList<Integer> getPartir() {
        return partir;
    }

    public void setPartir(ArrayList<Integer> partir) {
        this.partir = partir;
    }
    
    public Torta getTorta(int i){
        return torta.get(i);
    }
    public ArrayList<Torta> getTorta() {
        return torta;
    }
    public int sizeTorta(){
        int cont = 0;
        for (Torta t : torta) {
            if(!t.isEliminado()){
                cont++;
            }
        }
        return cont;
    }
    public void setTorta(Torta torta,int i){
        this.torta.set(i, torta);
    }
    public void setTorta(ArrayList<Torta> torta) {
        this.torta = torta;
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
    public Torta addBusStopsToTorta(Torta t){
        int cont = 0;
        for (BusStop b : bus_stops) {
            if(b.getDegrees() >= t.getInicio_torta() && b.getDegrees() < t.getFin_torta()){
                b.setIndex_destinations(cont);
                t.addBusStop(b);
                t.addSize_passengers(b.getPassengers().size());
            }
            cont++;
        }
        return t;
        
    }
   
    
    /**
     * find the most approximate optimal way, through insertions
     */
    public void optimalWay(int j) {
        ArrayList<Edge> edges = new ArrayList<>();
        if(torta.get(j).getBus_stops().isEmpty()){
            return;
        }
        if(torta.get(j).getBus_stops().size() == 1){
            torta.get(j).getBus_stop(0).setDestination(-1);
            torta.get(j).setDestination(0);
            
        }
        else {
            if (torta.get(j).getBus_stops().size() == 2) {
                torta.get(j).getBus_stop(0).setDestination(-1);
                torta.get(j).setDestination(1);
                torta.get(j).getBus_stop(1).setDestination(0);
            }
            else {
                torta.get(j).getBus_stop(0).setDestination(2);
                torta.get(j).getBus_stop(1).setDestination(0);
                torta.get(j).getBus_stop(2).setDestination(1);
                edges.add(new Edge(0, 2, distances[torta.get(j).getI(0)][torta.get(j).getI(2)]));
                edges.add(new Edge(2, 1, distances[torta.get(j).getI(2)][torta.get(j).getI(1)]));
                edges.add(new Edge(1, 0, distances[torta.get(j).getI(1)][torta.get(j).getI(0)]));
                double distance = distances[torta.get(j).getI(1)][torta.get(j).getI(0)] + distances[torta.get(j).getI(0)][torta.get(j).getI(2)] + distances[torta.get(j).getI(2)][torta.get(j).getI(1)];
                for (int i = 3; i < torta.get(j).getBus_stops().size(); i++) {
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
                    torta.get(j).getBus_stop(ext_a).setDestination(i);
                    torta.get(j).getBus_stop(i).setDestination(ext_b);
                    edges.add(new Edge(ext_a, i, distances[torta.get(j).getI(ext_a)][torta.get(j).getI(i)]));
                    edges.add(new Edge(i, ext_b, distances[torta.get(j).getI(i)][torta.get(j).getI(ext_b)]));
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
                torta.get(j).getBus_stop(ext_a).setDestination(-1);
                torta.get(j).setDestination(ext_b);
                edges.add(new Edge(ext_a, -1, torta.get(j).getBus_stop(ext_a).getTrip_distance()));
                edges.add(new Edge(-1, ext_b, torta.get(j).getBus_stop(ext_a).getTrip_distance()));
                edges.remove(min_edge);
                torta.get(j).setDistance(distance);
                torta.get(j).setTime(distance/10);
                
            }
        }
        /*System.out.println("Optimal Way: " + minimal_way + ",destination");
        System.out.println("Distance: " + minimal_distance);*/
    }

    /**
     * Print the optim way and return it
     * @return optimal way 
     */
    public void way(int j){
        if(torta.get(j).getBus_stops().isEmpty()){
            return;
        }
        Way w = new Way();
        torta.get(j).addWay(-1);
        int d = torta.get(j).getDestination();
        do {
            d = torta.get(j).getI(d);
            torta.get(j).addWay(d);
            d = bus_stops.get(d).getDestination();
        } while (d != -1);
        torta.get(j).addWay(-1);    
    }
     public void imprimirWay(int i){
        for (int w : torta.get(i).getWay()) {
            if(w == -1){
                System.out.println("Destination "+getCoor()+" ->");
            }
            else{
                System.out.println("Bus Stop "+bus_stops.get(w).getCoor()+" ->");
            }
        }
    }
    public void calculatePrice(int id,double basic_c,double additional_km,double additional_30_min,int i){
        double price;
        if(torta.get(i).getDistance_min()== 0 || torta.get(i).getTime_min()== 0){
            price = 0;
        }
        else{
            price = basic_c;
        }
        
        double km = ((torta.get(i).getDistance_min())/1000)-15;
        double min = (torta.get(i).getTime_min()/60)-30;
        int km_i = (int)km;
        if(km_i > 0){
            price += (((double)km_i)* additional_km);
        }
        int min_30 = (int)(min/30);
        if(min_30 > 0){
            price += (((double)min_30)*additional_30_min);
        }
        if(torta.get(i).getMinimal_price() == -1 || price < torta.get(i).getMinimal_price()){
            torta.get(i).setMinimal_price(price);
            torta.get(i).setVehicle(id);
        }
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
