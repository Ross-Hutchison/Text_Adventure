package Processors;

import Events.*;
import Game.game;
import Game.player;
import Interaction.interactive;
import Rooms.room;

public class eventProcessor {

    private traversalProcessor T_processor = new traversalProcessor();

    public void processEvent(event event, player player, room currentRoom) {
        if (event != null && event.getLimit() != 0) {   // the event exists and has not been depleted
                String type = event.getType(); // the type of event that occurred
                switch (type) {
                    case "winGame": // the game has been won
                        gameWon(event);    // the additional info will be a message that details the game's end
                        break;
                    case "revealItem": // an item has been revealed in the room
                        // adds the itemIs of the revealed item to the current room's description, the cause is the interaction that revealed the item
                        revealAnItemInTheRoom(currentRoom, event);
                        break;
                    case "outputMessage":
                        sendMessage(event); // the additional info of this event is a message to output
                        break;
                    case "usedUp":
                        System.out.println(event.getUsedUpMsg());
                        return; // if the event is used up no need to process further
                    case "moveRoom":
                        moveRoom(event, currentRoom);
                        break;
                    default:
                        System.out.println("invalid event flag type:\n" + event);
                        break;
                }
                event.decreaseLimit();
        }
    }

    private void moveRoom(event event, room currentRoom) {
        alterRoomEvent AR_event = (alterRoomEvent)event;

        T_processor.saveRoom(currentRoom);
        room toGo = T_processor.loadRoom(AR_event.getEventSpecifics());
        if(toGo != null){   // if the room was not found will have error output from T_proc otherwise alter c room
            game.setCurrentRoom(toGo);
            System.out.println("moved to a new room \n --------------------");
        }
    }

    private void gameWon(event eventData) {
        outputMessageEvent event = (outputMessageEvent) eventData;
        game.setGameEnd(true);
        game.setEndMsg(event.getMsg());
    }

    private void revealAnItemInTheRoom(room currentRoom, event eventData) {
        alterRoomEvent event = (alterRoomEvent) eventData;
        String itemIs = event.getEventSpecifics();
        String cause = event.getBelongsTo().getDisplayItemIs();

        interactive toReveal = currentRoom.getItemIsToItem().get(itemIs);
        toReveal.setVisible(true);

        String desc = currentRoom.getDescription();
        desc = desc.concat("\nThe " + cause + " revealed " + toReveal.getDisplayItemIs());
        currentRoom.setDescription(desc);
    }

    private void sendMessage(event eventData) {
        outputMessageEvent event = (outputMessageEvent)eventData;
        System.out.println(event.getMsg());
    }

}
