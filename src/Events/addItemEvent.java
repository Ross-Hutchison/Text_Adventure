package Events;

import Interaction.interactive;

public class addItemEvent extends alterRoomEvent {

    interactive toAdd;

    /*
        this type of event has a hard set limit of 1 to prevent accidental errors
     */
    public addItemEvent(String type, String eventSpecifics, interactive toAdd) {
        super(type, eventSpecifics);
        this.toAdd = toAdd;
        this.eventSubType = "addItem";
        this.limit = 1; // can't add the same item twice would break everything
        this.usedUpMsg = "you see if repeating the action will make another " + toAdd.getDisplayItemIs() + "but it doesn't";
    }

    public interactive getToAdd() { return toAdd; }
}
