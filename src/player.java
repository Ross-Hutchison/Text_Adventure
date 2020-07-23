import Interaction.item;
import Interaction.obstacle;
import Rooms.room;

import java.util.HashMap;

public class player {
    private int inventorySize = 5;
    private int currentlyHolding = 0;
    private item[] inventory = new item[inventorySize];

    public boolean addToInventory(item toAdd) {
        if(currentlyHolding != inventorySize) {
            inventory[currentlyHolding++] = toAdd;
            return true;
        }
        else return false;
    }

    /*
        Goes through the player's inventory and checks for the Interaction.item the player wants to
        leave then goes through the room's array of items to check that the wanted Interaction.item
        is present in it. if both these checks match check if the Interaction.item can be reached
        if all checks pass then switch the inventory and room items
     */
    public void switchX_With_Y(item toLeave, item toTake, room whereToLeave) {
        boolean has_X = false;
        boolean Y_present = false;
        int inventoryIndex = 0;
        int roomIndex = 0;

        for (int i = 0; i < inventory.length; i++) {    // check the player has the Interaction.item they want to leave
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
            item[] roomHas = whereToLeave.getItems();
            for (int i = 0; i < roomHas.length; i++) {  // check that the desired Interaction.item is present in the room (should always be but best to be safe)
                if (roomHas[i] == toTake) {
                    Y_present = true;
                    roomIndex = i;
                    break;
                }
            }
            if(!Y_present) {
                System.out.println(toTake.getItemIs() + " is not present");
            }
            else {
                HashMap<item, obstacle> blockMap = whereToLeave.getBlockedBy();
                obstacle blockage = blockMap.get(toTake);

                if(blockage == null || blockage.getSolved()) {  // if Interaction.item is unblocked
                    item temp = inventory[inventoryIndex];
                    inventory[inventoryIndex] = roomHas[roomIndex];
                    roomHas[roomIndex] = temp;
                }
                else {
                    System.out.println("Cannot reach the " + toTake.getItemIs() + ", it is blocked by " + blockage.getItemIs());
                }
            }
        }
    }
}
