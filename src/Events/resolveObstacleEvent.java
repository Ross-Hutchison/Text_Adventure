package Events;

public class resolveObstacleEvent extends alterRoomEvent {

    private String toResolve;   // the fullItemIs is used to get the item
    private String resolveKey;  // all obstacles resolved with an event will have this for solutionString and "" for solutionIsIn

    public resolveObstacleEvent(String type, String eventSpecifics, String obstIsIn, String obstItemIs) {
        super(type, eventSpecifics);
        this.eventSubType = "resolveObstacle";
        this.limit = 1;
        this.usedUpMsg = "the obstacle has already been resolved";
        this.toResolve = obstIsIn + obstItemIs; // more versatile and readable than taking in a single String
        this.resolveKey = "resolvedByEvent";
    }

    public String getToResolve() {
        return toResolve;
    }

    public String getResolveKey() {
        return resolveKey;
    }
}
