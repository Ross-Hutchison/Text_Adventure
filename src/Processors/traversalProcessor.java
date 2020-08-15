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
    private final String INTERACTIVE_DATA_SEPARATOR = " | ";    // separates the different variables of each interactive
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
            if (i != 0) { saveData = saveData.concat(ARRAY_ENTRY_SEPARATOR); }// if not first element add separator before element
            saveData = saveData.concat(saveInteractive(current));
        }
        saveData += ROOM_DATA_SEPARATOR;

        // adds all the obstacles to the saveData
        obstacle[] obstacles = toSave.getObstacles();

        for(int i = 0; i < obstacles.length; i++ ) {
            obstacle current = obstacles[i];
            if (i != 0) { saveData = saveData.concat(ARRAY_ENTRY_SEPARATOR); }// if not first element add separator before element
            saveData = saveData.concat(saveObstacle(current));
        }

        saveData += ROOM_DATA_SEPARATOR;

        // now saves the hashMap that maps interactives to the obstacles that block them
        Map<interactive, obstacle> map = toSave.getBlockedBy();
        Object[] keys = map.keySet().toArray(); // the set of all items that are blocked

        for(int i = 0; i < keys.length; i++) {
            interactive current = (interactive)keys[i];
            String toAdd = current.getItemIs() + ":" + map.get(current).getItemIs();
            if(i != 0) saveData += MAP_DATA_SEPARATOR;
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
        saveData += saveInteractive(toSave);    // adds the standard interactive data

        return saveData;
    }

    private String saveInteractive(interactive toSave) {// converts the interactive to a String
        String saveData = "";

        if(toSave != null) {
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
            saveData += toSave.getCanTake();    // might need to be more - cause is boolean
        }
        else saveData += "an empty space";

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
        }
        else saveData += "null";
        return saveData;
    }

    public room loadRoom(String id) {
        room toLoad = null;

        String loadData = roomIdToSaveData.get(id);
        if(loadData == null) {
            System.out.println(ROOM_NOT_FOUND_ERR);
        }
        else {
            String[] roomData = loadData.split( ROOM_DATA_SEPARATOR);

            for (String part : roomData) {
                System.out.println("\n ---------------------");
                System.out.println(part);
            }
        }
        return toLoad;
    }

}
