package Interaction;

import Events.event;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class interactive {
    String fullItemIs;
    String displayItemIs;
    String description;
    String feelsLike;
    String usedAlone;
    event touchResult;
    event useResult;
    boolean canTake;
    boolean visible;

    public interactive(String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                       event useResult, boolean canTake) {
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
        this.visible = true;    // default state for any interactive is to be visible
    }

    public interactive(String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                       event useResult, boolean canTake, boolean visible) {

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
        this.visible = visible;    // default state for any interactive is to be visible
    }

    /*
    if the map of itemIs for the room contains the displayItemIs of the
    item being left adds a number to the end to distinguish it
     */
    public String addNumber(HashMap<String, interactive> itemsPresent) {
        if (itemsPresent.containsKey(this.getDisplayItemIs())) {

            int version = 2;
            String newItemIs = formatItemIsToNumbered(this.getDisplayItemIs(), version);;

            while (itemsPresent.containsKey(newItemIs)) {

                interactive present = itemsPresent.get(newItemIs);

                if (present.getVisible()) {
                    newItemIs = formatItemIsToNumbered(this.getDisplayItemIs(), version++);
                } else {
                    this.displayItemIs = newItemIs;
                    present.addNumber(itemsPresent);
                    itemsPresent.put(present.displayItemIs, present);
                    break;  // if the item stopping it was hidden then you can update and break the loop
                }
            }

            itemsPresent.put(newItemIs, this);
            this.displayItemIs = this.getDisplayItemIs() + " " + version;
        }

        return this.displayItemIs;
    }

    private String formatItemIsToNumbered(String itemIs, int number) {
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

    public void useOn(obstacle useOn) {
        useOn.resolve(this);
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

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
