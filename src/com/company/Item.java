package com.company;

import java.util.ArrayList;

public abstract class Item implements Logger {
    String name;                // I didn't implement a naming scheme - mostly ignoring this - how would you?
    double purchasePrice;       // $1 to $50
    double listPrice;           // purchasePrice x 2
    boolean isNew;              // set by constructor randomly
    int dayArriving;            // 0 at initialization, otherwise set at delivery
    Condition condition;        // set by constructor randomly
    double salePrice;           // set when sold
    int daySold;                // set when sold
    ItemType itemType;          // set by subclass constructors
    Inventory inventory;
    ArrayList<Item> accessories;

    void damageAnItem() {
        //first decrement the listPrice by 20%
        //then decrement the condition
        out("The broken item was a " + condition.toString().toLowerCase() + " condition " + itemType.toString().toLowerCase() + " with a list price of $" + listPrice);
        listPrice -= 0.2 * listPrice;
        condition.level -= 1;
        out("Condition has been reduced to " + condition.toString().toLowerCase() + " and the list price has been reduced 20% to $" + listPrice);
    }

    Item(Inventory inventory) {
        // common initialization of a new instance
        purchasePrice = Utility.rndFromRange(1,50);
        listPrice = 2 * purchasePrice;
        isNew = (Utility.rnd() > .5);  // coin flip for new or used
        dayArriving = 0;
        condition = Utility.randomEnum(Condition.class);
        salePrice = 0;
        daySold = 0;
        this.inventory = inventory;
    };
}

abstract class Music extends Item {
    String band;
    String album;
    String[] bands = {"Yes","Jethro Tull","Rush","Genesis","ELP","Enya"};
    String[] albums = {"Fragile","Stormwatch","2112","Abacab","Tarkus","The Memory of Trees"};
    Music(Inventory inventory) {
        super(inventory);
        band = bands[Utility.rndFromRange(0,bands.length-1)];
        album = albums[Utility.rndFromRange(0,albums.length-1)];
    }
}

class PaperScore extends Music {
    PaperScore(Inventory inventory) {
        super(inventory);
        itemType = ItemType.PAPERSCORE;
    }
}
class CD_M extends Music {
    CD_M(Inventory inventory) {
        super(inventory);
        itemType = ItemType.CD_M;
    }
}
class Vinyl extends Music {
    Vinyl(Inventory inventory) {
        super(inventory);
        itemType = ItemType.VINYL;
    }
}

class Cassette_M extends Music {
    Cassette_M(Inventory inventory) {
        super(inventory);
        itemType = ItemType.CASSETTE_M;
    }
}

abstract class Player extends Item {
    boolean equalized;
    Player(Inventory inventory) {
        super(inventory);
        equalized = false;
    }
}

class CD_P extends Player {
    CD_P(Inventory inventory) {
        super(inventory);
        itemType = ItemType.CD_P;
    }
}

class RecordPlayer extends Player {
    RecordPlayer(Inventory inventory) {
        super(inventory);
        itemType = ItemType.RECORDPLAYER;
    }
}

class MP3 extends Player {
    MP3(Inventory inventory) {
        super(inventory);
        itemType = ItemType.MP3;
    }
}

class Cassette_P extends Player {
    Cassette_P(Inventory inventory) {
        super(inventory);
        itemType = ItemType.CASSETTE_P;
    }
}

abstract class Instrument extends Item {
    Instrument(Inventory inventory) {
        super(inventory);
    }
}

abstract class Stringed extends Instrument {
    boolean isElectric;
    boolean tuned;
    Stringed(Inventory inventory) {
        super(inventory);
        isElectric = (Utility.rnd()>.5); // coin flip for electric or acoustic
        tuned = false;
    }
}

abstract class AccessoryDecorator extends Stringed {
    Stringed instrument;

    AccessoryDecorator(Stringed instrument, Inventory inventory){
        super(inventory);
        this.instrument = instrument;
    }

    abstract void SellAccessory();
}

class GigBagAccessory extends AccessoryDecorator {
    Stringed instrument;

    GigBagAccessory(Stringed instrument, Inventory inventory){
        super(instrument, inventory);
        this.instrument = instrument;
    }

    void SellAccessory() {
        if(instrument.isElectric){
            if(Utility.rnd() > .8){
                if(inventory.countByType(inventory.items, ItemType.GIGBAG) == 0){
                    out("No Gig Bags left in inventory to sell");
                    return;
                }
                else {
                    accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.GIGBAG)));
                }
            }
            else{
                return;
            }
        }
        else{
            if(Utility.rnd() > .9){
                if(inventory.countByType(inventory.items, ItemType.GIGBAG) == 0){
                    out("No Gig Bags left in inventory to sell");
                    return;
                }
                else {
                    accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.GIGBAG)));
                }
            }
            else{
                return;
            }
        }
    }
}

class PracticeAmpAccessory extends AccessoryDecorator {
    Stringed instrument;

    PracticeAmpAccessory(Stringed instrument, Inventory inventory){
        super(instrument, inventory);
        this.instrument = instrument;
    }

    void SellAccessory() {
        if(instrument.isElectric){
            if(Utility.rnd() > .75){
                if(inventory.countByType(inventory.items, ItemType.PRACTICEAMP) == 0){
                    out("No Practice Amps left in inventory to sell");
                    return;
                }
                else {
                    accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.PRACTICEAMP)));
                }
            }
            else{
                return;
            }
        }
        else{
            if(Utility.rnd() > .85){
                if(inventory.countByType(inventory.items, ItemType.PRACTICEAMP) == 0){
                    out("No Practice Amps left in inventory to sell");
                    return;
                }
                else {
                    accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.PRACTICEAMP)));
                }
            }
            else{
                return;
            }
        }
    }
}

class CablesAccessory extends AccessoryDecorator {
    Stringed instrument;

    CablesAccessory(Stringed instrument, Inventory inventory){
        super(instrument, inventory);
        this.instrument = instrument;
    }

    void SellAccessory() {
        if(instrument.isElectric){
            if(Utility.rnd() > .7){
                if(inventory.countByType(inventory.items, ItemType.CABLE) == 0){
                    out("No Cables left in inventory to sell");
                    return;
                }
                else{
                    for(int i = 0; i < Utility.rndFromRange(1, 2); i++){
                        accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.CABLE)));
                    }
                }
            }
            else{
                return;
            }
        }
        else{
            if(Utility.rnd() > .8){
                if(inventory.countByType(inventory.items, ItemType.CABLE) == 0){
                    out("No Cables left in inventory to sell");
                    return;
                }
                else {
                    for (int i = 0; i < Utility.rndFromRange(1, 2); i++) {
                        accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.CABLE)));
                    }
                }
            }
            else{
                return;
            }
        }
    }
}

class StringsAccessory extends AccessoryDecorator {
    Stringed instrument;

    StringsAccessory(Stringed instrument, Inventory inventory){
        super(instrument, inventory);
        this.instrument = instrument;
    }

    void SellAccessory() {
        if(instrument.isElectric){
            if(Utility.rnd() > .6){
                if(inventory.countByType(inventory.items, ItemType.STRINGS) == 0){
                    out("No Strings left in inventory to sell");
                    return;
                }
                else {
                    for (int i = 0; i < Utility.rndFromRange(1, 3); i++) {
                        accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.STRINGS)));
                    }
                }
            }
            else{
                return;
            }
        }
        else{
            if(Utility.rnd() > .7){
                if(inventory.countByType(inventory.items, ItemType.STRINGS) == 0){
                    out("No Strings left in inventory to sell");
                    return;
                }
                else{
                    for(int i = 0; i < Utility.rndFromRange(1, 3); i++){
                        accessories.add(inventory.items.get(inventory.getFromListByType(inventory.items, ItemType.STRINGS)));
                    }
                }
            }
            else{
                return;
            }
        }
    }
}

class Guitar extends Stringed {
    Guitar(Inventory inventory) {
        super(inventory);
        itemType = ItemType.GUITAR;
    }
}

class Bass extends Stringed {
    Bass(Inventory inventory) {
        super(inventory);
        itemType = ItemType.BASS;
    }
}
class Mandolin extends Stringed {
    Mandolin(Inventory inventory) {
        super(inventory);
        itemType = ItemType.MANDOLIN;
    }
}

abstract class Wind extends Instrument {
    boolean adjusted;
    Wind(Inventory inventory) {
        super(inventory);
        adjusted = false;
    }
}

class Flute extends Wind {
    String type;
    String[] types = {"Piccolo","Alto","Bass","Tierce","Concert","Plastic"};
    Flute(Inventory inventory) {
        super(inventory);
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.FLUTE;
    }
}
class Harmonica extends Wind {
    String key;
    String keys[] = {"E","A","G","C","D"};
    Harmonica(Inventory inventory) {
        super(inventory);
        key = keys[Utility.rndFromRange(0,keys.length-1)];
        itemType = ItemType.HARMONICA;
    }
}

class Saxophone extends Wind {
    String type;
    String[] types = {"Type1", "Type2"};
    Saxophone(Inventory inventory) {
        super(inventory);
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.SAXOPHONE;
    }
}

abstract class Clothing extends Item {
    Clothing(Inventory inventory) {
        super(inventory);
    }
}

class Hat extends Clothing {
    String hatSize;
    String hatSizes[] = {"XS", "S", "M", "L", "XL"};
    Hat(Inventory inventory) {
        super(inventory);
        hatSize = hatSizes[Utility.rndFromRange(0,hatSizes.length-1)];
        itemType = ItemType.HAT;
    }
}

class Shirt extends Clothing {
    String shirtSize;
    String shirtSizes[] = {"XS", "S", "M", "L", "XL"};
    Shirt(Inventory inventory) {
        super(inventory);
        shirtSize = shirtSizes[Utility.rndFromRange(0,shirtSizes.length-1)];
        itemType = ItemType.SHIRT;
    }
}

class Bandana extends Clothing {
    Bandana(Inventory inventory) {
        super(inventory);
        itemType = ItemType.BANDANA;
    }
}

abstract class Accessory extends Item {
    Accessory(Inventory inventory) {
        super(inventory);
    }
}

class PracticeAmp extends Accessory {
    int wattage;
    int wattages[] = {5, 10, 15, 20, 25, 30};
    PracticeAmp(Inventory inventory) {
        super(inventory);
        wattage = wattages[Utility.rndFromRange(0,wattages.length-1)];
        itemType = ItemType.PRACTICEAMP;
    }
}

class Cable extends Accessory {
    int length;
    int lengths[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Cable(Inventory inventory) {
        super(inventory);
        length = lengths[Utility.rndFromRange(0,lengths.length-1)];
        itemType = ItemType.CABLE;
    }
}

class Strings extends Accessory {
    String type;
    String[] types = {"Type1", "Type2"};
    Strings(Inventory inventory) {
        super(inventory);
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.STRINGS;
    }
}

class GigBag extends Accessory {
    GigBag(Inventory inventory) {
        super(inventory);
        itemType = ItemType.GIGBAG;
    }
}