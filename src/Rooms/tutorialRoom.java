package Rooms;

import Interaction.item;
import Interaction.obstacle;
import java.util.HashMap;

public class tutorialRoom extends room {

    public tutorialRoom(String description, item[] items, obstacle[] obstacles, HashMap<item, obstacle> blockedBy){
        super(description, items, obstacles, blockedBy);
    }
}
