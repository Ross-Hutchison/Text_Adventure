package Factories;

import java.util.HashMap;

import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

public class roomFactory {

    private static interactionFactory itemGenerator = new interactionFactory();

    public room createTutorialRoom() {
        // the tutorial room's unique id and interactive prefix
        String id = "tutorialRoom";
        String interactivePrefix = id + ":";    // used to show that say one large key belongs to one room while another belongs to another

        // generating the item array for the room
        interactive key = itemGenerator.createKey(interactivePrefix);
        interactive choco = itemGenerator.createChocolate(interactivePrefix);
        interactive box = itemGenerator.createBox(interactivePrefix, key);
        interactive[] interactives = new interactive[]{choco, box};

        // generating the obstacle array
        obstacle door = itemGenerator.createWoodenDoor(interactivePrefix, "tutorialRoom", key);
        obstacle[] obstacles = new obstacle[]{door};

        // generating the HashMap for blocked items
        HashMap<interactive, obstacle>blockedBy = new HashMap<>();
        blockedBy.put(choco, door);

        // generates the map of Strings to items
        HashMap<String, interactive>itemIsToItem = new HashMap<>();
        itemIsToItem.put(choco.getDisplayItemIs(), choco);
        itemIsToItem.put(box.getDisplayItemIs(), box);

        // generates the map of Strings to Obstacles
        HashMap<String, obstacle> itemIsToObstacle = new HashMap<>();
        itemIsToObstacle.put(door.getDisplayItemIs(), door);

        // writing the description
        String description = "the room is small and rather dull, the walls are grey brick, \n" +
                "and the floor is made of old wooden planks, several are rotting and most squeak when you walk over them\n" +
                "at the other end of the room there is a sturdy looking " + door.getDisplayItemIs() + "\n" +
                "laying on the floor by where your head was is a " + choco.getDisplayItemIs() + "\n" +
                "sitting on the floor by the door is a " + box.getDisplayItemIs();

        // creating and returning the room
        room tutorialRoom = new room(id, description, interactives, obstacles, blockedBy, itemIsToItem, itemIsToObstacle);
        return tutorialRoom;
    }

    public room createForestGlade(){
        // the tutorial room's unique id and interactive prefix
        String id = "glade";
        String interactivePrefix = id + ":";    // used to show that say one large key belongs to one room while another belongs to another

        // generating the item array for the room
        interactive axe = itemGenerator.createAxe(interactivePrefix);
        interactive[] interactives = new interactive[]{};

        // generating the obstacle array
        obstacle[] obstacles = new obstacle[]{};

        // generating the HashMap for blocked items
        HashMap<interactive, obstacle>blockedBy = new HashMap<>();

        // generates the map of Strings to items
        HashMap<String, interactive>itemIsToItem = new HashMap<>();

        // generates the map of Strings to Obstacles
        HashMap<String, obstacle> itemIsToObstacle = new HashMap<>();

        // writing the description
        String description = "";

        // creating and returning the room
        room tutorialRoom = new room(id, description, interactives, obstacles, blockedBy, itemIsToItem, itemIsToObstacle);
        return tutorialRoom;
    }

}
