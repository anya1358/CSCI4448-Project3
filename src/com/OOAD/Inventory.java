package com.OOAD;

import java.util.ArrayList;

public class Inventory implements Logger {

    // I specifically wanted to try to do this for now with single level ArrayLists
    // there may be better collections or nested lists that might make some of
    // the operations more efficient...

    public ArrayList<Item> items;
    public ArrayList<Item> arrivingItems;
    public ArrayList<Item> soldItems;
    public ArrayList<Item> discardedItems;

    Inventory() {
        items = new ArrayList<>();
        arrivingItems = new ArrayList<>();
        soldItems = new ArrayList<>();
        discardedItems = new ArrayList<>();

        initializeInventory(items);
    }

    void initializeInventory(ArrayList<Item> list) {
        for (int i = 0; i < 3; i++) {
            for (ItemType type: ItemType.values()) {
                Item item = makeNewItemByType(type);
                list.add(item);
            }
        }
    }

    // as I showed in Piazza posts, there are fancier ways to do this with newInstance
    // and the reflection framework, this has the advantage of being pretty clean and readable
    // we're not applying patterns here yet, otherwise this really wants to be formatted
    // as a factory
    Item makeNewItemByType(ItemType type) {
        Item item;
        switch (type) {
            case PAPERSCORE -> item = new PaperScore();
            case CD_M -> item = new CD_M();
            case VINYL -> item = new Vinyl();
            case GUITAR -> item = new Guitar();
            case BASS -> item = new Bass();
            case MANDOLIN -> item = new Mandolin();
            case FLUTE -> item = new Flute();
            case HARMONICA -> item = new Harmonica();
            case CD_P -> item = new CD_P();
            case RECORDPLAYER -> item = new RecordPlayer();
            case MP3 -> item = new MP3();
            case HAT -> item = new Hat();
            case SHIRT -> item = new Shirt();
            case BANDANA -> item = new Bandana();
            case PRACTICEAMP -> item = new PracticeAmp();
            case CABLE -> item = new Cable();
            case STRINGS -> item = new Strings();
            case SAXOPHONE -> item = new Saxophone();
            case CASSETTE_P -> item = new Cassette_P();
            case CASSETTE_M -> item = new Cassette_M();
            case GIGBAG -> item = new GigBag();
            default -> {
                out("Error in makeNewItemByType - unexpected type enum");
                item = null;
            }
        }
        return item;
    }

    // add(), remove() can be done directly to the list
    // overall count can come from size()
    // count of specific item types is needed sometimes
    int countByType(ArrayList<Item> list, ItemType type) {
        int count = 0;
        for (Item item:list) if (item.itemType == type) count += 1;
        return count;
    }

    // helper to get value of items in a list
    double getValue(ArrayList<Item> list) {
        double value = 0;
        for (Item item:list) value = value + item.purchasePrice;
        return value;
    }

    // return the index of a random Item of a given item type
    // this is a little complicated, which would probably make me question
    // if the simple ArrayList is the best way to carry items
    // Going this way for now...
    int getFromListByType(ArrayList<Item> list, ItemType type) {
        int count = countByType(list, type);
        if (count==0) return 0;
        int index = 0;
        int selecting = 1;
        int selected = 0;
        if (count>1) selecting = Utility.rndFromRange(1,count);
        for (int i = 0; i < list.size(); i++ ) {
            if (list.get(i).itemType == type) {
                index = i;
                selected += 1;
                if (selected == selecting) return index;
            }
        }
        return index;
    }

}
