package Processors;

import java.util.HashMap;

import Rooms.room;
import Interaction.*;
import Events.*;

class traversalProcessor {  // can be package-protected since it'll be called in the event processor
    HashMap<String, String> roomIdToSaveData = new HashMap<>();// stores the id of all rooms that have been saved i.e. visited

    private final String ROOM_DATA_SEPARATOR = " - ";   // separates the different sections of the room's data
    private final String ARRAY_ENTRY_SEPARATOR = " , ";   // separates the different interactives in the room
    private final String INTERACTIVE_DATA_SEPARATOR = " | ";    // separates the different variables of each interactive
    private final String EVENT_DATA_SEPARATOR = " ; ";

    void saveRoom(room toSave) {    // converts the room to a String
        String saveData = ""; // used to store all the data

        saveData += toSave.getDescription();   // add the description
        saveData += ROOM_DATA_SEPARATOR;

        interactive[] interactives = toSave.getInteractives();

        for (int i = 0; i < interactives.length; i++) {
            interactive current = toSave.getInteractives()[i];
            saveData = saveData.concat(saveInteractive(current));
            if (i != interactives.length - 1)
                saveData += ARRAY_ENTRY_SEPARATOR; // if not last element add separator
        }
        saveData += ROOM_DATA_SEPARATOR;

        System.out.println(saveData);
    }

    private String saveInteractive(interactive toSave) { // converts the interactive to a String
        String saveData = "";
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

        System.out.println(saveData);
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

}
