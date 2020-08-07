package Game;

import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

import java.util.HashMap;

class eventProcessor {

    void processEvent(String event, player player, room currentRoom) {
        if (event != null) {
            String[] parts = event.split("-");
            if (parts.length == 5) { //valid format is eventType-itemCausingIt-additionalInformation-interactionType-usesLeft
                String type = parts[0]; // the type of event that occurred
                String cause = parts[1];    // the interactive that triggered the event
                String additionalInfo = parts[2];   // the additional information that is specific to the event
                String interactionType = parts[3];  // the type of interaction that triggered the event
                String usesLeft = parts[4]; // the number of times the event can occur

                switch (type) {
                    case "winGame": // the game has been won
                        gameWon(additionalInfo);    // the additional info will be a message that details the game's end
                        break;
                    case "revealItem": // an item has been revealed in the room
                        // adds the itemIs of the revealed item to the current room's description, the cause is the interaction that revealed the item
                        revealAnItemInTheRoom(currentRoom, additionalInfo, cause);
                        break;
                    case "outputMessage":
                        sendMessage(additionalInfo); // the additional info of this event is a message to output
                        break;
                    case "usedUp":
                        informPlayerEventUsedUp(interactionType, cause);// informs the user that that event for that interaction is no longer usable
                        return; // if the event is used up no need to process further
                    default:
                        System.out.println("invalid event flag type:\n" + event);
                        break;
                }
                if (!usesLeft.equals("noLimit")) {  // if the event has limited use then the limit needs to be decreased
                    interactive toAlter;
                    toAlter = findInteractiveToAlter(cause, currentRoom, player);

                    if (toAlter == null) {  // if the cause is somehow not present send error
                        System.out.println("something went horribly wrong with reducing the uses for event:\n" + event);
                        return;
                    }

                    usesLeft = decreaseUsesLeft(usesLeft);

                    String usedUpFlag = "usedUp-" + cause + "-" + additionalInfo + "-" + interactionType + "-" + usesLeft;   // the event is out of uses
                    String eventFlag = type + "-" + cause + "-" + additionalInfo + "-" + interactionType + "-" + usesLeft;  // there are still uses

                    updateEvent(toAlter, usesLeft, interactionType, usedUpFlag, eventFlag);
                }
            } else System.out.println("invalid event flag - " + event);
        }
    }

    private String decreaseUsesLeft(String usesLeft) {
        int remainingUses = Integer.parseInt(usesLeft); // decreases uses left and reconverts it to a String
        remainingUses--;
        usesLeft = Integer.toString(remainingUses);

        return usesLeft;
    }

    private interactive findInteractiveToAlter(String cause, room
            currentRoom, player p) {
        HashMap<String, interactive> items = currentRoom.getItemIsToItem();
        HashMap<String, obstacle> obstacles = currentRoom.getItemIsToObstacle();

        interactive toAlter = items.get(cause); // check if the cause is an item in the room
        if (toAlter == null)
            toAlter = p.hasItemInInventory(cause); // if it isn't check if it's in the player's inventory
        if (toAlter == null)
            toAlter = obstacles.get(cause); // if it's neither check if it's an obstacle in the room

        return toAlter; // return either the interactive to alter or null if it isn't present
    }

    private void gameWon(String msg) {
        game.setGameEnd(true);
        game.setEndMsg(msg);
    }

    private void revealAnItemInTheRoom(room currentRoom, String
            itemIs, String causeOfEvent) {
        interactive toReveal = currentRoom.getItemIsToItem().get(itemIs);
        toReveal.setVisible(true);
        String desc = currentRoom.getDescription();
        desc = desc.concat("\nThe " + causeOfEvent + " revealed " + itemIs);
        currentRoom.setDescription(desc);
    }

    private void sendMessage(String msg) {
        System.out.println(msg);
    }

    private void informPlayerEventUsedUp(String interactionType, String
            cause) {
        switch (interactionType) {  // there are different ways an item can be used up
            case "touchResult":
                System.out.println("poking the " + cause + "no longer does anything");
                break;
            case "useResult":
                System.out.println("the " + cause + " can no longer be used");
                break;
            default:
                System.out.println("event has an invalid interactionType");
        }
    }


    private void updateEvent(interactive toAlter, String usesLeft, String interactionType, String usedUpFlag, String eventFlag) {
        if (usesLeft.equals("0")) {
            switch (interactionType) {
                case "touchResult":
                    toAlter.setTouchResult(usedUpFlag);
                    break;
                case "useResult":
                    toAlter.setUseResult(usedUpFlag);
                    break;
            }
        } else {  // adds the decreased count by resetting the message
            switch (interactionType) {
                case "touchResult":
                    toAlter.setTouchResult(eventFlag);
                    break;
                case "useResult":
                    toAlter.setUseResult(eventFlag);
                    break;
            }
        }
    }
}
