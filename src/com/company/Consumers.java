package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class LoggerConsumer extends Observer {
    public Store store;
    public LoggerConsumer(Store store) {
        this.store = store;
    }

    //needs to have a function for printing to a file
    @Override
    public void update() {
        //update for the logger is simply storing the daily actions to a file
        File file = new File("./logger_files/Logger-"+store.today+".txt");
        try {
            boolean var = file.createNewFile();
            FileWriter myWriter = new FileWriter("./logger_files/Logger-"+store.today+".txt");
            myWriter.write("Summary for Day " + store.today + " :::::::\n");
            //handle the arrive at store
            myWriter.write(store.activeClerk.name + " arrives at the store.\n");
            myWriter.write(store.activeClerk.items_added_to_inventory + " items were added to the inventory.\n");
            //Check register and go to bank actions
            if(store.activeClerk.went_to_bank) {
                myWriter.write("The register has $" + (store.cashRegister - 1000) + ".\n");
            }
            else{
                myWriter.write("The register has $" + (store.cashRegister) + ".\n");
            }
            myWriter.write("The register has $" + store.cashRegister + " after the bank.\n");
            //DoInventory
            myWriter.write( "The total number of items in the store inventory is " + store.inventory.items.size() + ".\n");
            double total_purchase_price = 0;
            for(Item item: store.inventory.items){
                total_purchase_price += item.purchasePrice;
            }
            myWriter.write("The total purchase price of the items in the inventory is $" + total_purchase_price + ".\n");
            myWriter.write("The number of items damaged during tuning is " + store.activeClerk.items_damaged_tuning + ".\n");
            //place an order
            myWriter.write("The number of items ordered is " + store.activeClerk.items_ordered + ".\n");
            //Open the store
            myWriter.write("The number of items sold is " + store.activeClerk.items_sold + ".\n");
            myWriter.write("The number of items purchased is " + store.activeClerk.items_purchased + ".\n");
            //Clean the store
            myWriter.write("The number of items damaged while cleaning is " + store.activeClerk.items_damaged_cleaning + ".\n");
            //Leave the store
            myWriter.write(store.activeClerk.name + " left the store.\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


class TrackerConsumer extends Observer implements Logger{
    public Store store;
    HashMap<Clerk, int[]> clerk_counts;
    public TrackerConsumer(Store store) {
        this.store = store;
        clerk_counts = new HashMap<>();

    }

    @Override
    public void update() {
        //update should print out a new table with the names of the clerks and the counts of the three things
        if (!clerk_counts.containsKey(store.activeClerk)) {
            clerk_counts.put(store.activeClerk, new int[3]);
        }
        clerk_counts.get(store.activeClerk)[0] += store.activeClerk.items_sold;
        clerk_counts.get(store.activeClerk)[1] += store.activeClerk.items_purchased;
        clerk_counts.get(store.activeClerk)[2] += store.activeClerk.items_damaged_cleaning + store.activeClerk.items_damaged_tuning;

        out("Tracker: Day " + store.today);
        out("Clerk        Items Sold        Items Purchased        Items Damaged");
        for (Map.Entry<Clerk,int[]> entry : clerk_counts.entrySet())
            out(entry.getKey().name + "           " + entry.getValue()[0] + "                  " + entry.getValue()[1] + "                     " + entry.getValue()[2]);
        }
    }
