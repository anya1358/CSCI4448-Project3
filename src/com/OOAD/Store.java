package com.OOAD;
import java.util.ArrayList;

public class Store implements Logger {
    public ArrayList<Clerk> clerks;
    public Clerk activeClerk;
    public double cashRegister;
    public double cashFromBank;
    public Inventory inventory;
    public int today;

    Store() {
        // initialize the store's starting inventory
        inventory = new Inventory();

        cashRegister = 0;   // cash register is empty to begin
        cashFromBank = 0;   // no cash from bank yet

        // initialize the store's staff
        clerks = new ArrayList<Clerk>();
        clerks.add(new Clerk("Velma",.05, this));
        clerks.add(new Clerk("Shaggy", .20, this));
        clerks.add(new Clerk("Daphne", .10, this));
    }

    void openToday(int day) {
        today = day;
        out("Store opens today, day "+day);
        activeClerk = getValidClerk();
        out(activeClerk.name + " is working today.");
        activeClerk.arriveAtStore();
        activeClerk.checkRegister();
        activeClerk.doInventory();
        activeClerk.openTheStore();
        activeClerk.cleanTheStore();
        activeClerk.leaveTheStore();
    }

    Clerk getValidClerk() {
        // pick a random clerk
        Clerk clerk = clerks.get(Utility.rndFromRange(0,clerks.size()-1));
        // if they are ok to work, set days worked on other clerks to 0

        // Placeholder for sick clerk
        Clerk sick = clerk;

        // Chance of sickness (.1)
        if(Utility.rnd() > .9) {
            out(clerk.name+" is sick and cannot work today.");
            clerk.daysWorked = 0;
            for (Clerk other: clerks) {
                if (other != clerk) {
                    sick = clerk;
                    clerk = other;
                    break;
                }
            }
        }

        if (clerk.daysWorked < 3) {
            clerk.daysWorked += 1;
            for (Clerk other: clerks) {
                if (other != clerk) other.daysWorked = 0; // they had a day off, so clear their counter
            }
        }
        // if they are not ok to work, set their days worked to 0 and get another clerk
        else {
            out(clerk.name+" has worked maximum of 3 days in a row.");
            clerk.daysWorked = 0;   // they can't work, get another clerk
            for (Clerk other: clerks) {
                if (other != clerk & other != sick) {
                    clerk = other;
                    break;
                }
            }
        }
        return clerk;
    }

    void closedToday(int day) {
        out("Store is closed today, day "+day);
    }
}
