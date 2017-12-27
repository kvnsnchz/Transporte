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
public class Torta {
    private double inicio_torta;
    private double fin_torta;
    private int destination;
    private ArrayList<BusStop> bus_stops;
    private double distance,distance_min;
    private double time,time_min;
    private double minimal_price;
    private ArrayList<Integer> way;
    private boolean fine;
    private int vehicle;
    private int size_passengers;
    private boolean eliminado;
    public Torta(double inicio_torta, double fin_torta){
        this.inicio_torta = inicio_torta;
        this.fin_torta = fin_torta;
        distance = 0;
        time = 0;
        destination = 0;
        bus_stops = new ArrayList<>();
        way = new ArrayList<>();
        fine = false;
        size_passengers = 0;
        eliminado = false;
        minimal_price = -1;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    public int getSize_passengers() {
        return size_passengers;
    }

    public void setSize_passengers(int size_passengers) {
        this.size_passengers = size_passengers;
    }
    public void addSize_passengers(int size_passengers){
        this.size_passengers += size_passengers;
    }
    public int getVehicle() {
        return vehicle;
    }

    public void setVehicle(int vehicle) {
        this.vehicle = vehicle;
    }
   
    public double getMinimal_price() {
        return minimal_price;
    }

    public void setMinimal_price(double minimal_price) {
        this.minimal_price = minimal_price;
    }
    
    public double getDistance_min() {
        return distance_min;
    }
    public void addDistance_min(double distance_min){
        this.distance_min += distance_min;
    }
    public void setDistance_min(double distance_min) {
        this.distance_min = distance_min;
    }

    public double getTime_min() {
        return time_min;
    }
    public void addTime_min(double time_min){
        this.time_min += time_min;
    }
    public void setTime_min(double time_min) {
        this.time_min = time_min;
    }
    
    public boolean isFine() {
        return fine;
    }

    public void setFine(boolean fine) {
        this.fine = fine;
    }
    
    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
    
    public double getInicio_torta() {
        return inicio_torta;
    }

    public void setInicio_torta(double inicio_torta) {
        this.inicio_torta = inicio_torta;
    }

    public double getFin_torta() {
        return fin_torta;
    }

    public void setFin_torta(double fin_torta) {
        this.fin_torta = fin_torta;
    }
    public double mitad(){
        return ((inicio_torta+fin_torta)/2);
    }
    
    public void addBusStop(BusStop bus_stop){
        bus_stops.add(bus_stop);
    }
    public BusStop getBus_stop(int i){
        return bus_stops.get(i);
    }
    public int getI(int i){
        if(bus_stops.isEmpty()){
            return 0;
        }
        return bus_stops.get(i).getIndex_destinations();
    }

    public void setBus_stops(ArrayList<BusStop> bus_stops) {
        this.bus_stops = bus_stops;
    }
    public void addWay(int i){
        way.add(i);
    }
    public ArrayList<Integer> getWay() {
        return way;
    }
    public int getWay(int i){
        return way.get(i);
    }
    public void setWay(ArrayList<Integer> way) {
        this.way = way;
    }
    
    public ArrayList<BusStop> getBus_stops() {
        return bus_stops;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
   
    public static int indexArray(ArrayList<Torta> torta,double inicio,double fin){
        int cont=0;
        for (Torta t: torta) {
            if(t.getInicio_torta()== inicio && t.getFin_torta()== fin){
                return cont;
            }
            cont++;
        }
        return -1;
    }
}
