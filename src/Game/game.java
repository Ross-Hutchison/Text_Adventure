package Game;

import Factories.roomFactory;
import Rooms.*;
import Interaction.interactive;
import Interaction.obstacle;

import java.util.HashMap;
import java.util.regex.*;

public class game {

    private roomFactory roomGenerator = new roomFactory();

    private boolean gameEnd;
    private String endMsg;
    private room currentRoom;
    private player Jo;

    private final int itemVerbItem_requiredItems = 2;
    private final int verbItem_requiredItems = 1;
    private final String INVALID_COMMAND_ERR_MSG = "this is not a valid command - please look at the format paragraph";
    private final String REPEATED_VERBS_ERR_MSG = "this is not a valid command - please do not include multiple verbs in one command";
    private final String TOO_MANY_ITEMS_ERR_MSG = "this is not a valid command - there are more than two items included";
    final String USED_ITEM_WITH_ITEM_ERR_MSG = "nothing happens \n - use items on obstacles not on other items";
    private final String verbs = "lookAt|touch|take|use|taste";
    private final String verbs_2 = "switchWith|useOn";
    private Pattern verbObjectPattern = Pattern.compile("^(" + verbs + ")( [a-zA-Z]+)+$");
    private Pattern itemVerbItemPattern = Pattern.compile("^[a-zA-Z]+( [a-zA-Z]+)+ (" + verbs_2 + ")( [a-zA-Z]+)+$");

    public void outputCommandFormats() {
        System.out.println("--------------------");
        System.out.println("Verbs: lookAt, touch, take, use, taste, useOn, switchWith");
        System.out.println("Items and Obstacles will be highlighted in text using \"\" use the exact encased text when referring to them");
        System.out.println("e.g. \"big key\" \"door\" - lookAt big key  ");
        System.out.println("Verb formats: \"lookAt [item name]\" \n" +
                "\"touch [item name]\"  etc.\n" +
                "two exceptions\n" +
                "\"[item name] useOn [obstacle name]\"\n" +
                "\"[item name] switchWith [item name]\"");
        System.out.println("--------------------");
    }

    public void outputIntroPara() {
        System.out.println("hello and welcome to - name TBA - \n " +
                "this is a text adventure meaning all control of the player character is done through text input of instructions\n");
    }

    public void outputGameStartPara() {
        System.out.println("you wake up on the floor in a dusty room\n" +
                "You don't remember how you ended up here, but if it was important you probably would so it's probably fine\n" +
                "though while that's the case, it's probably best to try leave the room, you don't have to, but i'd appreciate it");
    }

    /*
        before any input is processed
        need to generate the tutorial room
        and any other
     */
    public void setUpGame() {
        currentRoom = roomGenerator.createTutorialRoom();
        gameEnd = false;
        endMsg = "no end message yet game is only beginning";
        Jo = new player(); // the canonical name of the PC is Jo

        System.out.println(currentRoom.getDescription());
        System.out.println("--------------------\n");
    }

    public void turn(String input) {
        processInput(input);
        System.out.println("\n" + currentRoom.getDescription());
    }

    private void processInput(String input) {
        Matcher verbObjMatcher = verbObjectPattern.matcher(input);
        String event = null;
        if (verbObjMatcher.matches()) event = processVerbObject(input);
        else {
            Matcher itemVerbItemMatcher = itemVerbItemPattern.matcher(input);
            if (itemVerbItemMatcher.matches())
                event = processItemVerbItem(input);
            else {
                System.out.println(INVALID_COMMAND_ERR_MSG);
            }
        }
        processEvent(event);
    }

    private void processEvent(String event) {
        if (event != null) {
            String[] parts = event.split("-");
            if (parts.length == 5) { //valid format is eventType-itemCausingIt-additionalInformation-interactionType-usesLeft
                String type = parts[0];
                String cause = parts[1];
                String additionalInfo = parts[2];
                String interactionType = parts[3];
                String usesLeft = parts[4];

                switch (type) {
                    case "winGame":
                        gameEnd = true;
                        endMsg = additionalInfo;
                        break;
                    case "revealItem":
                        String desc = currentRoom.getDescription();
                        desc = desc.concat("\nThe " + cause + " revealed " + additionalInfo);
                        currentRoom.setDescription(desc);
                        break;
                    case "outputMessage":
                        System.out.println(additionalInfo);
                        break;
                    case "usedUp":
                        switch (interactionType) {  // there are different ways an item can be used up
                            case "touchResult":
                                System.out.println("poking the " + cause + "no longer does anything");
                                break;
                            case "tasteResult":
                                System.out.println("you've finished the " + cause + " there is nothing left to eat");
                                break;
                            case "useResult":
                                System.out.println("the " + cause + " has ceased to function");
                                break;
                            default:
                                System.out.println("event- " + event + "\n Has an invalid interactionType");

                        }
                        return; // if the event is used up no need to process further
                    default:
                        System.out.println("invalid event flag type:\n" + event);
                        break;
                }

                interactive toAlter;
                HashMap<String, interactive> items = currentRoom.getItemIsToItem();
                toAlter = items.get(cause);
                if (toAlter == null) toAlter = Jo.hasItemInInventory(cause);

                if (toAlter == null) {
                    System.out.println("something went horribly wrong with reducing the uses for event:\n" + event);
                    return;
                }

                int remainingUses = Integer.parseInt(usesLeft); // decreases uses left and reconverts it to a String
                remainingUses--;
                usesLeft = Integer.toString(remainingUses);

                String usedUpFlag = "usedUp-" + cause + "-" + additionalInfo + "-" + interactionType + "-" + usesLeft;   // the event is out of uses
                String eventFlag = type + "-" + cause + "-" + additionalInfo + "-" + interactionType + "-" + usesLeft;  // there are still uses

                if (usesLeft.equals("0")) {
                    switch (interactionType) {
                        case "touchResult":
                            toAlter.setTouchResult(usedUpFlag);
                            break;
                        case "tasteResult":
                            toAlter.setTasteResult(usedUpFlag);
                            break;
                        case "useResult":
                            toAlter.setUseResult(usedUpFlag);
                            break;
                    }
                } else {  // adds the decreased count by resetting the message
                    switch (interactionType) {
                        case "touchResult":
                            toAlter.setTouchResult(eventFlag);
                            break;
                        case "tasteResult":
                            toAlter.setTasteResult(eventFlag);
                            break;
                        case "useResult":
                            toAlter.setUseResult(eventFlag);
                            break;
                    }
                }
            } else {
                System.out.println("invalid event flag format");
            }
        }
    }

    /*
        returns null when the action does not lead to a game event such as winning
     */
    private String processItemVerbItem(String input) {
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
            interactives[i] = "\"" + interactives[i] + "\"";
            if (checkItemForVerb(interactives[i])) {    // check each item is free of any other verbs
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
            interactiveObj1 = Jo.hasItemInInventory(interactives[0]);  // or in the player's inventory

        if (interactiveObj1 != null) {  // then move on to next step
            splitterVerb = splitterVerb.strip();    // take the spaces off the end of the splitter verb

            if (splitterVerb.equals("switchWith")) {    // if the command was to switch two items
                interactive interactiveObj2;
                boolean actionSucceeded;


                interactiveObj2 = itemChecker.get(interactives[1]); // if second Interaction is an item in the room
                if(interactiveObj2 == null) interactiveObj2 = Jo.hasItemInInventory(interactives[1]);   // or in the player's inventory

                if (interactiveObj2 != null) {  // item is present
                    actionSucceeded = currentRoom.playerSwitchesItems(Jo, interactiveObj1, interactiveObj2);    // no event attached - ret null

                    if (actionSucceeded) {   //alters the description if two items were switched
                        currentRoom.addItemToDescription(interactiveObj1);
                        currentRoom.removeItemFromDescription(interactiveObj2);
                    }

                    return null;
                } else if (obstacleChecker.get(interactives[1]) != null)
                    System.out.println("you can't take that with you \n - you cannot pick up objects");
                System.out.println("the " + interactives[1] + " is not present");

            } else if (splitterVerb.equals("useOn")) {  // if the command was to use one item on an obstacle

                obstacle obstacleObj = obstacleChecker.get(interactives[1]);  // look for the second desired Interaction in the rooms obstacle map

                if (obstacleObj != null) {  // if the second Interaction is an obstacle in the room
                    currentRoom.playerUsedItemOnObstacle(Jo, interactiveObj1, obstacleObj); // no event attached - ret null
                    return null;
                } else {
                    // if the second interaction is an item in the room or the player's inventory
                    if (itemChecker.get(interactives[1]) != null || Jo.hasItemInInventory(interactives[1]) != null) {
                        System.out.println(USED_ITEM_WITH_ITEM_ERR_MSG);
                    }
                    else    // otherwise item just isn't there
                        System.out.println("the " + interactives[1] + " is not present");
                    return null;
                }
            }
        }
        return null; // if first item is not present then no operation occurs
    }


    /*
        splits input by space taking the first as the verb
        then takes it and uses it to isolate the item's itemIs descriptor
        this is used to check the item is present in the current room
        before being used to fetch the desired item object

        the verb is then checked to determine which function is carried out
     */
    private String processVerbObject(String input) {
        String[] parts = input.split(" "); //separate the input by ' '
        String verb = parts[0]; // takes the verb
        String item = "\"" + input.substring(verb.length() + 1) + "\"";  // uses the verb's length to take the set of words

        if (checkItemForVerb(item)) {   // check the item does not contain more verbs since this is invalid
            System.out.println(REPEATED_VERBS_ERR_MSG);
            return null;
        }

        interactive interactiveObj;
        HashMap<String, interactive> itemChecker = currentRoom.getItemIsToItem();   // lets you check for item's being present
        HashMap<String, obstacle> obstacleChecker = currentRoom.getItemIsToObstacle();  // lets you check for obstacles being present

        if (itemChecker.containsKey(item)) {    // desired interactive is an item present in the room
            interactiveObj = itemChecker.get(item);
        }
        else if (obstacleChecker.containsKey(item)) {   // desired interactive is an obstacle present in the room
            interactiveObj = obstacleChecker.get(item);
        }
        else interactiveObj = Jo.hasItemInInventory(item);  // desired interactive is a takable interactive in the player's inventory

        if (interactiveObj != null) {   // if the item was found in the room or player's inventory
            switch (verb) {
                case "take":
                    boolean actionSucceeded;
                    actionSucceeded = currentRoom.playerTakesItem(Jo, interactiveObj);   // no game event currently attached - ret null
                    if (actionSucceeded)
                        currentRoom.removeItemFromDescription(interactiveObj); // alters description only if the take is successful
                    return null;
                case "touch":
                    return currentRoom.playerTouchedItem(Jo, interactiveObj);
                case "lookAt":
                    currentRoom.playerLooksAtItem(Jo, interactiveObj); // no game event currently attached - ret null
                    return null;
                case "use":
                    return currentRoom.playerUsedItem(Jo, interactiveObj);
                case "taste":
                    return currentRoom.playerTastedItem(Jo, interactiveObj);
            }
        } else
            System.out.println("you turn your attention to the " + item + "\n" +
                    "but you must have hallucinated it\n" +
                    "- this item is not currently present in your inventory or the current room");
        return null;
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

    public boolean getGameEnd() {
        return gameEnd;
    }

    public String getEndMsg() {
        return endMsg;
    }
}
