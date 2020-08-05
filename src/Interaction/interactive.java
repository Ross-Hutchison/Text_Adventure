package Interaction;

public class interactive {
    String itemIs;
    String description;
    String feelsLike;
    String tastesLike;
    String usedAlone;
    String touchResult;
    String tasteResult;
    String useResult;
    boolean canTake;
    boolean visible;

    public interactive(String itemIs, String description, String feelsLike, String tastesLike, String usedAlone, String touchResult,
                       String tasteResult, String useResult, boolean canTake) {
        this.itemIs = itemIs;
        this.description = description;
        this.feelsLike = feelsLike;
        this.tastesLike = tastesLike;
        this.usedAlone = usedAlone;
        this.touchResult = touchResult;
        this.tasteResult = tasteResult;
        this.useResult = useResult;
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

    public String getTasteResult() {
        return tasteResult;
    }

    public String getTastesLike() {
        return tastesLike;
    }

    public String getTouchResult() {
        return touchResult;
    }

    public String getUsedAlone() {
        return usedAlone;
    }

    public String getUseResult() {
        return useResult;
    }

    public boolean getCanTake() {
        return canTake;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setUseResult(String useResult) {
        this.useResult = useResult;
    }

    public void setTouchResult(String touchResult) {
        this.touchResult = touchResult;
    }

    public void setTasteResult(String tasteResult) {
        this.tasteResult = tasteResult;
    }

    public void setVisible(boolean visible) { this.visible = visible; }

    public void lookAt() {
        System.out.println(this.description);
    }

    public String touch() {
        System.out.println(this.feelsLike);
        return this.touchResult;
    }

    public String taste() {
        System.out.println(this.tastesLike);
        return this.tasteResult;
    }

    public String use() {
        System.out.println(this.usedAlone);
        return this.useResult;
    }

    public void useOn(obstacle useOn) {
        useOn.resolve(this);
    }
}
