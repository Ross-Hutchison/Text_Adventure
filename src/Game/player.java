package Game;

import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

import java.util.HashMap;

public class player {
    private int inventorySize = 1;
    private int currentlyHolding = 0;
    private interactive[] inventory = new interactive[inventorySize];

    /*
        returns the desired item if it is part of the player's inventory
        or null if it is not
     */
    public interactive hasItemInInventory(String checkFor) {
        for (interactive current : inventory) {
            if(current != null && current.getItemIs().equals(checkFor)) return current;
        }
        return null;
    }

    public boolean addToInventory(interactive toAdd) {
        if(currentlyHolding != inventorySize) {
            inventory[currentlyHolding++] = toAdd;
            System.out.println("picked up " + toAdd.getItemIs());
            return true;
        }
        else {
            System.out.println("Inventory is full");
            return false;
        }
    }

    /*
        Goes through the Game.player's inventory and checks for the Interaction.item the Game.player wants to
        leave then goes through the room's array of items to check that the wanted Interaction.item
        is present in it. if both these checks match check if the Interaction.item can be reached
        if all checks pass then switch the inventory and room items
     */
    public boolean switchX_With_Y(interactive toLeave, interactive toTake, room whereToLeave) {
        boolean has_X = false;
        boolean Y_present = false;
        int inventoryIndex = 0;
        int roomIndex = 0;

        for (int i = 0; i < inventory.length; i++) {    // check the Game.player has the Interaction.item they want to leave
            if (inventory[i] == toLeave) {
                has_X = true;
                inventoryIndex = i;
                break;
            }
        }
        if(!has_X) {
            System.out.println("You do not have " + toLeave.getItemIs());
        }
        else {
            interactive[] roomHas = whereToLeave.getInteractives();
            for (int i = 0; i < roomHas.length; i++) {  // check that the desired Interaction.item is present in the room (should always be but best to be safe)
                if (roomHas[i] == toTake) {
                    Y_present = true;
                    roomIndex = i;
                    break;
                }
            }
            if(!Y_present) {
                if(hasItemInInventory(toTake.getItemIs()) != null ) {
                    System.out.println("you already have the " + toTake.getItemIs());
                }
                else System.out.println(toTake.getItemIs() + " is not present");
            }
            else {
                HashMap<interactive, obstacle> blockMap = whereToLeave.getBlockedBy();
                obstacle blockage = blockMap.get(toTake);

                if(blockage == null || blockage.getSolved()) {  // if Interaction.item is unblocked
                    // switches the item in inventory with the item in the room's item array
                    interactive temp = inventory[inventoryIndex];
                    inventory[inventoryIndex] = roomHas[roomIndex];
                    roomHas[roomIndex] = temp;
                    // removes the key in the room's String to item HM for the old item and adds one for the new item
                    whereToLeave.getItemIsToItem().remove(toTake.getItemIs());
                    whereToLeave.getItemIsToItem().put(toLeave.getItemIs(), toLeave);

                    System.out.println("switched the " + toLeave.getItemIs() + " with  the " + toTake.getItemIs());
                    return true;
                }
                else {
                    System.out.println("Cannot reach the " + toTake.getItemIs() + ", it is blocked by " + blockage.getItemIs());
                }
            }
        }
        return false;
    }
}
