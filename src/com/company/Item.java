package com.company;

public abstract class Item implements Logger {
    String name;            // I didn't implement a naming scheme - mostly ignoring this - how would you?
    double purchasePrice;   // $1 to $50
    double listPrice;       // purchasePrice x 2
    boolean isNew;          // set by constructor randomly
    int dayArriving;        // 0 at initialization, otherwise set at delivery
    Condition condition;    // set by constructor randomly
    double salePrice;       // set when sold
    int daySold;            // set when sold
    ItemType itemType;      // set by subclass constructors
    boolean tunable = false;

    void damageAnItem() {
        //first decrement the listPrice by 20%
        //then decrement the condition
        out("The broken item was a " + condition.toString().toLowerCase() + " condition " + itemType.toString().toLowerCase() + " with a list price of $" + listPrice);
        listPrice -= 0.2 * listPrice;
        condition.level -= 1;
        out("Condition has been reduced to " + condition.toString().toLowerCase() + " and the list price has been reduced 20% to $" + listPrice);
    }

    Item() {
        // common initialization of a new instance
        purchasePrice = Utility.rndFromRange(1,50);
        listPrice = 2 * purchasePrice;
        isNew = (Utility.rnd() > .5);  // coin flip for new or used
        dayArriving = 0;
        condition = Utility.randomEnum(Condition.class);
        salePrice = 0;
        daySold = 0;
    };
}

abstract class Music extends Item {
    String band;
    String album;
    String[] bands = {"Yes","Jethro Tull","Rush","Genesis","ELP","Enya"};
    String[] albums = {"Fragile","Stormwatch","2112","Abacab","Tarkus","The Memory of Trees"};
    Music() {
        super();
        band = bands[Utility.rndFromRange(0,bands.length-1)];
        album = albums[Utility.rndFromRange(0,albums.length-1)];
    }
}

class PaperScore extends Music {
    PaperScore() {
        super();
        itemType = ItemType.PAPERSCORE;
    }
}
class CD_M extends Music {
    CD_M() {
        super();
        itemType = ItemType.CD_M;
    }
}
class Vinyl extends Music {
    Vinyl() {
        super();
        itemType = ItemType.VINYL;
    }
}

class Cassette_M extends Music {
    Cassette_M() {
        super();
        itemType = ItemType.CASSETTE_M;
    }
}

abstract class TunableItem extends Item {
    abstract void setProperty(boolean result);
    abstract boolean getProperty();
}

abstract class Player extends TunableItem  {
    boolean equalized;
    Player() {
        super();
        equalized = false;
        tunable = true;
    }
    public boolean getProperty(){
        return equalized;
    }
    public void setProperty(boolean result){
        equalized = result;
    }
}

class CD_P extends Player {
    CD_P() {
        super();
        itemType = ItemType.CD_P;
    }
}

class RecordPlayer extends Player {
    RecordPlayer() {
        super();
        itemType = ItemType.RECORDPLAYER;
    }
}

class MP3 extends Player {
    MP3() {
        super();
        itemType = ItemType.MP3;
    }
}

class Cassette_P extends Player {
    Cassette_P() {
        super();
        itemType = ItemType.CASSETTE_P;
    }
}

abstract class Instrument extends TunableItem {
}

abstract class Stringed extends Instrument {
    boolean isElectric;
    boolean tuned;
    Stringed() {
        super();
        isElectric = (Utility.rnd()>.5); // coin flip for electric or acoustic
        tuned = false;
        tunable = true;
    }
    public boolean getProperty(){
        return tuned;
    }
    public void setProperty(boolean result){
        tuned = result;
    }
}

class Guitar extends Stringed {
    Guitar() {
        super();
        itemType = ItemType.GUITAR;
    }
}
class Bass extends Stringed {
    Bass() {
        super();
        itemType = ItemType.BASS;
    }
}
class Mandolin extends Stringed {
    Mandolin() {
        super();
        itemType = ItemType.MANDOLIN;
    }
}

abstract class Wind extends Instrument {
    boolean adjusted;
    Wind() {
        super();
        adjusted = false;
        tunable = true;
    }

    public boolean getProperty(){
        return adjusted;
    }
    public void setProperty(boolean result){
        adjusted = result;
    }
}

class Flute extends Wind {
    String type;
    String[] types = {"Piccolo","Alto","Bass","Tierce","Concert","Plastic"};
    Flute() {
        super();
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.FLUTE;
    }
}
class Harmonica extends Wind {
    String key;
    String keys[] = {"E","A","G","C","D"};
    Harmonica() {
        super();
        key = keys[Utility.rndFromRange(0,keys.length-1)];
        itemType = ItemType.HARMONICA;
    }
}

class Saxophone extends Wind {
    String type;
    String[] types = {"Type1", "Type2"};
    Saxophone() {
        super();
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.SAXOPHONE;
    }
}

abstract class Clothing extends Item {
}

class Hat extends Clothing {
    String hatSize;
    String hatSizes[] = {"XS", "S", "M", "L", "XL"};
    Hat() {
        super();
        hatSize = hatSizes[Utility.rndFromRange(0,hatSizes.length-1)];
        itemType = ItemType.HAT;
    }
}

class Shirt extends Clothing {
    String shirtSize;
    String shirtSizes[] = {"XS", "S", "M", "L", "XL"};
    Shirt() {
        super();
        shirtSize = shirtSizes[Utility.rndFromRange(0,shirtSizes.length-1)];
        itemType = ItemType.SHIRT;
    }
}

class Bandana extends Clothing {
    Bandana() {
        super();
        itemType = ItemType.BANDANA;
    }
}

abstract class Accessory extends Item {
}

class PracticeAmp extends Accessory {
    int wattage;
    int wattages[] = {5, 10, 15, 20, 25, 30};
    PracticeAmp() {
        super();
        wattage = wattages[Utility.rndFromRange(0,wattages.length-1)];
        itemType = ItemType.PRACTICEAMP;
    }
}

class Cable extends Accessory {
    int length;
    int lengths[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Cable() {
        super();
        length = lengths[Utility.rndFromRange(0,lengths.length-1)];
        itemType = ItemType.CABLE;
    }
}

class Strings extends Accessory {
    String type;
    String[] types = {"Type1", "Type2"};
    Strings() {
        super();
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.STRINGS;
    }
}

class GigBag extends Accessory {
    GigBag() {
        super();
        itemType = ItemType.GIGBAG;
    }
}