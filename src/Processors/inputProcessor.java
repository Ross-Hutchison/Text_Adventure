package Processors;

import Events.event;
import Interaction.interactive;
import Interaction.obstacle;
import Game.game;
import Game.player;
import Rooms.room;

import java.util.HashMap;

public class inputProcessor {

    private String verbs;
    private String verbs_2;
    private final int itemVerbItem_requiredItems = 2;
    private final int verbItem_requiredItems = 1;
    private final String REPEATED_VERBS_ERR_MSG = "this is not a valid command - please do not include multiple verbs in one command";
    final String USED_ITEM_WITH_ITEM_ERR_MSG = "nothing happens \n - use items on obstacles not on other items";

    public void setUpInputProcessor(game game) {
        verbs = game.getVerbs();
        verbs_2 = game.getVerbs_2();
    }

    /*
        splits input by space taking the first as the verb
        then takes it and uses it to isolate the item's itemIs descriptor
        this is used to check the item is present in the current room
        before being used to fetch the desired item object

        the verb is then checked to determine which function is carried out
     */
    public event processVerbObject(String input, room currentRoom, player p) {
        String[] parts = input.split(" "); //separate the input by ' '
        String verb = parts[0]; // takes the verb
        String item = input.substring(verb.length() + 1); // uses the verb's length to take the set of words
        String encasedItem;

        if (item.contains(":")) {
            encasedItem = item.substring(0, item.indexOf(":") + 1) + "\"" + item.substring(item.indexOf(":") + 1) + "\"";
        } else encasedItem = "\"" + item + "\"";

        if (checkItemForVerb(encasedItem)) {   // check the item does not contain more verbs since this is invalid
            System.out.println(REPEATED_VERBS_ERR_MSG);
            return null;
        }

        interactive interactiveObj;
        HashMap<String, interactive> itemChecker = currentRoom.getItemIsToItem();   // lets you check for item's being present
        HashMap<String, obstacle> obstacleChecker = currentRoom.getItemIsToObstacle();  // lets you check for obstacles being present

        if (itemChecker.containsKey(encasedItem)) {    // desired interactive is an item present in the room
            interactiveObj = itemChecker.get(encasedItem);
        } else if (obstacleChecker.containsKey(encasedItem)) {   // desired interactive is an obstacle present in the room
            interactiveObj = obstacleChecker.get(encasedItem);
        } else
            interactiveObj = p.hasItemInInventory(encasedItem);  // desired interactive is a takable interactive in the player's inventory

        if (interactiveObj != null) {   // if the item was found in the room or player's inventory
            switch (verb) {
                case "take":
                    boolean actionSucceeded;
                    actionSucceeded = currentRoom.playerTakesItem(p, interactiveObj);   // no game event currently attached - ret null
                    if (actionSucceeded) {
                        currentRoom.removeItemFromDescription(interactiveObj); // alters description only if the take is successful
                    }
                    return null;
                case "touch":
                    return currentRoom.playerTouchedItem(p, interactiveObj);
                case "lookAt":
                    currentRoom.playerLooksAtItem(p, interactiveObj); // no game event currently attached - ret null
                    return null;
                case "use":
                    return currentRoom.playerUsedItem(p, interactiveObj);
            }
        } else
            System.out.println("you turn your attention to the " + encasedItem + "\n" +
                    "but you must have hallucinated it\n" +
                    "- this item is not currently present in your inventory or the current room");
        return null;
    }

    /*
       returns null when the action does not lead to a game event such as winning
    */
    public event processItemVerbItem(String input, room currentRoom, player p) {
        // checking the contents of the input
        String[] verbs_2Arr = verbs_2.split("\\|"); // array of verbs for: "item verb item" format
        String splitterVerb = null;

        for (String verb : verbs_2Arr) {
            verb = " " + verb + " ";    // looking for the verb outside of a word and not at the start or end of the input
            int verbFirstIndex = input.indexOf(verb);
            if (verbFirstIndex == -1) {
                continue;
            } // move to the next verb since current is not present

            int verbLastIndex = input.lastIndexOf(verb);
            if (verbFirstIndex != verbLastIndex) {   // there are more than one instances of the verb making it an invalid command
                System.out.println(REPEATED_VERBS_ERR_MSG);
                break;
            } else {    // there is only one of the verb and it is in-between two sets of words this can be used to split
                splitterVerb = verb;
                break;  // only one splitter verb in a valid input, no need to look further
            }
        }

        if (splitterVerb == null) {
            System.out.println("this command is invalid");
            return null;
        }

        String[] interactives = input.split(splitterVerb); //

        for (int i = 0; i < itemVerbItem_requiredItems; i++) {    // surround both items with the "" needed for processing
            String current = interactives[i];
            if(current.contains(":")) {
                interactives[i] = current.substring(0, current.indexOf(":") + 1) + "\"" + current.substring(current.indexOf(":") + 1) + "\"";
            }
            else interactives[i] = "\"" + current + "\"";

            if (checkItemForVerb(current)) {    // check each item is free of any other verbs
                System.out.println(REPEATED_VERBS_ERR_MSG);
                return null;
            }
        }


        // there is a splitter verb and two itemIs that contain no verbs now to find what method is carried out
        HashMap<String, interactive> itemChecker = currentRoom.getItemIsToItem();   //lets game check for items
        HashMap<String, obstacle> obstacleChecker = currentRoom.getItemIsToObstacle();  // lets game check for obstacles

        interactive interactiveObj1;

        if (itemChecker.containsKey(interactives[0])) { // if the first item is in the room
            interactiveObj1 = itemChecker.get(interactives[0]);
        } else
            interactiveObj1 = p.hasItemInInventory(interactives[0]);  // or in the player's inventory

        if (interactiveObj1 != null) {  // then move on to next step
            splitterVerb = splitterVerb.strip();    // take the spaces off the end of the splitter verb

            if (splitterVerb.equals("switchWith")) {    // if the command was to switch two items
                interactive interactiveObj2;
                boolean actionSucceeded;


                interactiveObj2 = itemChecker.get(interactives[1]); // if second Interaction is an item in the room
                if (interactiveObj2 == null)
                    interactiveObj2 = p.hasItemInInventory(interactives[1]);   // or in the player's inventory

                if (interactiveObj2 != null) {  // item is present
                    // switches the two items
                    interactiveObj1.addNumber(currentRoom.getItemIsToItem());
                    actionSucceeded = currentRoom.playerSwitchesItems(p, interactiveObj1, interactiveObj2);    // no event attached - ret null

                    if (actionSucceeded) {   //alters the description if two items were switched
                        currentRoom.addItemToDescription(interactiveObj1);
                        currentRoom.removeItemFromDescription(interactiveObj2);
                        interactiveObj2.removeNumber();
                    }

                    return null;
                } else if (obstacleChecker.get(interactives[1]) != null)
                    System.out.println("you can't take that with you \n - you cannot pick up objects");
                System.out.println("the " + interactives[1] + " is not present");

            } else if (splitterVerb.equals("useOn")) {  // if the command was to use one item on an obstacle

                obstacle obstacleObj = obstacleChecker.get(interactives[1]);  // look for the second desired Interaction in the rooms obstacle map

                if (obstacleObj != null) {  // if the second Interaction is an obstacle in the room
                    currentRoom.playerUsedItemOnObstacle(p, interactiveObj1, obstacleObj); // no event attached - ret null
                    return null;
                } else {
                    // if the second interaction is an item in the room or the player's inventory
                    if (itemChecker.get(interactives[1]) != null || p.hasItemInInventory(interactives[1]) != null) {
                        System.out.println(USED_ITEM_WITH_ITEM_ERR_MSG);
                    } else    // otherwise item just isn't there
                        System.out.println("the " + interactives[1] + " is not present");
                    return null;
                }
            }
        }
        System.out.println("the " + interactives[0] + "is not present in the room");
        return null; // if first item is not present then no operation occurs
    }

    private boolean checkItemForVerb(String item) {
        String[] verbsArr = verbs.split("\\|"); // array of verbs for: "verb item" format
        String[] verbs_2Arr = verbs_2.split("\\|"); // array of verbs for: "item verb item" format
        // adds a space to the start and removes the "" surrounding it so that the below check will work
        String modifiedItem = " " + item.substring(1, item.length() - 1);
        for (String verb : verbsArr) {
            if (modifiedItem.contains(" " + verb)) return true;
        }
        for (String verb : verbs_2Arr) {
            if (modifiedItem.contains(" " + verb)) return true;
        }
        return false;
    }
}
