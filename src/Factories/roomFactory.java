package Factories;

import java.util.HashMap;

import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

public class roomFactory {

    private static interactionFactory itemGenerator = new interactionFactory();

    public room createTutorialRoom() {
        // the tutorial room's unique id
        String id = "tutorialRoom";
        // generating the item array for the room
        interactive key = itemGenerator.createKey();
        interactive choco = itemGenerator.createChocolate();
        interactive box = itemGenerator.createBox(key);
        interactive[] interactives = new interactive[]{choco, key, box};
        int originalItemCount = interactives.length;
        // generating the obstacle array
        obstacle door = itemGenerator.createWoodenDoor(null, key);
        obstacle[] obstacles = new obstacle[]{door};
        // generating the HashMap for blocked items
        HashMap<interactive, obstacle>blockedBy = new HashMap<>();
        blockedBy.put(choco, null);
        blockedBy.put(key, null);
        blockedBy.put(box, null);
        // generates the map of Strings to items
        HashMap<String, interactive>itemIsToItem = new HashMap<>();
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

        room tutorialRoom = new room(id, description, interactives, obstacles, blockedBy, itemIsToItem, itemIsToObstacle, originalItemCount);
        return tutorialRoom;
    }

}
