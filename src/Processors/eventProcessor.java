package Processors;

import Events.*;
import Game.game;
import Game.player;
import Interaction.interactive;
import Rooms.room;

public class eventProcessor {

    public void processEvent(event event, player player, room currentRoom) {
        if (event != null && event.getLimit() != 0) {   // the event exists and has not been depleted
                String type = event.getType(); // the type of event that occurred
                interactive cause = event.getBelongsTo();   // the interactive that triggered the event

                switch (type) {
                    case "winGame": // the game has been won
                        gameWon( ((outputMessageEvent)event).getMsg() );    // the additional info will be a message that details the game's end
                        break;
                    case "revealItem": // an item has been revealed in the room
                        // adds the itemIs of the revealed item to the current room's description, the cause is the interaction that revealed the item
                        revealAnItemInTheRoom(currentRoom, ((alterRoomEvent)event).getEventSpecifics(), cause);
                        break;
                    case "outputMessage":
                        sendMessage( ((outputMessageEvent)event).getMsg() ); // the additional info of this event is a message to output
                        break;
                    case "usedUp":
                        System.out.println(event.getUsedUpMsg());
                        return; // if the event is used up no need to process further
                    default:
                        System.out.println("invalid event flag type:\n" + event);
                        break;
                }
                event.decreaseLimit();
        }
    }

    private void gameWon(String msg) {
        game.setGameEnd(true);
        game.setEndMsg(msg);
    }

    private void revealAnItemInTheRoom(room currentRoom, String
            itemIs, interactive causeOfEvent) {
        interactive toReveal = currentRoom.getItemIsToItem().get(itemIs);
        toReveal.setVisible(true);
        String desc = currentRoom.getDescription();
        desc = desc.concat("\nThe " + causeOfEvent.getItemIs() + " revealed " + itemIs);
        currentRoom.setDescription(desc);
    }

    private void sendMessage(String msg) {
        System.out.println(msg);
    }

}
