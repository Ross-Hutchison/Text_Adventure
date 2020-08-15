package Processors;

import java.util.HashMap;
import java.util.Map;

import Rooms.room;
import Interaction.*;
import Events.*;

class traversalProcessor {  // can be package-protected since it'll be called in the event processor
    HashMap<String, String> roomIdToSaveData = new HashMap<>();// stores the id of all rooms that have been saved i.e. visited

    private final String ROOM_DATA_SEPARATOR = " ~ ";   // separates the different sections of the room's data
    private final String ARRAY_ENTRY_SEPARATOR = " , "; // separates the different interactives in the room's array
    private final String INTERACTIVE_DATA_SEPARATOR = " # ";    // separates the different variables of each interactive
    private final String EVENT_DATA_SEPARATOR = " ; ";  // separates the different variables of each event object
    private final String MAP_DATA_SEPARATOR = " ! ";    // separates the different key-value pairings in the map

    private final String ROOM_NOT_FOUND_ERR = "The room could not be loaded - id not present in save data map";

    void saveRoom(room toSave) {    // converts the room to a String
        String saveData = ""; // used to store all the data

        saveData += toSave.getDescription();   // add the description
        saveData += ROOM_DATA_SEPARATOR;

        // adds all the items to the saveData - interactives that are not obstacles
        interactive[] interactives = toSave.getInteractives();
        for (int i = 0; i < interactives.length; i++) {
            interactive current = interactives[i];
            if (i != 0) {
                saveData = saveData.concat(ARRAY_ENTRY_SEPARATOR);
            }// if not first element add separator before element
            saveData = saveData.concat(saveInteractive(current));
        }
        saveData += ROOM_DATA_SEPARATOR;

        // adds all the obstacles to the saveData
        obstacle[] obstacles = toSave.getObstacles();

        for (int i = 0; i < obstacles.length; i++) {
            obstacle current = obstacles[i];
            if (i != 0) {
                saveData = saveData.concat(ARRAY_ENTRY_SEPARATOR);
            }// if not first element add separator before element
            saveData = saveData.concat(saveObstacle(current));
        }

        saveData += ROOM_DATA_SEPARATOR;

        // now saves the hashMap that maps interactives to the obstacles that block them
        Map<interactive, obstacle> map = toSave.getBlockedBy();
        Object[] keys = map.keySet().toArray(); // the set of all items that are blocked

        for (int i = 0; i < keys.length; i++) {
            interactive current = (interactive) keys[i];
            String toAdd = current.getItemIs() + ":" + map.get(current).getItemIs();
            if (i != 0) saveData += MAP_DATA_SEPARATOR;
            saveData += toAdd;
        }

        roomIdToSaveData.put(toSave.getId(), saveData);
    }

    /*
        converts an obstacle to a String for saving
     */
    private String saveObstacle(obstacle toSave) {
        String saveData = "";
        saveData += toSave.getSolvedBy().getItemIs(); // only need itemIs since by this point in loading the item will have been made
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += toSave.getResolvedMsg();
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += toSave.getResolveFailMsg();
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += toSave.getAlreadyResolvedMsg();
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += toSave.getUsedWithoutSolveMsg();
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += saveEvent(toSave.getUseNonResolvedResult());
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += toSave.getSolved();
        saveData += INTERACTIVE_DATA_SEPARATOR;
        saveData += saveInteractive(toSave);    // adds the standard interactive data

        return saveData;
    }

    private String saveInteractive(interactive toSave) {// converts the interactive to a String
        String saveData = "";

        if (toSave != null) {
            saveData += toSave.getItemIs();
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += toSave.getDescription();
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += toSave.getFeelsLike();
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += toSave.getUsedAlone();
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += saveEvent(toSave.getTouchResult());
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += saveEvent(toSave.getUseResult());
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += toSave.getCanTake();
            saveData += INTERACTIVE_DATA_SEPARATOR;
            saveData += toSave.getVisible();
        } else saveData += "an empty space";

        return saveData;
    }

    private String saveEvent(event toSave) {
        String saveData = "";
        if (toSave != null) {
            saveData += toSave.getEventSubType();
            saveData += EVENT_DATA_SEPARATOR;
            saveData += toSave.getType();
            saveData += EVENT_DATA_SEPARATOR;
            saveData += toSave.getLimit();
            saveData += EVENT_DATA_SEPARATOR;
            saveData += toSave.getUsedUpMsg();
            saveData += EVENT_DATA_SEPARATOR;

            if (toSave.getEventSubType().equals("outputMessage")) {
                saveData += ((outputMessageEvent) toSave).getMsg();

            } else if (toSave.getEventSubType().equals("alterRoom")) {
                saveData += ((alterRoomEvent) toSave).getEventSpecifics();
            }
        } else saveData += "null";
        return saveData;
    }

    public room loadRoom(String id) {
        room toLoad = null;

        String loadData = roomIdToSaveData.get(id);
        if (loadData == null) {
            System.out.println(ROOM_NOT_FOUND_ERR);
        } else {
            String[] roomData = loadData.split(ROOM_DATA_SEPARATOR);   // get the different room data segments

            String desc = roomData[0];  // assign the description to a variable

            interactive[] items = loadItems(roomData[1]);   // parse the second data section into an array of items present

            HashMap<String, interactive> itemMap = new HashMap<>(); // create and fill the item map
            for (interactive current : items) {
                itemMap.put(current.getItemIs(), current);
            }

            obstacle[] obstacles = loadObstacles(roomData[2], itemMap); // create the obstacle array from the third data section

            HashMap<String, obstacle> obstacleMap = new HashMap<>();    // create and fill the obstacle map
            for (obstacle current : obstacles) {
                obstacleMap.put(current.getItemIs(), current);
            }

            // converts the blockedStr to a map showing what obstacles block what items
            HashMap<interactive, obstacle> blockedMap = loadBlockedMap(roomData[3], itemMap, obstacleMap);

            toLoad = new room(id, desc, items, obstacles, blockedMap, itemMap, obstacleMap);    // create the room to load up
        }
        return toLoad;
    }


    /*
        converts the String version of the items array back into an array of interactive objects
     */
    private interactive[] loadItems(String itemsStr) {
        String[] parts = itemsStr.split(ARRAY_ENTRY_SEPARATOR);
        interactive[] retData = new interactive[parts.length];
        int insertIndex = 0;

        for (String part : parts) {
            if (part.equals("an empty space"))
                continue; // skip processing for items that are not present in the room anymore
            String[] segments = part.split(INTERACTIVE_DATA_SEPARATOR);
            event touchEvent = loadEvent(segments[4]);
            event useEvent = loadEvent(segments[5]);
            boolean canTake = (segments[6].equals("true"));
            boolean visible = (segments[7].equals("true"));

            interactive toAdd = new interactive(segments[0], segments[1], segments[2], segments[3], touchEvent, useEvent, canTake, visible);
            retData[insertIndex++] = toAdd;
        }
        return retData;
    }

    /*
        converts the String version of the obstacle array back into an array
     */
    private obstacle[] loadObstacles(String roomStr, HashMap<String, interactive> itemMap) {
        String[] parts = roomStr.split(ARRAY_ENTRY_SEPARATOR);
        obstacle[] retData = new obstacle[parts.length];
        int insertIndex = 0;

        for (String part : parts) {
            String[] segments = part.split(INTERACTIVE_DATA_SEPARATOR);

            interactive resolvedBy = itemMap.get(segments[0]);
            event useNonResolvedEvent = loadEvent(segments[5]);
            event touchEvent = loadEvent(segments[11]);
            event useEvent = loadEvent(segments[12]);
            boolean solved = (segments[6].equals("true"));
            boolean canTake = (segments[13].equals("true"));
            boolean visible = (segments[14].equals("true"));

            obstacle toAdd = new obstacle(resolvedBy, segments[1], segments[2], segments[3], segments[4], useNonResolvedEvent,
                    solved, segments[7], segments[8], segments[9], segments[10], touchEvent, useEvent, canTake, visible);

            retData[insertIndex++] = toAdd;
        }
        return retData;
    }

    /*
    converts the generated String for an event back into an event
     */
    private event loadEvent(String eventStr) {
        if (!eventStr.equals("null")) {
            String[] segments = eventStr.split(EVENT_DATA_SEPARATOR);
            String subType = segments[0];
            String type = segments[1];
            int limit = Integer.parseInt(segments[2]);
            String usedUpMsg = segments[3];

            if (subType.equals("outputMessage")) {
                String msg = segments[4];
                return new outputMessageEvent(type, msg, limit, usedUpMsg);
            } else if (subType.equals("alterRoom")) {
                String eventSpecifics = segments[4];
                return new alterRoomEvent(type, eventSpecifics, limit, usedUpMsg);
            }
        }
        return null;
    }

    private HashMap<interactive, obstacle> loadBlockedMap(String blockedStr, HashMap<String, interactive> itemMap, HashMap<String, obstacle> obstacleMap) {
        HashMap<interactive, obstacle> retData = new HashMap<>();
        String[] parts = blockedStr.split(MAP_DATA_SEPARATOR);

        for (String part : parts) {
            String[] segments = part.split(":");
            String key = segments[0];
            String value = segments[1];

            interactive insertKey = itemMap.get(key);
            obstacle insertValue = obstacleMap.get(value);

            retData.put(insertKey, insertValue);
        }

        return retData;
    }
}
