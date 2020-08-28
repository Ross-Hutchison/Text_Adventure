package Events;

import Interaction.obstacle;

public class resolveObstacleEvent extends alterRoomEvent {

    private String toResolve;   // the fullItemIs is used to get the item
    private String resolveKey;  // all obstacles resolved with an event will have this for solutionString and "" for solutionIsIn

    public resolveObstacleEvent(String type, String eventSpecifics, String toResolve) {
        super(type, eventSpecifics);
        this.limit = 1;
        this.usedUpMsg = "the obstacle has already been resolved";
        this.toResolve = toResolve;
        this.resolveKey = "resolvedByEvent";
    }

    public String getToResolve() {
        return toResolve;
    }

    public String getResolveKey() {
        return resolveKey;
    }
}
