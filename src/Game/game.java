package Game;

import Factories.roomFactory;
import Rooms.*;
import Interaction.interactive;
import Interaction.obstacle;

import java.util.HashMap;
import java.util.regex.*;

public class game {

    private roomFactory roomGenerator = new roomFactory();

    private static boolean gameEnd;
    private static String endMsg;
    private room currentRoom;
    private player Jo;

    private final String INVALID_COMMAND_ERR_MSG = "this is not a valid command - please look at the format paragraph";
    private final String verbs = "lookAt|touch|take|use";
    private final String verbs_2 = "switchWith|useOn";
    private Pattern verbObjectPattern = Pattern.compile("^(" + verbs + ")( [a-zA-Z]+)+$");
    private Pattern itemVerbItemPattern = Pattern.compile("^[a-zA-Z]+( [a-zA-Z]+)+ (" + verbs_2 + ")( [a-zA-Z]+)+$");
    private eventProcessor E_processor = new eventProcessor();
    private inputProcessor I_processor = new inputProcessor();

    public void outputCommandFormats() {
        System.out.println("--------------------");
        System.out.println("Verbs: lookAt, touch, take, use, useOn, switchWith");
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
        I_processor.setUpInputProcessor(this);

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
        if (verbObjMatcher.matches()) event = I_processor.processVerbObject(input, currentRoom, Jo);
        else {
            Matcher itemVerbItemMatcher = itemVerbItemPattern.matcher(input);
            if (itemVerbItemMatcher.matches())
                event = I_processor.processItemVerbItem(input, currentRoom, Jo);
            else {
                System.out.println(INVALID_COMMAND_ERR_MSG);
            }
        }
        E_processor.processEvent(event, Jo, currentRoom);
    }

    public boolean getGameEnd() {
        return gameEnd;
    }

    public String getEndMsg() {
        return endMsg;
    }

    public static void setGameEnd(boolean setTo) {
        game.gameEnd = setTo;
    }

    public static void setEndMsg(String setTo) {
        game.endMsg = setTo;
    }

    public String getVerbs() {
        return verbs;
    }

    public String getVerbs_2() {
        return verbs_2;
    }
}
