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
    private String type;
    private interactive belongsTo;
    private String interactionType;
    private int limit;
    private String usedUpMsg;

    // constructor for limited events
    public event(String type, String interactionType, int limit, String usedUpMsg) {
        this.type = type;
        this.belongsTo = null; // assigned when it is passed into an interactive's constructor
        this.interactionType = interactionType;
        this.limit = limit;
        this.usedUpMsg = usedUpMsg;
    }

    // constructor for unlimited events - saves typing in -1 and "cannot be used up..." repeatedly
    public event(String type, String interactionType) {
        this.type = type;
        this.belongsTo = null; // assigned when it is passed into an interactive's constructor
        this.interactionType = interactionType;
        this.limit = -1;
        this.usedUpMsg = "cannot be used up - no limit";
    }

    /*
        used to tell the event which interactive it belongs to
     */
    public void setBelongsTo(interactive belongsTo) { this.belongsTo = belongsTo; }

    /*
    checks there is a limit (limit != -1)
    and then that the limit has not been reached (limit != 0)
    if this is the case decreases the limit by 1
     */
    public void decreaseLimit() {
        if(this.limit != -1 && this.limit != 0) {
            this.limit--;
        }
    }

    public String getType() { return this.type; }
    public interactive belongsTo() { return this.belongsTo; }
    public String getInteractionType() { return this.interactionType; }
    public int getlimit() { return this.limit; }
    public String getUsedUpMsg() { return this.usedUpMsg; }
}
