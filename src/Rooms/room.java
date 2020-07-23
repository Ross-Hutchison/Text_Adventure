package Rooms;

import Interaction.item;
import Interaction.obstacle;
import Game.player;
import java.util.HashMap;

public abstract class room {
     String description; // a brief description o the current room
     item[] items;   // an array of the items in the current room - if later allow dropping may need to make a list but for now number of items per room is fixed
     obstacle[] obstacles;   // an array of all obstacles in the current room
     HashMap<item, obstacle> blockedBy;    // a map that shows what Interaction.obstacle blocks each Interaction.item (if any) - so the program knows if the user can approach them
    final String TAKE_OBST_ERR_MSG = "Taking that might be a bit ambitious \n - you cannot pick up obstacles";
    final String TAKE_NULL_OBJ_ERR_MSG = "That object doesn't seem to exist \n - you may have done something VERY wrong, or it's a glitch";

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

    public void playerTakesItem(player p, item toTake) {
        if(toTake == null) System.out.println(TAKE_NULL_OBJ_ERR_MSG);
        else if(toTake instanceof obstacle) System.out.println(TAKE_OBST_ERR_MSG);
        else {
            boolean wasTaken = p.addToInventory(toTake);
            if(wasTaken) {
                for (int i = 0; i < this.items.length; i++) {
                    if(this.items[i] == toTake) this.items[i] = null;
                }
            }
        }
    }

    /*
        could have the check that the item is present in the room here
        would make the player message less complicated

        do later in separate branch
     */
    public void playerSwitchesItems(player p, item toLeave, item toTake){
        if(toTake == null) System.out.println(TAKE_NULL_OBJ_ERR_MSG);
        else if(toTake instanceof obstacle) System.out.println(TAKE_OBST_ERR_MSG);
        else {
            p.switchX_With_Y(toLeave, toTake, this); //the player method needs data on the room so it takes it as a parameter
        }
    }

}
