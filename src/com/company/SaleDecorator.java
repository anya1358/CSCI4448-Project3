package com.company;

import java.util.ArrayList;

public abstract class SaleDecorator extends Sale {
    ArrayList<Item> temp = new ArrayList<>();

    abstract ArrayList<Item> getItems();

    // Make x amount of accessories of type
    void makeAccessories(int max, ItemType type){
        // Random amount of accessories based on given range
        int num = Utility.rndFromRange(1, max);

        // Loop through for the # of accessories needed
        for(int i = 0; i < num; i++) {
            // If there are no items of type currently in inventory
            if(store.inventory.countByType(store.inventory.items, type) == 0){
                out("No " + type.toString().toLowerCase() + " in inventory to add to sale.");
                break;
            }

            // If the number of items of type in inventory are less than the number of accessories the customer wants
            // Set the number of accessories we're selling to the count
            else if(store.inventory.countByType(store.inventory.items, type) < num){
                num = store.inventory.countByType(store.inventory.items, type);
            }

            // Add the accessory to ArrayList<Item> temp to decorate the sale later
            // Remove the item from the store inventory
            else{
                int index = store.inventory.getFromListByType(store.inventory.items, type);
                temp.add(store.inventory.items.get(index));
                store.inventory.items.remove(store.inventory.items.get(index));
            }
        }
    }
}

class GigBagAccessory extends SaleDecorator {
    Sale sale;

    GigBagAccessory(Sale sale, Store store) {
        this.sale = sale;
        items_for_sale = sale.items_for_sale;
        this.store = store;
    }

    ArrayList<Item> getItems() {
        // If it is electric, 20%
        // Not, 10%
        if(sale.items_for_sale.get(0).isElectric) {
            if(Utility.rnd() > .8) {
                makeAccessories(1, ItemType.GIGBAG);
            }
        }
        else {
            if(Utility.rnd() > .9) {
                makeAccessories(1, ItemType.GIGBAG);
            }
        }

        // Get items from the wrapped sale
        ArrayList<Item> items = sale.getItems();
        // Add items from the decorator into items list
        items.addAll(temp);

        return items;
    }
}

class PracticeAmpAccessory extends SaleDecorator {
    Sale sale;

    PracticeAmpAccessory(Sale sale, Store store) {
        this.sale = sale;
        items_for_sale = sale.items_for_sale;
        this.store = store;
    }

    ArrayList<Item> getItems() {
        // If it is electric, 25%
        // Not, 15%
        if(sale.items_for_sale.get(0).isElectric) {
            if(Utility.rnd() > .75) {
                makeAccessories(1, ItemType.PRACTICEAMP);
            }
        }
        else {
            if(Utility.rnd() > .85) {
                makeAccessories(1, ItemType.PRACTICEAMP);
            }
        }

        // Get items from the wrapped sale
        ArrayList<Item> items = sale.getItems();
        // Add items from the decorator into items list
        items.addAll(temp);

        return items;
    }
}

class CableAccessory extends SaleDecorator {
    Sale sale;

    CableAccessory(Sale sale, Store store) {
        this.sale = sale;
        items_for_sale = sale.items_for_sale;
        this.store = store;
    }

    ArrayList<Item> getItems() {
        // If it is electric, 30%
        // Not, 20%
        if(sale.items_for_sale.get(0).isElectric) {
            if(Utility.rnd() > .7) {
                makeAccessories(2, ItemType.CABLE);
            }
        }
        else {
            if(Utility.rnd() > .8) {
                makeAccessories(2, ItemType.CABLE);
            }
        }

        // Get items from the wrapped sale
        ArrayList<Item> items = sale.getItems();
        // Add items from the decorator into items list
        items.addAll(temp);

        return items;
    }
}

class StringAccessory extends SaleDecorator {
    Sale sale;

    StringAccessory(Sale sale, Store store) {
        this.sale = sale;
        items_for_sale = sale.items_for_sale;
        this.store = store;
    }

    ArrayList<Item> getItems() {
        // If it is electric, 40%
        // Not, 30%
        if(sale.items_for_sale.get(0).isElectric) {
            if(Utility.rnd() > .6) {
                makeAccessories(3, ItemType.STRINGS);
            }
        }
        else {
            if(Utility.rnd() > .7) {
                makeAccessories(3, ItemType.STRINGS);
            }
        }

        // Get items from the wrapped sale
        ArrayList<Item> items = sale.getItems();
        // Add items from the decorator into items list
        items.addAll(temp);

        return items;
    }
}

