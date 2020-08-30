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
    private final String EXTERNAL_INTERACTIVE_DATA_SEPARATOR = " # ";    // separates the different variables of each interactive when it is outside an interactive
    private final String INTERNAL_INTERACTIVE_DATA_SEPARATOR = " ## ";    // separates the different variables of each interactive when it is inside an interactive
    private final String EVENT_DATA_SEPARATOR = " ; ";  // separates the different variables of each event object
    private final String MAP_DATA_SEPARATOR = " ! ";    // separates the different key-value pairings in the map

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
            saveData = saveData.concat(saveInteractive(current, EXTERNAL_INTERACTIVE_DATA_SEPARATOR));
        }
        saveData += ROOM_DATA_SEPARATOR;

    // adds all the obstacles to the saveData
        obstacle[] obstacles = toSave.getObstacles();

        for (int i = 0; i < obstacles.length; i++) {
            obstacle current = obstacles[i];
            if (i != 0) {   // if not first element add separator before element
                saveData = saveData.concat(ARRAY_ENTRY_SEPARATOR);
            }
            saveData = saveData.concat(saveObstacle(current, EXTERNAL_INTERACTIVE_DATA_SEPARATOR));
        }

        saveData += ROOM_DATA_SEPARATOR;

    // now saves the hashMap that maps interactives to the obstacles that block them
        Map<interactive, obstacle> map = toSave.getBlockedBy();
        Object[] keys = map.keySet().toArray(); // the set of all items that are blocked

        for (int i = 0; i < keys.length; i++) {
            interactive current = (interactive) keys[i];
            String toAdd = current.getDisplayItemIs() + ":" + map.get(current).getDisplayItemIs();
            if (i != 0) saveData += MAP_DATA_SEPARATOR;
            saveData += toAdd;
        }

        roomIdToSaveData.put(toSave.getId(), saveData);
    }

    /*
        converts an obstacle to a String for saving
     */
    private String saveObstacle(obstacle toSave, String separator) {
        String saveData = "";
        saveData += toSave.getSolvedBy();
        saveData += separator;
        saveData += toSave.getResolvedMsg();
        saveData += separator;
        saveData += toSave.getResolveFailMsg();
        saveData += separator;
        saveData += toSave.getAlreadyResolvedMsg();
        saveData += separator;
        saveData += toSave.getUsedWithoutSolveMsg();
        saveData += separator;
        saveData += saveEvent(toSave.getUseNonResolvedResult());
        saveData += separator;
        saveData += saveEvent(toSave.getResolveEvent());
        saveData += separator;
        saveData += toSave.getSolved();
        saveData += separator;
        saveData += saveInteractive(toSave, separator);    // adds the standard interactive data

        return saveData;
    }

    private String saveInteractive(interactive toSave, String separator) {// converts the interactive to a String
        String saveData = "";

        if (toSave != null) {
            saveData += toSave.getType();
            saveData += separator;
            saveData += toSave.getFullItemIs();
            saveData += separator;
            saveData += toSave.getDescription();
            saveData += separator;
            saveData += toSave.getFeelsLike();
            saveData += separator;
            saveData += toSave.getUsedAlone();
            saveData += separator;
            saveData += saveEvent(toSave.getTouchResult());
            saveData += separator;
            saveData += saveEvent(toSave.getUseResult());
            saveData += separator;
            saveData += toSave.getCanTake();
        } else saveData += "empty space";

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
            else if (toSave.getEventSubType().equals("addItem")) {
                saveData += ((addItemEvent)toSave).getEventSpecifics();
                saveData += EVENT_DATA_SEPARATOR;
                saveData += saveInteractive(((addItemEvent)toSave).getToAdd(), INTERNAL_INTERACTIVE_DATA_SEPARATOR);
                saveData += EVENT_DATA_SEPARATOR;
                saveData += ((addItemEvent)toSave).getLocation();
            }
            else if (toSave.getEventSubType().equals("resolveObstacle")) {
                saveData += ((resolveObstacleEvent)toSave).getEventSpecifics();
                saveData += EVENT_DATA_SEPARATOR;
                saveData += ((resolveObstacleEvent)toSave).getToResolve();
            }
        } else saveData += "null";
        return saveData;
    }

    public room loadRoom(String id) {
        room toLoad = null;

        String loadData = roomIdToSaveData.get(id);
        if (loadData != null) {
            String[] roomData = loadData.split(ROOM_DATA_SEPARATOR);   // get the different room data segments

            String desc = roomData[0];  // assign the description to a variable

            interactive[] items = loadItems(roomData[1], EXTERNAL_INTERACTIVE_DATA_SEPARATOR);   // parse the second data section into an array of items present

            HashMap<String, interactive> itemMap = new HashMap<>(); // create and fill the item map
            for (interactive current : items) {
                if(current == null) continue;   // skip removed items
                itemMap.put(current.getDisplayItemIs(), current);
            }

            obstacle[] obstacles = loadObstacles(roomData[2], EXTERNAL_INTERACTIVE_DATA_SEPARATOR); // create the obstacle array from the third data section

            HashMap<String, obstacle> obstacleMap = new HashMap<>();    // create and fill the obstacle map
            for (obstacle current : obstacles) {
                if(current == null) continue;   // skips any missing obstacle (shouldn't happen but hey)
                obstacleMap.put(current.getDisplayItemIs(), current);
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
    private interactive[] loadItems(String itemsStr, String separator) {
        String[] parts = itemsStr.split(ARRAY_ENTRY_SEPARATOR);
        interactive[] retData = new interactive[parts.length];
        int insertIndex = 0;

        for (String part : parts) {
            if (part.equals("empty space"))
                continue; // skip processing for items that are not present in the room anymore
            String[] segments = part.split(separator);
            event touchEvent = loadEvent(segments[5]);
            event useEvent = loadEvent(segments[6]);
            boolean canTake = (segments[7].equals("true"));

            interactive toAdd = new interactive(segments[0], segments[1], segments[2], segments[3], segments[4], touchEvent, useEvent, canTake);
            retData[insertIndex++] = toAdd;
        }
        return retData;
    }

    /*
        converts the String version of the obstacle array back into an array
     */
    private obstacle[] loadObstacles(String roomStr, String separator) {
        String[] parts = roomStr.split(ARRAY_ENTRY_SEPARATOR);
        obstacle[] retData = new obstacle[parts.length];
        int insertIndex = 0;

        for (String part : parts) {
            String[] segments = part.split(separator);

            event useNonResolvedEvent = loadEvent(segments[5]);
            event resolveEvent = loadEvent(segments[6]);
            event touchEvent = loadEvent(segments[13]);
            event useEvent = loadEvent(segments[14]);
            boolean solved = (segments[7].equals("true"));
            boolean canTake = (segments[15].equals("true"));

            obstacle toAdd = new obstacle(segments[0], segments[1], segments[2], segments[3], segments[4], useNonResolvedEvent, resolveEvent,
                    solved, segments[8] ,segments[9], segments[10], segments[11], segments[12], touchEvent, useEvent, canTake);

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
            else if (subType.equals("addItem")) {
                String eventSpecifics = segments[4];
                interactive toAdd = loadItems(segments[5], INTERNAL_INTERACTIVE_DATA_SEPARATOR)[0];  // could probably use this to load a single item - maybe
                String location = segments[6];
                return new addItemEvent(type, eventSpecifics, toAdd, location);
            }
            else if (subType.equals("resolveObject")) {
                String eventSpecifics = segments[4];
                String[] obst = segments[5].split(":"); // should separate back into isIn and
                String isIn = obst[0] + ":";
                String itemIs = obst[1];
                return new resolveObstacleEvent(type, eventSpecifics, isIn, itemIs);
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
