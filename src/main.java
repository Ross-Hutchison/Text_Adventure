import Interaction.*;
import Rooms.*;

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
                "it contains two pillars on the left one is " + key_1.getItemIs() + " and on the other is " + choco_1.getItemIs();


        tutorialRoom tutorialRoom = new tutorialRoom(tutRoomDes, tutRoomItms, tutRoomObs, tutRoomBlocks);


        davedave.addToInventory(key_1);
        davedave.addToInventory(choco_1);

        davedave.switchX_With_Y(key_1, choco_1, tutorialRoom);


    }
}
