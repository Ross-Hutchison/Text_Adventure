package Interaction;

import Events.event;

public class interactive {
    String itemIs;
    String description;
    String feelsLike;
    String usedAlone;
    event touchResult;
    event useResult;
    boolean canTake;
    boolean visible;

    public interactive(String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                       event useResult, boolean canTake) {
        this.itemIs = itemIs;
        this.description = description;
        this.feelsLike = feelsLike;
        this.usedAlone = usedAlone;
        this.touchResult = touchResult;
        this.touchResult.setBelongsTo(this);    // lets the event object know which interactive it applies to
        this.useResult = useResult;
        this.useResult.setBelongsTo(this);      // lets the event object know which interactive it applies to
        this.canTake = canTake;
        this.visible = true;    // default state for any interactive is to be visible
    }

    public String getItemIs() {
        return this.itemIs;
    }

    public String getDescription() {
        return description;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public event getTouchResult() {
        return touchResult;
    }

    public String getUsedAlone() {
        return usedAlone;
    }

    public event getUseResult() {
        return useResult;
    }

    public boolean getCanTake() {
        return canTake;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setUseResult(event useResult) {
        this.useResult = useResult;
    }

    public void setTouchResult(event touchResult) { this.touchResult = touchResult; }

    public void setVisible(boolean visible) { this.visible = visible; }

    public void lookAt() {
        System.out.println(this.description);
    }

    public event touch() {
        System.out.println(this.feelsLike);
        return this.touchResult;
    }

    public event use() {
        System.out.println(this.usedAlone);
        return this.useResult;
    }

    public void useOn(obstacle useOn) {
        useOn.resolve(this);
    }
}
