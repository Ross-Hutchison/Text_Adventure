package Events;

import Interaction.*;

public abstract class event {
    /*
        the variables all event flags have:
        type - the type of event - winGame etc. specifies how exactly it's processed, two outputMessageEvents can be treated differently
        belongsTo - the interactive that the event belongs to, not included in constructor since the event is created before the interactive
                        assigned in the interactive's constructor
        interactionType - the type of interaction with the interactive that causes the event - touch, use etc.
        limit - the number of times the event can occur, -1 == no limit
        usedUpMsg - the message displayed when an event can no longer occur - limit == 0
    */
    String eventSubType;
    private String type;
    private interactive belongsTo;
    private int limit;
    private String usedUpMsg;

    // constructor for limited events
    public event(String type, int limit, String usedUpMsg) {
        this.type = type;
        this.belongsTo = null; // assigned when it is passed into an interactive's constructor
        this.limit = limit;
        this.usedUpMsg = usedUpMsg;
    }

    // constructor for unlimited events - saves typing in -1 and "cannot be used up..." repeatedly
    public event(String type) {
        this.type = type;
        this.belongsTo = null; // assigned when it is passed into an interactive's constructor
        this.limit = -1;
        this.usedUpMsg = "cannot be used up - no limit";
    }

    /*
    a limit of -1 represents an unlimited event
    while a limit of 0 represents a depleted event
    checking the limit is > 0 means that unlimited and
    depleted events are ignored while limited but non depleted events
    have their remaining uses decreased
     */
    public void decreaseLimit() {
        if(this.limit > 0) {
            this.limit--;
        }
    }


    /*
    used to tell the event which interactive it belongs to
 */
    public void setBelongsTo(interactive belongsTo) { this.belongsTo = belongsTo; }

    public String getType() { return this.type; }
    public interactive getBelongsTo() { return this.belongsTo; }
    public int getLimit() { return this.limit; }
    public String getUsedUpMsg() { return this.usedUpMsg; }

    public String getEventSubType() { return this.eventSubType; }
}
