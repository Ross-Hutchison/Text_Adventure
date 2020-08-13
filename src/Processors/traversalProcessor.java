package Processors;

import java.util.HashMap;

import Rooms.room;

class traversalProcessor {  // can be package-protected since it'll be called in the event processor
    HashMap<String, String> roomIdToSaveData = new HashMap<>();// stores the id of all rooms that have been saved i.e. visited

    void saveRoom(room toSave) {
        StringBuilder saveData = new StringBuilder();
        saveData.append(toSave.getDescription());
        saveData.append("-");
        saveData.append(toSave.getInteractives());
        System.out.println(saveData.toString());
    }

}
