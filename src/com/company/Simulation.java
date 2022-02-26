package com.company;

import java.util.ArrayList;

// top level object to run the simulation
public class Simulation implements Logger, Observable {
    Store store;
    int dayCounter;
    Weekday weekDay;
    ArrayList<Observer> observerArrayList;

    @Override
    public void registerObserver(Observer observer) {
        observerArrayList.add(observer);
    }

    @Override
    public void notifyObservers()  {
        for(Observer observer: observerArrayList){
            observer.update();
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observerArrayList.remove(observer);
    }



    // enum for Weekdays
    // next implementation from
    // https://stackoverflow.com/questions/17006239/whats-the-best-way-to-implement-next-and-previous-on-an-enum-type
    public static enum Weekday {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
        private static Weekday[] vals = values();
        public Weekday next() {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }

    Simulation() {
        weekDay = Weekday.MONDAY;   //set the starting day
        dayCounter = 0;
        store = new Store();
        observerArrayList = new ArrayList<>();
    }

    void startSim(int days) {
        //add the tracker observer here
        Observer tracker = new TrackerConsumer(store);
        registerObserver(tracker);
        for (int day = 1; day <= days; day++) {
            out(" ");
            out("*** Simulation day "+day+" ***");
            Observer logger = new LoggerConsumer(store);
            registerObserver(logger);
            startDay(day);
            //first update the observers
            notifyObservers();
            //then remove the logger observer
            removeObserver(logger);
        }
        removeObserver(tracker);
    }

    void startDay(int day) {
        if (weekDay == Weekday.SUNDAY) store.closedToday(day);
        else store.openToday(day);
        weekDay = weekDay.next();
    }

    void summary() {
        int totalPrice = 0;
        out("Items remaining in inventory: ");
        for(int i = 0; i < store.inventory.items.size(); i++){
            out(store.inventory.items.get(i).itemType.toString().toLowerCase());
            totalPrice += store.inventory.items.get(i).purchasePrice;
        }
        out("Total value of items in inventory: " + totalPrice);

        totalPrice = 0;
        for(int i = 0; i < store.inventory.soldItems.size(); i++){
            out(store.inventory.soldItems.get(i).itemType.toString().toLowerCase() + " daySold: " + store.inventory.soldItems.get(i).daySold + " salePrice: " + store.inventory.soldItems.get(i).salePrice);
            totalPrice += store.inventory.soldItems.get(i).salePrice;
        }
        out("Total salePrice: " + totalPrice);

        out("Money in cash register: " + store.cashRegister);

        out("Money added from bank: " + store.cashFromBank);
    }
}
