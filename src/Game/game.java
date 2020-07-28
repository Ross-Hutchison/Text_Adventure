package Game;

import Rooms.*;
import Interaction.item;

import java.util.HashMap;
import java.util.regex.*;

public class game {

    private roomFactory roomGenerator = new roomFactory();

    private boolean gameEnd;
    private String endMsg;
    private room currentRoom;
    private player Jo;

    private final String INVALID_COMMAND_ERR_MSG = "this is not a valid command - please look at the format paragraph";
    private final String COMMAND_HAS_VERBS_ERR_MSG = "this is not a valid command - please do not include multiple verbs in one command";
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
        if (verbObjMatcher.matches()) processVerbObject(input);
        else {
            Matcher itemVerbItemMatcher = itemVerbItemPattern.matcher(input);
            if (itemVerbItemMatcher.matches()) processItemVerbItem(input);
            else {
                System.out.println(INVALID_COMMAND_ERR_MSG);
            }
        }


    }

    private void processItemVerbItem(String input) {

    }

    /*
        splits input by space taking the first as the verb
        then takes it and uses it to isolate the item's itemIs descriptor
        this is used to check the item is present in the current room
        before being used to fetch the desired item object

        the verb is then checked to determine which function is carried out
     */
    private void processVerbObject(String input) {
        String[] parts = input.split(" "); //separate the input by ' '
        String verb = parts[0]; // takes the verb
        String item = "\"" + input.substring(verb.length() + 1) + "\"";  // uses the verb's length to take the set of words

        if(checkItemForVerb(item)) {
            System.out.println(COMMAND_HAS_VERBS_ERR_MSG);
            return;
        }

        item itemObj;

        HashMap<String, item> itemChecker = currentRoom.getItemIsToItem();

        if (itemChecker.containsKey(item)) itemObj = itemChecker.get(item);
        else itemObj = Jo.hasItemInInventory(item);

        if (itemObj != null) {   // if the item was found in the room or player's inventory
            switch (verb) {
                case "take":
                    currentRoom.playerTakesItem(Jo, itemObj);
                    break;
                case "touch":
                    currentRoom.playerTouchedItem(Jo, itemObj);
                    break;
                case "lookAt":
                    currentRoom.playerLooksAtItem(Jo, itemObj);
                    break;
                case "use":
                    currentRoom.playerUsedItem(Jo, itemObj);
                    break;
                case "taste":
                    currentRoom.playerTastedItem(Jo, itemObj);
                    break;
            }
        } else
            System.out.println("you turn your attention to the " + item + "\n" +
                    "but you must have hallucinated it\n" +
                    "- this item is not currently present in your inventory or the current room");
    }

    private boolean checkItemForVerb(String item) {
        String[] verbsArr = verbs.split("\\|"); // verbs for: "verb item" format
        String[] verbs_2Arr = verbs_2.split("\\|"); // verbs for: "item verb item" format
        // adds a space to the start and removes the "" surrounding it so that the below check will work
        String modifiedItem = " " + item.substring(1, item.length() - 1);
        for (String verb : verbsArr) {
            if(modifiedItem.contains(" " + verb)) return true;
        }
        for (String verb : verbs_2Arr) {
            if(modifiedItem.contains(" " + verb)) return true;
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
