/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author Kevin Sanchez
 */
public class Vehicles {
    private int num_seat;
    private boolean assigned;
    public Vehicles(int num_seat) {
        this.num_seat = num_seat;
        assigned = false;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
    
    public int getNum_seat() {
        return num_seat;
    }

    public void setNum_seat(int num_seat) {
        this.num_seat = num_seat;
    }
    
}
