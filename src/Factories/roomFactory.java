package Factories;

import java.util.HashMap;

import Interaction.item;
import Interaction.obstacle;
import Rooms.room;

public class roomFactory {

    private static interactionFactory itemGenerator = new interactionFactory();

    public room createTutorialRoom() {
        // generating the item array for the room
        item key = itemGenerator.createKey();
        item choco = itemGenerator.createChocolate();
        item box = itemGenerator.createBox(key);
        item[] items = new item[]{choco, key, box};
        int originalItemCount = items.length;
        // generating the obstacle array
        obstacle door = itemGenerator.createWoodenDoor(null, key);
        obstacle[] obstacles = new obstacle[]{door};
        // generating the HashMap for blocked items
        HashMap<item, obstacle>blockedBy = new HashMap<>();
        blockedBy.put(choco, null);
        blockedBy.put(key, null);
        blockedBy.put(box, null);
        // generates the map of Strings to items
        HashMap<String, item>itemIsToItem = new HashMap<>();
        itemIsToItem.put(key.getItemIs(), key);
        itemIsToItem.put(choco.getItemIs(), choco);
        itemIsToItem.put(box.getItemIs(), box);
        // generates the map of Strings to Obstacles
        HashMap<String, obstacle> itemIsToObstacle = new HashMap<>();
        itemIsToObstacle.put(door.getItemIs(), door);
        // writing the description
        String description = "the room is small and rather dull, the walls are grey brick, \n" +
                "and the floor is made of old wooden planks, several are rotting and most squeak when you walk over them\n" +
                "at the other end of the room there is a sturdy looking " + door.getItemIs() + "\n" +
                "laying on the floor by where your head was is a " + choco.getItemIs() + "\n" +
                "sitting on the floor by the door is a " + box.getItemIs();

        room tutorialRoom = new room(description, items, obstacles, blockedBy, itemIsToItem, itemIsToObstacle, originalItemCount);
        return tutorialRoom;
    }

}
