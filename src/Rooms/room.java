package Rooms;

import Interaction.item;
import Interaction.obstacle;
import java.util.HashMap;

public abstract class room {
     String description; // a brief description o the current room
     item[] items;   // an array of the items in the current room - if later allow dropping may need to make a list but for now number of items per room is fixed
     obstacle[] obstacles;   // an array of all obstacles in the current room
     HashMap<item, obstacle> blockedBy;    // a map that shows what Interaction.obstacle blocks each Interaction.item (if any) - so the program knows if the user can approach them

    public room(String description, item[] items, obstacle[] obstacles, HashMap<item, obstacle> blockedBy){
        this.description = description;
        this.items = items;
        this.obstacles = obstacles;
        this.blockedBy = blockedBy;
    }

    public room(){
        
    }

    public item[] getItems() {
        return items;
    }

    public HashMap<item, obstacle> getBlockedBy() {
        return blockedBy;
    }
}
