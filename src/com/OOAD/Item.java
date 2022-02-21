package com.OOAD;

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

    void damageAnItem(Item i) {

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

abstract class Player extends Item {
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
        itemType = ItemType.RecordPlayer;
    }
}

class MP3 extends Player {
    MP3() {
        super();
        itemType = ItemType.MP3;
    }
}

abstract class Instrument extends Item {
}

abstract class Stringed extends Instrument {
    boolean isElectric;
    Stringed() {
        super();
        isElectric = (Utility.rnd()>.5); // coin flip for electric or acoustic
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

abstract class Clothing extends Item {
}

class Hat extends Clothing {
    String hatSize;
    String hatSizes[] = {"XS", "S", "M", "L", "XL"};
    Hat() {
        super();
        hatSize = hatSizes[Utility.rndFromRange(0,hatSizes.length-1)];
        itemType = ItemType.Hat;
    }
}

class Shirt extends Clothing {
    String shirtSize;
    String shirtSizes[] = {"XS", "S", "M", "L", "XL"};
    Shirt() {
        super();
        shirtSize = shirtSizes[Utility.rndFromRange(0,shirtSizes.length-1)];
        itemType = ItemType.Shirt;
    }
}

class Bandana extends Clothing {
    Bandana() {
        super();
        itemType = ItemType.Bandana;
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
        itemType = ItemType.PracticeAmp;
    }
}

class Cable extends Accessory {
    int length;
    int lengths[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Cable() {
        super();
        length = lengths[Utility.rndFromRange(0,lengths.length-1)];
        itemType = ItemType.Cable;
    }
}

class Strings extends Accessory {
    String type;
    String[] types = {};
    Strings() {
        super();
        type = types[Utility.rndFromRange(0,types.length-1)];
        itemType = ItemType.Strings;
    }
}