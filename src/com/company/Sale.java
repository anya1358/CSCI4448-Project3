package com.company;

import java.util.ArrayList;

public abstract class Sale implements Logger {
    Store store;
    ArrayList<Item> items_for_sale = new ArrayList<>();

    // Return items
    ArrayList<Item> getItems() {
        return items_for_sale;
    }
}

class ItemSale extends Sale {
    ItemSale(Item item, Store store) {
        this.items_for_sale.add(item);
        this.store = store;
    }
}


