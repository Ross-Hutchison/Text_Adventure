package Factories;

import java.util.HashMap;

import Events.outputMessageEvent;
import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

public class roomFactory {

    private static interactionFactory itemGenerator = new interactionFactory();

    public room createTutorialRoom() {
        // the tutorial room's unique id and interactive prefix
        String id = "dustyRoom";
        String interactivePrefix = id + ":";    // used to show that say one large key belongs to one room while another belongs to another

        // generating the item array for the room
        interactive key = itemGenerator.createKey(interactivePrefix);
        interactive choco = itemGenerator.createChocolate(interactivePrefix);
        interactive box = itemGenerator.createBox(interactivePrefix, key);
        interactive[] interactives = new interactive[]{choco, box};

        // generating the obstacle array
        obstacle door = itemGenerator.createWoodenDoor(interactivePrefix, "glade", interactivePrefix, key.getDisplayItemIs());
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
        interactive woodBundle = itemGenerator.createWoodBundle(interactivePrefix);
        interactive[] interactives = new interactive[3];    // axe and two paths
        interactives[0] = axe;

        // generating the obstacle array
        obstacle tree = itemGenerator.createTree(interactivePrefix, "", axe.getDisplayItemIs());// "" means any item of this itemIs can solve it
        obstacle vines = itemGenerator.createVines(interactivePrefix, "", axe.getDisplayItemIs());
        obstacle river = itemGenerator.createRiver(interactivePrefix, "", woodBundle.getDisplayItemIs());
        obstacle woodenDoor = itemGenerator.createWoodenDoor(interactivePrefix, "dustyRoom", "", null);
        obstacle[] obstacles = new obstacle[]{tree, river, vines, woodenDoor};

        // creates the paths to different room and adds them to interactives array
            // creates the path but alters the useEvent since this path ends the game if taken
        interactive forestPath = itemGenerator.createPath("wild", interactivePrefix, null);
        forestPath.setUseResult(new outputMessageEvent("winGame", "perhaps taking an unknown path deep into the woods with no supplies was a bad idea..."));

        interactive civilPath = itemGenerator.createPath("paved", interactivePrefix, "dustyRoom");
        interactives[1] = forestPath;
        interactives[2] = civilPath;


        // generating the HashMap for blocked items
        HashMap<interactive, obstacle>blockedBy = new HashMap<>();
        blockedBy.put(civilPath, river);
        blockedBy.put(forestPath, vines);

        // generates the map of Strings to items
        HashMap<String, interactive>itemIsToItem = new HashMap<>();
        itemIsToItem.put(axe.getDisplayItemIs(), axe);
        itemIsToItem.put(civilPath.getDisplayItemIs(), civilPath);
        itemIsToItem.put(forestPath.getDisplayItemIs(), forestPath);

        // generates the map of Strings to Obstacles
        HashMap<String, obstacle> itemIsToObstacle = new HashMap<>();
        itemIsToObstacle.put(tree.getDisplayItemIs(), tree);
        itemIsToObstacle.put(vines.getDisplayItemIs(), vines);
        itemIsToObstacle.put(river.getDisplayItemIs(), river);
        itemIsToObstacle.put(woodenDoor.getDisplayItemIs(), woodenDoor);

        // writing the description
        String description = "A clearing surrounded on all sides by a deep wood, the trees loom over you creating patches of shade\n" +
                "there is the sound of flowing water but otherwise the glade is silent.\n" +
                "by the " + woodenDoor.getDisplayItemIs() + " you came through there is an " + axe.getDisplayItemIs() + " sitting on a stump\n" +
                " leaving the door about 15 meters forwards a " + tree.getDisplayItemIs() + " stands next to a " + river.getDisplayItemIs() + "\n" +
                "there is what seems to be the ruins of a bridge spanning it and across it a " + civilPath.getDisplayItemIs() + "\n" +
                "to the left of the door behind a thick patch of " + vines.getDisplayItemIs() + " is a " + forestPath.getDisplayItemIs() + "\n" +
                "it does not seem like a good idea to take it unprepared even if you can reach it.";

        // creating and returning the room
        room tutorialRoom = new room(id, description, interactives, obstacles, blockedBy, itemIsToItem, itemIsToObstacle);
        return tutorialRoom;
    }

}
