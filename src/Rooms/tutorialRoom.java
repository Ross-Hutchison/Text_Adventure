package Rooms;

import Interaction.item;
import Interaction.key;
import Interaction.chocolate;
import Interaction.obstacle;
import Interaction.woodenDoor;
import java.util.HashMap;

public class tutorialRoom extends room {

    public tutorialRoom() {
        // generating the item array for the room
        key key = new key();
        chocolate choco = new chocolate();
        this.items = new item[]{choco, key};
        // generating the obstacle array
        woodenDoor door = new woodenDoor(key, null);
        this.obstacles = new obstacle[]{door};
        // generating the HashMap for blocked items
        this.blockedBy = new HashMap<>();
        this.blockedBy.put(choco, null);
        this.blockedBy.put(key, null);
        // generates the map of Strings to items
        this.itemIsToItem = new HashMap<>();
        this.itemIsToItem.put(key.getItemIs(), key);
        this.itemIsToItem.put(choco.getItemIs(), choco);
        // writing the description
        this.description = "the rooms is small and rather dull, the walls are grey brick, \n" +
                "and the floor is made of old wooden planks, several are rotting and most squeak when you walk over them\n" +
                "at the other end of the room there is a sturdy looking wooden door\n" +
                "laying on the floor by where your head was is a " + choco.getItemIs() + "\n" +
                "sitting on the floor by the door is a " + key.getItemIs();

    }

    public tutorialRoom(String description, item[] items, obstacle[] obstacles, HashMap<item, obstacle> blockedBy){
        super(description, items, obstacles, blockedBy);
    }
}
