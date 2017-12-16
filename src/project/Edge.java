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
public class Edge {
    private int extremeA,extremeB;
    private double value;

    public Edge(int extremeA, int extremeB, double value) {
        this.extremeA = extremeA;
        this.extremeB = extremeB;
        this.value = value;
    }

    public int getExtremeA() {
        return extremeA;
    }

    public void setExtremeA(int extremeA) {
        this.extremeA = extremeA;
    }

    public int getExtremeB() {
        return extremeB;
    }

    public void setExtremeB(int extremeB) {
        this.extremeB = extremeB;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    public double distanceWithBridge(double [][]values,int bridge,double current_value){
        return values[extremeA][bridge]+values[bridge][extremeB]+current_value-value;
    }
    public double distanceWithBridge(ArrayList<BusStop> bus_stops,double current_value){
        return bus_stops.get(extremeA).getTrip_distance()+bus_stops.get(extremeB).getTrip_distance()+current_value-value;
    }

    @Override
    public String toString() {
        return "("+extremeA+","+extremeB+") d->"+value;
    }
    
}
