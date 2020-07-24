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
     HashMap<String, item> itemIsToItem;
    final String TAKE_OBST_ERR_MSG = "Taking that might be a bit ambitious \n - you cannot pick up obstacles";
    final String TAKE_NULL_OBJ_ERR_MSG = "That object doesn't seem to exist \n - you may have done something VERY wrong, or it's a glitch";
    final String USED_OBST_WITH_OBST_ERR_MSG = "Maybe combining two obstacles isn't the way to clear the path \n - use items with obstacles not other obstacles";

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

    public String getDescription() {return description;}

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

    /*
        the next group of methods aren't really very necessary currently
        but could be useful later if interaction is expanded
     */
//-------------------------------------------------------------------------------------
    public void playerLooksAtItem(player p, item lookedAt) {
        lookedAt.lookAt();
    }

    public void playerTouchedItem(player p, item touched) {
        obstacle blockage = blockedBy.get(touched);
        if(blockage == null || blockage.getSolved()) touched.touch();
        else System.out.println("you go to touch the " + touched.getItemIs() + "but find yourself stopped by a " + blockage.getItemIs());
    }

    public void playerTastedItem(player p, item licked) {
        if(licked instanceof  obstacle)  licked.taste();
        if(p.hasItemInInventory(licked)) licked.taste();
        else System.out.println("If you're going to taste something at least be dignified and pick it up first");
    }

    public void playerUsedItem(player p, item used) {
        if(used instanceof obstacle) used.use();
        else if(p.hasItemInInventory(used)) used.use();
        else System.out.println("you suddenly remember that in order to use something you normally have to have it with you");
    }

    public void playerUsedItemOnObstacle(player p, item used, obstacle usedOn) { //obstacles currently can't be blocked
        if(used instanceof obstacle) System.out.println(USED_OBST_WITH_OBST_ERR_MSG);
        else if(p.hasItemInInventory(used)) used.useOn(usedOn);
        else System.out.println("you reach for your " + used.getItemIs() + " but realise you don't have one : (");
    }

}
