package com.company;

import java.io.IOException;

public class Main {

    static final int SIM_DAYS = 30;

    public static void main(String[] args) throws IOException {
        Simulation sim = new Simulation();
        sim.startSim(SIM_DAYS);
        sim.summary();
    }
}
