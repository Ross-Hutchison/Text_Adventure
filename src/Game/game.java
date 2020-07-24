package Game;

import Rooms.*;

public class game {

    private boolean gameEnd = false;
    private String endMsg = "";
    private room currentRoom;

    public void outputCommandFormats() {
        System.out.println("--------------------");
        System.out.println("Verbs: lookAt, touch, use, useOn, taste");
        System.out.println("Items and Obstacles will be highlighted in text using \"\" use the exact encased text when referring to them");
        System.out.println("e.g. \"big key\" \"door\" - lookAt big key  ");
        System.out.println("Verb formats: \"lookAt [item name]\" \n" +
                "\"touch [item name]\"  etc, one exception \n" +
                "\"[item name] useOn [obstacle name]\"");
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
     */
    public void setUpGame() {
        currentRoom = new tutorialRoom();
        System.out.println(currentRoom.getDescription());
    }

    public void processInput(String input) {
        System.out.println(input);
        gameEnd = true;
        endMsg = "you win!";
    }

    public boolean getGameEnd() {
        return gameEnd;
    }

    public String getEndMsg() {
        return endMsg;
    }
}
