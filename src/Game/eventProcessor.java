package Game;

import Interaction.interactive;
import Rooms.room;
import java.util.HashMap;

class eventProcessor {

    void processEvent(String event, player player, room currentRoom) {
        if (event != null) {
            String[] parts = event.split("-");
            if (parts.length == 5) { //valid format is eventType-itemCausingIt-additionalInformation-interactionType-usesLeft
                String type = parts[0];
                String cause = parts[1];
                String additionalInfo = parts[2];
                String interactionType = parts[3];
                String usesLeft = parts[4];

                switch (type) {
                    case "winGame":
//                        gameEnd = true;
//                        endMsg = additionalInfo;
                        break;
                    case "revealItem":
//                        interactive toReveal = currentRoom.getItemIsToItem().get(additionalInfo);
//                        toReveal.setVisible(true);
//                        String desc = currentRoom.getDescription();
//                        desc = desc.concat("\nThe " + cause + " revealed " + additionalInfo);
//                        currentRoom.setDescription(desc);
                        break;
                    case "outputMessage":
//                        System.out.println(additionalInfo);
                        break;
                    case "usedUp":
//                        switch (interactionType) {  // there are different ways an item can be used up
//                            case "touchResult":
//                                System.out.println("poking the " + cause + "no longer does anything");
//                                break;
//                            case "useResult":
//                                System.out.println("the " + cause + " can no longer be used");
//                                break;
//                            default:
//                                System.out.println("event- " + event + "\n Has an invalid interactionType");
//
//                        }
                        return; // if the event is used up no need to process further
                    default:
                        System.out.println("invalid event flag type:\n" + event);
                        break;
                }

                interactive toAlter;
                HashMap<String, interactive> items = currentRoom.getItemIsToItem();
                toAlter = items.get(cause);
                if (toAlter == null) toAlter = player.hasItemInInventory(cause);

                if (toAlter == null) {
                    System.out.println("something went horribly wrong with reducing the uses for event:\n" + event);
                    return;
                }

                int remainingUses = Integer.parseInt(usesLeft); // decreases uses left and reconverts it to a String
                remainingUses--;
                usesLeft = Integer.toString(remainingUses);

                String usedUpFlag = "usedUp-" + cause + "-" + additionalInfo + "-" + interactionType + "-" + usesLeft;   // the event is out of uses
                String eventFlag = type + "-" + cause + "-" + additionalInfo + "-" + interactionType + "-" + usesLeft;  // there are still uses

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
            } else {
                System.out.println("invalid event flag format");
            }
        }
    }

}
