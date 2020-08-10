package Events;

public class alterRoomEvent extends event{

    private String eventSpecifics;  // used with the event type tp determine what specific change occurs to the room

    /*
    constructor for limited alter room events (like 95% of them)
     */
    public alterRoomEvent(String type, String eventSpecifics, String interactionType, int limit, String usedUpMsg) {
        super(type, interactionType, limit, usedUpMsg);
        this.eventSpecifics = eventSpecifics;
    }

    /*
    constructor for unlimited alter room events (just in case)
     */
    public alterRoomEvent(String type, String eventSpecifics, String interactionType) {
        super(type, interactionType);
        this.eventSpecifics = eventSpecifics;
    }

    public String getEventSpecifics() { return eventSpecifics; }
}
