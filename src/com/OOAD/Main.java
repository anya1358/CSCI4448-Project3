package com.OOAD;

// Example code for Project 2 Part 2
// Bruce Montgomery - 2/14/22 - OOAD CSCI 4/5448 - CU Boulder
// Please cite any use of this code in your own future OOAD exercises

public class Main {

    static final int SIM_DAYS = 30;

    public static void main(String[] args) {
	    Simulation sim = new Simulation();
	    sim.startSim(SIM_DAYS);
	    sim.summary();
    }
}
