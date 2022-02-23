package com.company;

public class Main {

    static final int SIM_DAYS = 30;

    public static void main(String[] args) {
        Simulation sim = new Simulation();
        sim.startSim(SIM_DAYS);
        sim.summary();
    }
}
