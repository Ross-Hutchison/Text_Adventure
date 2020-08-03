package Rooms;

import Interaction.interactive;
import Interaction.obstacle;
import Game.player;
import java.util.HashMap;

public
class room {
     String description; // a brief description o the current room
     interactive[] interactives;   // an array of the items in the current room - if later allow dropping may need to make a list but for now number of items per room is fixed
     obstacle[] obstacles;   // an array of all obstacles in the current room
     HashMap<interactive, obstacle> blockedBy;    // a map that shows what Interaction.obstacle blocks each Interaction.item (if any) - so the program knows if the user can approach them
     HashMap<String, interactive> itemIsToItem;
     HashMap<String, obstacle> itemIsToObstacle;
    final String TAKE_OBST_ERR_MSG = "Taking that might be a bit ambitious \n - you cannot pick up obstacles";
    final String TAKE_NULL_OBJ_ERR_MSG = "That object doesn't seem to exist \n - you may have done something VERY wrong, or it's a glitch";
    final String USED_OBST_WITH_OBST_ERR_MSG = "Maybe combining two obstacles isn't the way to clear the path \n - use items with obstacles not other obstacles";
    final String USED_ITEM_WITH_ITEM_ERR_MSG = "nothing happens \n - use items on obstacles not on other items";
    int originalItemCount;

    public room(String description, interactive[] interactives, obstacle[] obstacles, HashMap<interactive, obstacle> blockedBy, HashMap<String, interactive> itemIsToItem,
                HashMap<String, obstacle> itemIsToObstacle, int originalItemCount){
        this.description = description;
        this.interactives = interactives;
        this.obstacles = obstacles;
        this.blockedBy = blockedBy;
        this.itemIsToItem = itemIsToItem;
        this.itemIsToObstacle = itemIsToObstacle;
        this.originalItemCount = originalItemCount;
    }

    public interactive[] getInteractives() {
        return interactives;
    }

    public String getDescription() {return description;}

    public HashMap<interactive, obstacle> getBlockedBy() {
        return blockedBy;
    }

    public HashMap<String, interactive> getItemIsToItem() { return itemIsToItem;}

    public HashMap<String, obstacle> getItemIsToObstacle() { return itemIsToObstacle; }

    public int getOriginalItemCount() { return originalItemCount; }

    public void setDescription(String description) { this.description = description; }

    public boolean playerTakesItem(player p, interactive toTake) {
        if(toTake == null) {
            System.out.println(TAKE_NULL_OBJ_ERR_MSG);
            return false;
        }
        else if(toTake instanceof obstacle) {
            System.out.println(TAKE_OBST_ERR_MSG);
            return false;
        }
        else {
            boolean wasTaken = p.addToInventory(toTake);
            if(wasTaken) {
                for (int i = 0; i < this.interactives.length; i++) {
                    if(this.interactives[i] == toTake) this.interactives[i] = null;
                }
                this.itemIsToItem.remove(toTake.getItemIs());   // removes the item from the room's map of
            }
            return wasTaken;
        }
    }

    /*
        could have the check that the item is present in the room here
        would make the player message less complicated

        do later in separate branch
     */
    public boolean playerSwitchesItems(player p, interactive toLeave, interactive toTake){
        if(toTake == null){
            System.out.println(TAKE_NULL_OBJ_ERR_MSG);
            return false;
        }
        else if(toTake instanceof obstacle) {
            System.out.println(TAKE_OBST_ERR_MSG);
            return false;
        }
        else {
            return p.switchX_With_Y(toLeave, toTake, this); //the player method needs data on the room so it takes it as a parameter

        }
    }

    public void playerLooksAtItem(player p, interactive lookedAt) {
        lookedAt.lookAt();
    }

    public String playerTouchedItem(player p, interactive touched) {
        obstacle blockage = blockedBy.get(touched);
        if(blockage == null || blockage.getSolved()) return touched.touch();
        else {
            System.out.println("you go to touch the " + touched.getItemIs() + "but find yourself stopped by a " + blockage.getItemIs());
            return null;
        }
    }

    public String playerTastedItem(player p, interactive licked) {
        if(licked instanceof  obstacle)  return licked.taste();
        else if(p.hasItemInInventory(licked.getItemIs()) != null) return licked.taste();
        else {
            System.out.println("If you're going to taste something at least be dignified and pick it up first");
            return null;
        }
    }

    public String playerUsedItem(player p, interactive used) {
        if(used instanceof obstacle) return used.use();
        else if(p.hasItemInInventory(used.getItemIs()) != null) return used.use();
        else {
            System.out.println("you suddenly remember that in order to use something you normally have to have it with you");
            return null;
        }
    }

    public void playerUsedItemOnObstacle(player p, interactive used, obstacle usedOn) { //obstacles currently can't be blocked
        System.out.println(usedOn);
        if(used instanceof obstacle) System.out.println(USED_OBST_WITH_OBST_ERR_MSG);
        else if(itemIsToItem.get(usedOn.getItemIs()) != null) System.out.println(USED_ITEM_WITH_ITEM_ERR_MSG);
        else if(p.hasItemInInventory(used.getItemIs()) != null) used.useOn(usedOn);
        else System.out.println("you reach for your " + used.getItemIs() + " but realise you don't have one : (");
    }

    public void removeItemFromDescription(interactive toRemove) {
        this.description = this.description.replaceFirst(toRemove.getItemIs(), "an empty space");
    }

    public void addItemToDescription(interactive toAdd) {
        this.description = this.description.replaceFirst("an empty space", toAdd.getItemIs());
    }

}
