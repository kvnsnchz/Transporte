/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;

/**
 *  Class for way edges
 * @author Kevin Sanchez
 */
public class Edge {
    private int extremeA,extremeB;
    private double value;

    /**
     *  Constructor with parameters
     * @param extremeA first extreme of the edge
     * @param extremeB second extreme of the edge
     * @param value edge weight
     */
    public Edge(int extremeA, int extremeB, double value) {
        this.extremeA = extremeA;
        this.extremeB = extremeB;
        this.value = value;
    }

    /**
     * Get the extremeA
     * @return extremeA
     */
    public int getExtremeA() {
        return extremeA;
    }

    /**
     * Set the extremeA
     * @param extremeA first extreme of the edge
     */
    public void setExtremeA(int extremeA) {
        this.extremeA = extremeA;
    }

    /**
     * Get the extreme B
     * @return extremeB
     */
    public int getExtremeB() {
        return extremeB;
    }

    /**
     * Set the extremeB
     * @param extremeB second extreme of the edge
     */
    public void setExtremeB(int extremeB) {
        this.extremeB = extremeB;
    }

    /**
     * Get the edge weight
     * @return value
     */
    public double getValue() {
        return value;
    }
    
    /**
     *  Set the edge weight
     * @param value the edge weight
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Way distance with the new bridge
     * @param values matrix of distance between vertices
     * @param bridge new vertice 
     * @param current_value current distance 
     * @return the new distance
     */
    public double distanceWithBridge(double [][]values,int bridge,double current_value){
        return values[extremeA][bridge]+values[bridge][extremeB]+current_value-value;
    }
    
    /**
     * Way distance with the new bridge
     * @param bus_stops Array of bus stops
     * @param current_value current distance
     * @return
     */
    public double distanceWithBridge(ArrayList<BusStop> bus_stops,double current_value){
        return bus_stops.get(extremeA).getTrip_distance()+bus_stops.get(extremeB).getTrip_distance()+current_value-value;
    }

    @Override
    public String toString() {
        return "("+extremeA+","+extremeB+") d->"+value;
    }
    
}
