package com.company;

public interface TuneBehavior {
    void Tune(TunableItem item, Clerk clerk);
}
/*
Haphazard Tuning: The Clerk will have a 50% chance of changing the property (equalized, tuned,
or adjusted) by flipping the state (if it was true, it becomes false; if it was false, it becomes true).
o Manual Tuning: The Clerk will change the property from false to true 80% of the time, and from
true to false 20% of the time.
o Electronic Tuning: The Clerk will change the property from false to true automatically, and never
from true to false
 */
class HaphazardTune implements TuneBehavior{

    @Override
    public void Tune(TunableItem item, Clerk clerk) {
        //do a coin flip
        //if the coin is greater than 50% then change the
        boolean curr_property_state = item.getProperty();
        if(Utility.rnd() > 0.5){
            //then flip the property
            item.setProperty(!item.getProperty());
            if(!item.getProperty()){
                if(Utility.rnd() > 0.9){
                    clerk.damageItem(item);
                }
            }
        }
    }
}

class ManualTune implements TuneBehavior{

    @Override
    public void Tune(TunableItem item, Clerk clerk) {
        boolean curr_property_state = item.getProperty();
        if(!curr_property_state){
            //false to true 80% of the time
            if(Utility.rnd() > 0.2){
                item.setProperty(true);
            }
        }
        else{
            //go from true to false
            if(Utility.rnd() > 0.8){
                item.setProperty(false);
                if(Utility.rnd() > 0.9){
                    clerk.damageItem(item);
                }
            }
        }
    }
}

class ElectronicTune implements  TuneBehavior{

    @Override
    public void Tune(TunableItem item, Clerk clerk) {
        //only goes from false to true
        item.setProperty(true);
    }
}
