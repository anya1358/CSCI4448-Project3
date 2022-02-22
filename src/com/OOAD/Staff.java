package com.OOAD;

import java.util.Iterator;

public abstract class Staff {
    String name;    // Velma and Shaggy
}

class Clerk extends Staff implements Logger {
    int daysWorked;
    double damageChance;    // Velma = .05, Shaggy = .20
    Store store;

    Clerk(String name, double damageChance, Store store) {
         this.name = name;
         this.damageChance = damageChance;
         this.store = store;
         daysWorked = 0;
    }

    void arriveAtStore() {
        out(this.name + " arrives at store.");
        // have to check for any arriving items slated for this day
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
        for (ItemType type: ItemType.values()) {
            int numItems = store.inventory.countByType(store.inventory.items,type);
            out(this.name + " counts "+numItems+" "+type.toString().toLowerCase());
            if (numItems == 0) {
                this.placeAnOrder(type);
            }
        }
        int count = store.inventory.items.size();
        double worth = store.inventory.getValue(store.inventory.items);
        out(this.name + " finds " + count + " items in store, worth "+Utility.asDollar(worth));
    }

    void placeAnOrder(ItemType type) {
        String itemName = type.toString().toLowerCase();
        if(itemName == "hat" || itemName == "shirt" || itemName == "bandana"){
            if(store.inventory.countByType(store.inventory.items, type) == 0){
                out(this.name + " will not order more " + itemName + " because clothing is out of stock.");
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
                }
                else {
                    out("Insufficient funds to order this item.");
                }
            }
        }
    }

    void openTheStore() {
        int buyers = Utility.rndFromRange(1,10);
        int sellers = Utility.rndFromRange(1,4);
        out(buyers + " buyers, "+sellers+" sellers today.");
        for (int i = 1; i <= buyers; i++) this.sellAnItem(i);
        for (int i = 1; i <= sellers; i++) this.buyAnItem(i);
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
    void sellItemtoCustomer(Item item,String custName) {
        String itemName = item.itemType.toString().toLowerCase();
        String price = Utility.asDollar(item.listPrice);
        out (this.name + " is selling "+ itemName + " for " + price +" to "+custName);
        // when sold - move item to sold items with daySold and salePrice noted
        out ( "inventory count: "+store.inventory.items.size());
        store.inventory.items.remove(item);
        out ( "inventory count: "+store.inventory.items.size());
        item.salePrice = item.listPrice;
        item.daySold = store.today;
        store.inventory.soldItems.add(item);
        // money for item goes to register
        store.cashRegister += item.listPrice;
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
        if(itemName == "hat" || itemName == "shirt" || itemName == "bandana"){
            if(store.inventory.countByType(store.inventory.items, type) == 0){
                out(this.name + " will not buy " + itemName + " because clothing is out of stock.");
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
            out(this.name + " breaks something!");
            // reduce the condition for a random item
            // take the item off the main inventory and put it on the broken items ArrayList
            // left as an exercise to the reader :-)
            int index = (int)(Math.random() * store.inventory.items.size());
            Item item_to_break = store.inventory.items.get(index);
            if(item_to_break.condition == Condition.POOR){
                store.inventory.items.remove(item_to_break);
                out("Item was removed from inventory.");
                return;
            }
            item_to_break.damageAnItem(item_to_break);

        }
    }
    void leaveTheStore() {
        out(this.name + " locks up the store and leaves.");
    }
}
