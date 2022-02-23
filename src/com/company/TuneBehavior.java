package com.company;

public interface TuneBehavior {
    void Tune();
}

class HaphazardTune implements  TuneBehavior{

    @Override
    public void Tune() {

    }
}

class ManualTune implements  TuneBehavior{

    @Override
    public void Tune() {

    }
}

class ElectronicTune implements  TuneBehavior{

    @Override
    public void Tune() {

    }
}
