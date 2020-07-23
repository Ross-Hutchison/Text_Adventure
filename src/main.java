import Interaction.*;
import Rooms.*;
import Game.player;

import java.util.HashMap;

public class main {
    public static void main(String[] args) {
        player davedave = new player();
        key key_1 = new key();
        chocolate choco_1 = new chocolate();

        item[] tutRoomItms = new item[] {key_1, choco_1};
        obstacle[] tutRoomObs = null;
        HashMap<item, obstacle> tutRoomBlocks = new HashMap<>();
        tutRoomBlocks.put(key_1, null);
        tutRoomBlocks.put(choco_1, null);

        String tutRoomDes = "a very boring room, the walls are grey stone, the floor is grey stone " +
                "it contains two pillars on the left one is a " + key_1.getItemIs() + " and on the other is " + choco_1.getItemIs();


        tutorialRoom tutorialRoom = new tutorialRoom(tutRoomDes, tutRoomItms, tutRoomObs, tutRoomBlocks);


        tutorialRoom.playerTakesItem(davedave, key_1);
        tutorialRoom.playerTakesItem(davedave, choco_1);

        tutorialRoom.playerSwitchesItems(davedave, key_1, choco_1);

        System.out.println("\nyou look at the chocolate:");
        tutorialRoom.playerLooksAtItem(davedave, choco_1);

        System.out.println("\nyou feel the chocolate:");
        tutorialRoom.playerTouchedItem(davedave, choco_1);

        System.out.println("\nyou taste the chocolate:");
        tutorialRoom.playerTastedItem(davedave, choco_1);

        System.out.println("\nyou use the chocolate:");
        tutorialRoom.playerUsedItem(davedave, choco_1);

        System.out.println("----------------------");

        System.out.println("\nyou look at the key:");
        tutorialRoom.playerLooksAtItem(davedave, key_1);

        System.out.println("\nyou feel the key:");
        tutorialRoom.playerTouchedItem(davedave, key_1);

        System.out.println("\nyou taste the key:");
        tutorialRoom.playerTastedItem(davedave, key_1);

        System.out.println("\nyou use the key:");
        tutorialRoom.playerUsedItem(davedave, key_1);
    }
}
