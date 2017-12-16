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
    }

    public double getDistance() {
        return distance;
    }
    public void addDistance(double distance){
        this.distance += distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }
    public void addTime(double time){
        this.time += time;
    }
    public void setTime(double time) {
        this.time = time;
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
    public String getCoor(){
        return lat+","+lng;
    }

    public ArrayList<Integer> getWay() {
        return way;
    }
    
    public int getWay(int i){
        return way.get(i);
    }
    public boolean busStopsIsEmpty(){
        return bus_stops.isEmpty();
    }
    public boolean containedInArrayBusStops(double lat,double lng){
        return BusStop.containedinArray(bus_stops, lat, lng);
    }
    public void addBusStop(BusStop bus_stop){
        bus_stops.add(bus_stop);
    }
    public ArrayList<BusStop> getBusStops() {
        return bus_stops;
    }
    public double getLatBusStop(int i){
        return bus_stops.get(i).getLat();
    }
    public double getLngBusStop(int i){
        return bus_stops.get(i).getLng();
    }
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
    public void addDistance(int i,int j,double distance){
        distances[i][j] = distance;
        distances[j][i] = distance;
    }
    public void matriz(){
        for (int i = 0; i < bus_stops.size(); i++) {
            System.out.print("   "+i);
        }
        System.out.println("");
        for (int i = 0; i < bus_stops.size(); i++) {
            System.out.print(i+" ");
            for (int j = 0; j < bus_stops.size(); j++) {
                System.out.print(distances[i][j]+" ");
            }
            System.out.println("");
        }
    }
    public boolean containedInArray(ArrayList<Destination> destinations){
        for (Destination d: destinations) {
            if(d.getLat() == lat && d.getLng() == lng){
                return true;
            }
        }
        return false;
    }
    public static int indexArray(ArrayList<Destination> destinations,double lat,double lng){
        int cont=0;
        for (Destination d: destinations) {
            if(d.getLat() == lat && d.getLng() == lng){
                return cont;
            }
            cont++;
        }
        return 0;
    }
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
    /*public void optimalWay(){
        for (int i = 0; i < bus_stops.size(); i++) {
            System.out.print(""+i);
            System.out.print("-d:0");
            bus_stops.get(i).setMarked(true);
            searchWay(i,0,""+i);
            //System.out.println("");
        }
        System.out.println("Optimal Way: "+minimal_way+",destination");
        System.out.println("Distance: "+minimal_distance);
    }*/
    public void searchWay(int index_buss,double distance,String way){
        boolean all_marked = true;
        for (int i = 0; i < bus_stops.size(); i++) {
            if(!bus_stops.get(i).isMarked()){
                all_marked = false;
               /* System.out.print(" ** "+way+","+i);
                System.out.print("-d:"+(distances[index_buss][i]+distance));*/
                if(minimal_distance==-1 || (distances[index_buss][i]+distance) < minimal_distance){
                    bus_stops.get(i).setMarked(true);
                    searchWay(i, distances[index_buss][i]+distance,way+","+i);
                }
            }
        }
        if(all_marked){
            double d = bus_stops.get(index_buss).getTrip_distance()+distance;
           /* System.out.print(" ** "+way+",destination");
            System.out.print("-d:"+d);*/
            if(minimal_distance==-1 || d < minimal_distance){
                minimal_distance = d;
                minimal_way = way;
            }
        }
       // System.out.println("");
        bus_stops.get(index_buss).setMarked(false);
    }

    
}
