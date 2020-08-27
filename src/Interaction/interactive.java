package Interaction;

import Events.event;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class interactive {
    String type;
    String fullItemIs;
    String displayItemIs;
    String description;
    String feelsLike;
    String usedAlone;
    event touchResult;
    event useResult;
    boolean canTake;

    public interactive(String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                       event useResult, boolean canTake) {

        this.type = "inter";
        this.fullItemIs = itemIs;

        String[] bits = fullItemIs.split(":");
        this.displayItemIs = bits[1];   // the item's itemIs without the roomId prefix - used in display and for searching the current room

        this.description = description;
        this.feelsLike = feelsLike;
        this.usedAlone = usedAlone;
        this.touchResult = touchResult;
        if (this.touchResult != null)
            this.touchResult.setBelongsTo(this);    // lets the event object know which interactive it applies to
        this.useResult = useResult;
        if (this.useResult != null)
            this.useResult.setBelongsTo(this);      // lets the event object know which interactive it applies to
        this.canTake = canTake;
    }

    public interactive(String type, String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                       event useResult, boolean canTake) {

        this.type = type;
        this.fullItemIs = itemIs;

        String[] bits = fullItemIs.split(":");
        this.displayItemIs = bits[1];   // the item's itemIs without the roomId prefix - used in display and for searching the current room

        this.description = description;
        this.feelsLike = feelsLike;
        this.usedAlone = usedAlone;
        this.touchResult = touchResult;
        if (this.touchResult != null)
            this.touchResult.setBelongsTo(this);    // lets the event object know which interactive it applies to
        this.useResult = useResult;
        if (this.useResult != null)
            this.useResult.setBelongsTo(this);      // lets the event object know which interactive it applies to
        this.canTake = canTake;
    }


    /*
    if the map of itemIs for the room contains the displayItemIs of the
    item being left adds a number to the end to distinguish it
     */
    public String addNumberInteractive(HashMap<String, interactive> itemsPresent) {
        String newItemIs = this.getDisplayItemIs();

        if (itemsPresent.containsKey(this.getDisplayItemIs())) {

            int version = 2;
            newItemIs = formatItemIsToNumbered(this.getDisplayItemIs(), version);

            while (itemsPresent.containsKey(newItemIs)) {
                newItemIs = formatItemIsToNumbered(this.getDisplayItemIs(), version++);
                }
            }

            this.displayItemIs = newItemIs;

        return this.displayItemIs;
    }

    String formatItemIsToNumbered(String itemIs, int number) {
        String retVal = itemIs;
        String numEndStr = "[\\d]\"$";
        Pattern numberEnd = Pattern.compile(numEndStr);
        Matcher check = numberEnd.matcher(itemIs);

        if(check.matches()) {   // if already numbered
            retVal = retVal.replaceFirst(numEndStr, (number + "\""));
        }
        else {  // if not currently numbered
            retVal = retVal.replaceAll("\"$", (" " + number + "\"") );
        }

        return retVal;
    }

    public String removeNumber() {
        String[] bits = this.displayItemIs.split(" [\\d]"); // split by " number"
        this.displayItemIs = bits[0];   // take the first part i.e bit without the " number"

        return this.displayItemIs;
    }

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

    public event useOn(obstacle useOn) {
        return useOn.resolve(this);
    }

    public String getFullItemIs() {
        return this.fullItemIs;
    }

    public String getDisplayItemIs() {
        return displayItemIs;
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

    public String getType() { return type; }

    public void setTouchResult(event touchResult) { this.touchResult = touchResult; }

    public void setUseResult(event useResult) { this.useResult = useResult; }
}
