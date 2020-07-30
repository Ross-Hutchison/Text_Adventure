package Game;

import Rooms.*;
import Interaction.item;
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

    public void processInput(String input) {
        Matcher verbObjMatcher = verbObjectPattern.matcher(input);
        String event = null;
        if (verbObjMatcher.matches()) event = processVerbObject(input);
        else {
            Matcher itemVerbItemMatcher = itemVerbItemPattern.matcher(input);
            if (itemVerbItemMatcher.matches()) event = processItemVerbItem(input);
            else {
                System.out.println(INVALID_COMMAND_ERR_MSG);
            }
        }
        processEvent(event);
    }

    private void processEvent(String event) {
        if(event != null) {
            String[] parts = event.split("-");
            if(parts.length == 2) { //valid format is eventType-eventData
                String type = parts[0];
                String data = parts[1];

                if(type.equals("winGame")) {
                    gameEnd = true;
                    endMsg = data;
                }
            }
            else {
                System.out.println("invalid event flag");
            }
        }
    }

    /*
        returns null when the action does not lead to a game event such as winning
     */
    private String processItemVerbItem(String input) {
        String[] verbs_2Arr = verbs_2.split("\\|"); // array of verbs for: "item verb item" format
        String splitterVerb = null;

        for (String verb : verbs_2Arr) {
            verb = " " + verb + " ";    // looking for the verb outside of a word and not at the start or end of the input
            int verbFirstIndex = input.indexOf(verb);
            if (verbFirstIndex == -1)
                continue;  // move to the next verb since current is not present
            int verbLastIndex = input.lastIndexOf(verb);
            if (verbFirstIndex != verbLastIndex) {   // there are more than one instances of the verb making it an invalid command
                System.out.println(REPEATED_VERBS_ERR_MSG);
                return null;
            } else {
                splitterVerb = verb;
                break;
            }
        }

        if (splitterVerb == null) {  // if no verb rom verbs_2 are present - shouldn't be possible but important to be safe
            System.out.println("somehow no valid verb is present - glitch in code has occurred");
            return null;
        }

        String[] items = input.split(splitterVerb);

        if (items.length != itemVerbItem_requiredItems) { // also should never happen but just in case
            System.out.println(TOO_MANY_ITEMS_ERR_MSG);
            return null;
        }

        for (int i = 0; i < itemVerbItem_requiredItems; i++) {    // surround both items with the "" needed for processing
            items[i] = "\"" + items[i] + "\"";
            if (checkItemForVerb(items[i])) {    // check each item is free of any other verbs
                System.out.println(REPEATED_VERBS_ERR_MSG);
                return null;
            }
        }

        HashMap<String, item> itemChecker = currentRoom.getItemIsToItem();
        HashMap<String, obstacle> obstacleChecker = currentRoom.getItemIsToObstacle();

        item itemObj1;

        if (itemChecker.containsKey(items[0]))
            itemObj1 = itemChecker.get(items[0]);
        else itemObj1 = Jo.hasItemInInventory(items[0]);

        if (itemObj1 != null) {
            splitterVerb = splitterVerb.strip();

            if (splitterVerb.equals("switchWith")) {
                item itemObj2;
                if (itemChecker.containsKey(items[1]))
                    itemObj2 = itemChecker.get(items[1]);
                else itemObj2 = Jo.hasItemInInventory(items[1]);
                if (itemObj2 != null) {
                    currentRoom.playerSwitchesItems(Jo, itemObj1, itemObj2);    // no event attached - ret null
                    return null;
                }
                else
                    System.out.println("the " + items[1] + " is not present");

            } else if (splitterVerb.equals("useOn")) {
                obstacle obstacleObj;
                if (obstacleChecker.containsKey(items[1]))
                    obstacleObj = obstacleChecker.get(items[1]);
                else obstacleObj = null;

                if (obstacleObj != null) {
                    currentRoom.playerUsedItemOnObstacle(Jo, itemObj1, obstacleObj); // no event attached - ret null
                    return null;
                }
                else {
                    System.out.println("the " + items[1] + " is not present");
                    return null;
                }
            }
        }
        return null;
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

        if (checkItemForVerb(item)) {
            System.out.println(REPEATED_VERBS_ERR_MSG);
            return null;
        }

        item itemObj;

        HashMap<String, item> itemChecker = currentRoom.getItemIsToItem();
        HashMap<String, obstacle> obstacleChecker = currentRoom.getItemIsToObstacle();

        if (itemChecker.containsKey(item)) itemObj = itemChecker.get(item);
        else if(obstacleChecker.containsKey(item)) itemObj = obstacleChecker.get(item);
        else itemObj = Jo.hasItemInInventory(item);

        if (itemObj != null) {   // if the item was found in the room or player's inventory
            switch (verb) {
                case "take":
                    currentRoom.playerTakesItem(Jo, itemObj);   // no game event currently attached - ret null
                    return null;
                case "touch":
                    return currentRoom.playerTouchedItem(Jo, itemObj);
                case "lookAt":
                    currentRoom.playerLooksAtItem(Jo, itemObj); // no game event currently attached - ret null
                    return null;
                case "use":
                    return currentRoom.playerUsedItem(Jo, itemObj);
                case "taste":
                    return currentRoom.playerTastedItem(Jo, itemObj);
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
