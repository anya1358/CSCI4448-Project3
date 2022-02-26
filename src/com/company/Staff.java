package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public abstract class Staff {
    String name;    // Velma and Shaggy
}

class Clerk extends Staff implements Logger {
    int daysWorked;
    double damageChance;    // Velma = .05, Shaggy = .20
    Store store;
    int items_damaged_cleaning;
    int items_damaged_tuning;
    int items_sold;
    int items_purchased;
    int items_added_to_inventory;
    int items_ordered;
    TuneBehavior tuneBehavior;
    TuneBehaviorType tuneBehaviorType;
    ArrayList<TunableItem> tunableItems;
    public static enum TuneBehaviorType {
        HAPHAZARD, MANUAL, ELECTRONIC
    }
    //wind instruments and stringed istruments
    //flute harmonica sax, mandolin, bass
    //cd_p record player mp3 cassette_p

    Clerk(String name, double damageChance, Store store) {
         this.name = name;
         this.damageChance = damageChance;
         this.store = store;
         this.tuneBehaviorType = Utility.randomEnum(TuneBehaviorType.class);
         daysWorked = 0;
         tunableItems = new ArrayList<>();

    }

    void arriveAtStore() {
        items_damaged_cleaning = 0;
        items_damaged_tuning = 0;
        items_purchased = 0;
        items_sold = 0;
        items_added_to_inventory = 0;
        items_ordered = 0;
        out(this.name + " arrives at store.");
        // have to check for any arriving items slated for this day'
        out( this.name + " checking for arriving items.");
        // there's a tricky concurrent removal thing that prevents doing this
        // with a simple for loop - you need to use an iterator
        // https://www.java67.com/2014/03/2-ways-to-remove-elementsobjects-from-ArrayList-java.html#:~:text=There%20are%20two%20ways%20to,i.e.%20remove(Object%20obj).
        Iterator<Item> itr = store.inventory.arrivingItems.iterator();
        while (itr.hasNext()) {
            Item item = itr.next();
            if (item.dayArriving == store.today) {
                out( this.name + " putting a " + item.itemType.toString().toLowerCase() + " in inventory.");
                store.inventory.items.add(item);
                items_added_to_inventory++;
                itr.remove();
            }
        }
    }

    void checkRegister() {
        out(this.name + " checks: "+Utility.asDollar(store.cashRegister)+" in register.");
        if (store.cashRegister<75) {
            out("Cash register is low on funds.");
            this.goToBank();
        }
    }

    void goToBank() {
        out(this.name + " gets money from the bank.");
        store.cashRegister += 1000;
        store.cashFromBank += 1000;
        this.checkRegister();
    }

    void doInventory() {
        out(this.name + " is doing inventory.");
        //check if there are any tunable items in the inventory
        //get the tunable items
        findTunableItems();
        //tune all of the tunable items
        for(TunableItem tunableItem: tunableItems){
                tuneItem(tunableItem);
        }
        for (ItemType type: ItemType.values()) {
            int numItems = store.inventory.countByType(store.inventory.items,type);
            out(this.name + " counts "+numItems+" "+type.toString().toLowerCase());
            if (numItems == 0 && !store.itemsToStopSelling.contains(type)) {
                this.placeAnOrder(type);
            }
        }
        int count = store.inventory.items.size();
        double worth = store.inventory.getValue(store.inventory.items);
        out(this.name + " finds " + count + " items in store, worth "+Utility.asDollar(worth));
    }

    void findTunableItems(){
        for(Item item: store.inventory.items){
            if(item.tunable){
                TunableItem tunableItem = ((TunableItem) item);
                tunableItems.add(tunableItem);
            }
        }
    }

    void tuneItem(TunableItem item){
        //need to also maybe handle the case where the tune behavior is unidentified for the worst case scenario
        //we also need a way to check if the item is tunable

        switch (tuneBehaviorType){
            case MANUAL -> tuneBehavior = new ManualTune();
            case HAPHAZARD -> tuneBehavior = new HaphazardTune();
            case ELECTRONIC -> tuneBehavior = new ElectronicTune();
        }
        out(this.name + " is tuning a " + item.itemType.toString().toLowerCase() + " with a " + tuneBehaviorType.toString().toLowerCase() + " tuning method.");
        tuneBehavior.Tune(item, this);
    }

    void placeAnOrder(ItemType type) {
        String itemName = type.toString().toLowerCase();
        if(store.itemsToStopSelling.contains(type)) {
            if (store.inventory.countByType(store.inventory.items, type) == 0) {
                out(this.name + " did not order more " + itemName + "s because " + itemName + "s are out of stock and the store stopped selling them.");
                return;
            }
        }
        out(this.name + " needs to order "+type.toString().toLowerCase());
        // order 3 more of this item type
        // they arrive in 1 to 3 days
        int arrivalDay = Utility.rndFromRange(1,3);
        // check to see if any are in the arriving queue
        int count = store.inventory.countByType(store.inventory.arrivingItems,type);
        if (count>0) {
            out("There is an order coming for " + type.toString().toLowerCase());
        }
        else {
            // order 3 of the missing items if you have the money to buy them
            for (int i = 0; i < 3; i++) {
                Item item = store.inventory.makeNewItemByType(type);
                if (store.cashRegister > item.purchasePrice) {
                    out(this.name + " ordered a " + item.itemType.toString().toLowerCase());
                    item.dayArriving = store.today + arrivalDay;
                    store.inventory.arrivingItems.add(item);
                    items_ordered++;
                }
                else {
                    out("Insufficient funds to order this item.");
                }
            }
        }
    }

    void openTheStore() {
        //need to use a poisson distribution for the number of buyers to 2 plus a random variate from a Poisson distribution with mean 3 (this will result in
        //random numbers from 1 to about 6 or 7 with a rare spike to 10 or so).
        int buyers = 2 + getPoissonRandom(3);
        int sellers = Utility.rndFromRange(1,4);
        out(buyers + " buyers, "+sellers+" sellers today.");
        for (int i = 1; i <= buyers; i++) this.sellAnItem(i);
        for (int i = 1; i <= sellers; i++) this.buyAnItem(i);
    }

    private static int getPoissonRandom(double mean) {
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }

    void sellAnItem(int customer) {
        String custName = "Buyer "+customer;
        out(this.name+" serving "+custName);
        ItemType type = Utility.randomEnum(ItemType.class);
        out(custName + " wants to buy a "+type.toString().toLowerCase());
        int countInStock = store.inventory.countByType(store.inventory.items, type);
        // if no items - bye
        if (countInStock == 0) {
            out (custName + " leaves, no items in stock.");
        }
        else {
            // pick one of the types of items from inventory
            int pickItemIndex = Utility.rndFromRange(1,countInStock);
            Item item = GetItemFromInventoryByCount(countInStock, type);
            out("Item is "+type.toString().toLowerCase()+" in "+item.condition.toString().toLowerCase()+" condition.");
            // 50% chance to buy at listPrice
            out (this.name+" selling at "+Utility.asDollar(item.listPrice));
            if (Utility.rnd()>.5) {
                sellItemtoCustomer(item, custName);
            }
            else {
                // if not, clerk offers 10% off listPrice
                double newListPrice = item.listPrice * .9;
                out (this.name+" selling at "+Utility.asDollar(newListPrice));
                // now 75% chance of buy
                if (Utility.rnd()>.25) {
                    item.listPrice = newListPrice;
                    sellItemtoCustomer(item,custName);
                }
                else {
                    out(custName + " wouldn't buy item.");
                }
            }
        }
    }

    // things we need to do when an item is sold
    void sellItemtoCustomer(Item item, String custName) {
        String price = Utility.asDollar(item.listPrice);

        // Create new itemSale with the item and store
        Sale sale = new ItemSale(item, store);

        // Arraylist to hold ArrayList returned by getItems
        ArrayList<Item> sale_items = new ArrayList<Item>();

        // Print inventory count before sale
        out ( "inventory count: " + store.inventory.items.size());

        // If item is stringed type
        if(item.stringed){
            sale = new GigBagAccessory(sale, store);
            sale = new CableAccessory(sale, store);
            sale = new PracticeAmpAccessory(sale, store);
            sale = new StringAccessory(sale, store);
            sale_items = sale.getItems();
        }

        out (this.name + " is selling "+ sale.items_for_sale.get(0).itemType.toString().toLowerCase() + " for " + price +" to "+custName);

        // If there are any added accessories to the sale
        if(sale_items.size() > 1){
            out ("Added accessories: ");
            // Loop through the sale (starting after the first item) and print price for added accessories
            for(int i = 1; i < sale_items.size(); i++){
                price = Utility.asDollar(sale_items.get(i).listPrice);
                out(sale_items.get(i).itemType.toString().toLowerCase() + " for " + price);
            }
        }

        // Loop through the sale
        // Remove sale items from inventory
        // Set sale items salePrice and daySold
        // Add sale items to soldItems
        // Update money from items to register
        for(int i = 0; i < sale_items.size(); i++){
            store.inventory.items.remove(sale_items.get(i));

            sale_items.get(i).salePrice = sale_items.get(i).listPrice;
            sale_items.get(i).daySold = store.today;

            store.inventory.soldItems.add(sale_items.get(i));

            store.cashRegister += sale_items.get(i).listPrice;
            items_sold++;
        }

        // Reprint inventory count to double check if sale items got removed correctly
        out ( "inventory count: "+store.inventory.items.size());
    }

    // find a selected item of a certain type from the items
    Item GetItemFromInventoryByCount(int countInStock, ItemType type) {
        int count = 0;
        for(Item item: store.inventory.items) {
            if (item.itemType == type) {
                count += 1;
                if (count == countInStock) return item;
            }
        }
        return null;
    }

    void buyAnItem(int customer) {
        String custName = "Seller "+customer;
        out(this.name+" serving "+custName);
        ItemType type = Utility.randomEnum(ItemType.class);
        out(custName + " wants to sell a "+type.toString().toLowerCase());
        Item item = store.inventory.makeNewItemByType(type);
        String itemName = item.itemType.toString().toLowerCase();
        if(store.itemsToStopSelling.contains(item.itemType)){
            if(store.inventory.allClothingItemsSold()){
                out(this.name + " will not buy " + itemName +" from customer " + customer + " because all clothing items are out of stock.");
                return;
            }
        }

        // clerk will determine new or used, condition, purchase price (based on condition)
        // we'll take the random isNew, condition from the generated item
        out("Item is "+type.toString().toLowerCase()+" in "+item.condition.toString().toLowerCase()+" condition.");
        item.purchasePrice = getPurchasePriceByCondition(item.condition);
        // seller has 50% chance of selling
        out (this.name+" offers "+Utility.asDollar(item.purchasePrice));
        if (Utility.rnd()>.5) {
            buyItemFromCustomer(item, custName);
        }
        else {
            // if not, clerk will add 10% to purchasePrice
            item.purchasePrice += item.purchasePrice * .10;
            out (this.name+" offers "+Utility.asDollar(item.purchasePrice));
            // seller has 75% chance of selling
            if (Utility.rnd()>.25) {
                buyItemFromCustomer(item, custName);
            }
            else {
                out(custName + " wouldn't sell item.");
            }
        }
    }

    void buyItemFromCustomer(Item item, String custName) {
        String itemName = item.itemType.toString().toLowerCase();
        String price = Utility.asDollar(item.purchasePrice);
        out (this.name + " is buying "+ itemName + " for " + price +" from "+custName);
        if (store.cashRegister>item.purchasePrice) {
            store.cashRegister -= item.purchasePrice;
            item.listPrice = 2 * item.purchasePrice;
            item.dayArriving = store.today;
            store.inventory.items.add(item);
            items_purchased++;
        }
        else {
            out(this.name + "cannot buy item, register only has "+Utility.asDollar(store.cashRegister));
        }
    }


    double getPurchasePriceByCondition(Condition condition) {
        int lowPrice = 2*condition.level;
        int highPrice = 10*condition.level;
        return (double) Utility.rndFromRange(lowPrice,highPrice);
    }


    void cleanTheStore() {
        out(this.name + " is cleaning up the store.");
        if (Utility.rnd()>this.damageChance) {
            out(this.name + " doesn't break anything.");
        }
        else {

            // reduce the condition for a random item
            // take the item off the main inventory and put it on the broken items ArrayList
            // left as an exercise to the reader :-)
            items_damaged_cleaning++;
            int index = (int)(Math.random() * store.inventory.items.size());
            Item item_to_break = store.inventory.items.get(index);
            damageItem(item_to_break);


        }
    }

    void damageItem(Item item){
        out(this.name + " breaks something!");
        if(item.condition == Condition.POOR){
            store.inventory.items.remove(item);
            out("Item was removed from inventory.");
            return;
        }
        item.damageAnItem();
    }
    void leaveTheStore() {
        out(this.name + " locks up the store and leaves.");
    }
}
