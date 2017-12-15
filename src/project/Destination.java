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
    private double [][]distances;
    private double minimal_distance;
    private String minimal_way;
    public Destination(String coor){
        String []string = coor.split(",");
        lat = Double.parseDouble(string[0]);
        lng = Double.parseDouble(string[1]);
        bus_stops = new ArrayList<>();
        minimal_distance = -1;
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
    public boolean busStopsIsEmpty(){
        return bus_stops.isEmpty();
    }
    public boolean containedInArrayBusStops(double lat,double lng){
        return BusStop.containedinArray(bus_stops, lat, lng);
    }
    public void addBusStop(BusStop bus_stop){
        bus_stops.add(bus_stop);
    }

    public ArrayList<BusStop> getBus_stops() {
        return bus_stops;
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
    public void optimalWay(){
        for (int i = 0; i < bus_stops.size(); i++) {
            System.out.print(""+i);
            System.out.print("-d:0");
            bus_stops.get(i).setMarked(true);
            searchWay(i,0,""+i);
            System.out.println("");
        }
        System.out.println("Optimal Way: "+minimal_way+",destination");
        System.out.println("Distance: "+minimal_distance);
    }
    public void searchWay(int index_buss,double distance,String way){
        boolean all_marked = true;
        for (int i = 0; i < bus_stops.size(); i++) {
            if(!bus_stops.get(i).isMarked()){
                all_marked = false;
                System.out.print(" ** "+way+","+i);
                System.out.print("-d:"+(distances[index_buss][i]+distance));
                if(minimal_distance==-1 || (distances[index_buss][i]+distance) < minimal_distance){
                    bus_stops.get(i).setMarked(true);
                    searchWay(i, distances[index_buss][i]+distance,way+","+i);
                }
            }
        }
        if(all_marked){
            double d = bus_stops.get(index_buss).getTrip_distance()+distance;
            System.out.print(" ** "+way+",destination");
            System.out.print("-d:"+d);
            if(minimal_distance==-1 || d < minimal_distance){
                minimal_distance = d;
                minimal_way = way;
            }
        }
        System.out.println("");
        bus_stops.get(index_buss).setMarked(false);
    }

    
}
