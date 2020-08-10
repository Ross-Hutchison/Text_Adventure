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
    final String ITEM_IS_STATIONARY_ERR_MSG = "Taking that might be a bit ambitious \n - you cannot pick up this object";
    final String USE_NULL_OBJ_ERR_MSG = "That object doesn't seem to exist \n - you may have done something VERY wrong, or it's a glitch";
    final String USED_OBST_WITH_OBST_ERR_MSG = "Maybe combining two obstacles isn't the way to clear the path \n - use items with obstacles not other obstacles";
    final String USED_ITEM_WITH_ITEM_ERR_MSG = "nothing happens \n - use items on obstacles not on other items";
    final String ITEM_IS_INVIS_ERR_MSG = "you cannot see an item of that description in the room so interacting with one would be a bit hard";
    int originalItemCount;
    String id;

    public room(String id, String description, interactive[] interactives, obstacle[] obstacles, HashMap<interactive, obstacle> blockedBy, HashMap<String, interactive> itemIsToItem,
                HashMap<String, obstacle> itemIsToObstacle, int originalItemCount) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public HashMap<interactive, obstacle> getBlockedBy() {
        return blockedBy;
    }

    public HashMap<String, interactive> getItemIsToItem() {
        return itemIsToItem;
    }

    public HashMap<String, obstacle> getItemIsToObstacle() {
        return itemIsToObstacle;
    }

    public int getOriginalItemCount() {
        return originalItemCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() { return id; }

    public boolean playerTakesItem(player p, interactive toTake) {
        if (toTake == null) {
            System.out.println(USE_NULL_OBJ_ERR_MSG);
            return false;
        }

        if (!toTake.getVisible()) {
            System.out.println(ITEM_IS_INVIS_ERR_MSG);
            return false;
        } else if (!toTake.getCanTake()) { // if item is not able to be taken
            System.out.println(ITEM_IS_STATIONARY_ERR_MSG);
            return false;
        }
        obstacle blockage = this.getBlockedBy().get(toTake);
        if (blockage == null || blockage.getSolved()) {  // if item is unblocked
            boolean wasTaken = p.addToInventory(toTake);
            if (wasTaken) {
                for (int i = 0; i < this.interactives.length; i++) {
                    if (this.interactives[i] == toTake)
                        this.interactives[i] = null;
                }
                this.itemIsToItem.remove(toTake.getItemIs());   // removes the item from the room's map of
            }
            return wasTaken;
        } else
            System.out.println("you try to take the " + toTake.getItemIs() + " but the " + blockage.getDescription() + "blocks it");
        return false;
    }

    /*
        could have the check that the item is present in the room here
        would make the player message less complicated

        do later in separate branch
     */
    public boolean playerSwitchesItems(player p, interactive toLeave, interactive toTake) {
        if (toTake == null) {
            System.out.println(USE_NULL_OBJ_ERR_MSG);
            return false;
        } else if (!toTake.getCanTake()) {  // if second item cannot be taken
            System.out.println(ITEM_IS_STATIONARY_ERR_MSG);
            return false;
        }
        else if (!toTake.getVisible()) {
            System.out.println(ITEM_IS_INVIS_ERR_MSG);
            return false;
        }
        obstacle blockage = this.getBlockedBy().get(toTake);
        boolean isNotBlocked = (blockage == null || blockage.getSolved());
        boolean hasInInventory = (p.hasItemInInventory(toLeave.getItemIs()) != null);

        if (isNotBlocked && hasInInventory) {
            return p.switchX_With_Y(toLeave, toTake, this); //the player method needs data on the room so it takes it as a parameter
        } else if (!isNotBlocked) {
            System.out.println("you go to take the " + toTake + "but the " + blockage.getItemIs() + "stops you");
            return false;
        } else {
            System.out.println("you go to leave the " + toLeave + " but you realise you don't actually have it");
            return false;
        }
    }

    public void playerLooksAtItem(player p, interactive lookedAt) {
        if (!lookedAt.getVisible()) {
            System.out.println(ITEM_IS_INVIS_ERR_MSG);
        } else lookedAt.lookAt();
    }

    public String playerTouchedItem(player p, interactive touched) {
        if (touched == null) {
            System.out.println(USE_NULL_OBJ_ERR_MSG);
            return null;
        } else if (!touched.getVisible()) {
            System.out.println(ITEM_IS_INVIS_ERR_MSG);
            return null;
        } else {
            obstacle blockage = blockedBy.get(touched);
            if (blockage == null || blockage.getSolved()) {
                return touched.touch();
            } else {
                System.out.println("you go to touch the " + touched.getItemIs() + "but find yourself stopped by a " + blockage.getItemIs());
                return null;
            }
        }
    }

    public String playerUsedItem(player p, interactive used) {
        if(used == null) {
            System.out.println(USE_NULL_OBJ_ERR_MSG);
            return null;
        }
        else if(!used.getVisible()) {
            System.out.println(ITEM_IS_INVIS_ERR_MSG);
            return null;
        }
        else if (!used.getCanTake()) return used.use();
        else if (p.hasItemInInventory(used.getItemIs()) != null)
            return used.use();
        else {
            System.out.println("you suddenly remember that in order to use something you normally have to have it with you");
            return null;
        }
    }

    public void playerUsedItemOnObstacle(player p, interactive used, obstacle usedOn) { //obstacles currently can't be blocked
        if(!usedOn.getVisible()) {
            System.out.println(ITEM_IS_INVIS_ERR_MSG);
        }
        else if (used instanceof obstacle)
            System.out.println(USED_OBST_WITH_OBST_ERR_MSG);
        else if (itemIsToItem.get(usedOn.getItemIs()) != null)
            System.out.println(USED_ITEM_WITH_ITEM_ERR_MSG);
        else if (p.hasItemInInventory(used.getItemIs()) != null)
            used.useOn(usedOn);
        else
            System.out.println("you reach for your " + used.getItemIs() + " but realise you don't have one : (");
    }

    public void removeItemFromDescription(interactive toRemove) {
        this.description = this.description.replaceFirst(toRemove.getItemIs(), "an empty space");
    }

    public void addItemToDescription(interactive toAdd) {
        this.description = this.description.replaceFirst("an empty space", toAdd.getItemIs());
    }

}
