package Processors;

import Events.*;
import Game.game;
import Game.player;
import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

import java.util.HashMap;

public class eventProcessor {

    private traversalProcessor T_processor = new traversalProcessor();

    public void processEvent(event event, player player, room currentRoom) {
        if (event != null && event.getLimit() != 0) {   // the event exists and has not been depleted
                String type = event.getType(); // the type of event that occurred
                switch (type) {
                    case "winGame": // the game has been won
                        gameWon(event);    // the additional info will be a message that details the game's end
                        break;
                    case "addItem": // an item has been revealed in the room
                        // adds the itemIs of the revealed item to the current room's description, the cause is the interaction that revealed the item
                        addItemToRoom(currentRoom, event);
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

    private void addItemToRoom(room currentRoom, event eventData) {
        addItemEvent event = (addItemEvent)eventData;

        System.out.println(event.getEventSpecifics());
        interactive toAdd = event.getToAdd();

        if(toAdd.getType().equals("inter")){
            HashMap<String, interactive> map = currentRoom.getItemIsToItem();
            interactive[] items = new interactive[currentRoom.getInteractives().length + 1];    // enough space for a new item
            toAdd.addNumberInteractive(currentRoom.getItemIsToItem());

            map.put(toAdd.getDisplayItemIs(), toAdd);   // add to interactives map

            boolean added = false;  // add to items[]
            for(int i = 0; i < currentRoom.getInteractives().length; i++) {
                interactive current = currentRoom.getInteractives()[i];
                if(current == null && !added) {
                    added = true;
                    items[i] = toAdd;
                }
                else if(current != null) items[i] = current;
            }
            if(!added) items[items.length - 1] = toAdd;
            currentRoom.setInteractives(items);

            String desc = currentRoom.getDescription();
            desc = desc.concat("\n" + event.getLocation() + " is a " + toAdd.getDisplayItemIs());
            currentRoom.setDescription(desc);

        }
        else if(toAdd.getType().equals("obsta")){   // does same as above but for obstacles
            obstacle toAddObst = (obstacle)toAdd;
            HashMap<String, obstacle> map = currentRoom.getItemIsToObstacle();
            obstacle[] obstacles = new obstacle[currentRoom.getObstacles().length + 1];    // enough space for a new item
            toAddObst.addNumberObstacle(currentRoom.getItemIsToObstacle());

            map.put(toAdd.getDisplayItemIs(), toAddObst);   // add to interactives map

            boolean added = false;  // add to items[]
            for(int i = 0; i < currentRoom.getObstacles().length; i++) {
                obstacle current = currentRoom.getObstacles()[i];
                if(current == null && !added) {
                    added = true;
                    obstacles[i] = toAddObst;
                }
                else if(current != null) obstacles[i] = current;
            }
            if(!added) obstacles[obstacles.length - 1] = toAddObst;
            currentRoom.setObstacles(obstacles);

            String desc = currentRoom.getDescription();
            desc = desc.concat("\n" + event.getLocation() + " is a " + toAdd.getDisplayItemIs());
            currentRoom.setDescription(desc);

        }
        else {
            System.out.println("Invalid item type - " + toAdd.getType());
        }


    }

    private void sendMessage(event eventData) {
        outputMessageEvent event = (outputMessageEvent)eventData;
        System.out.println(event.getMsg());
    }

}
