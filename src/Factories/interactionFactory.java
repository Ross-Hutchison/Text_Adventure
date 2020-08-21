package Factories;

import Interaction.interactive;
import Interaction.obstacle;
import Events.*;

public class interactionFactory {

    public interactive createKey(String isIn) {

        String itemIs =  isIn + "\"large key\"";
        String description = "a heavy brass key of unusual size, the bow of which is a large ring about the size of your hand";
        String feelsLike = "the metal is cool to the touch, and smooth, the bit is made up of several rectangular teeth";
        String usedAlone = "you idly toss the key up by the bit and catch it by the bow. Very stylish";
        event useResult = null;
        event touchResult = null;

        interactive key = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return key;
    }

    public interactive createChocolate(String isIn) {
        String itemIs =  isIn + "\"chocolate bar\"";
        String description = "a bar of Schokolade brand chocolate, a cartoon chocolate bar smiles on the wrapper, " +
                "\"eat me\" its eyes seem to say, something is strangely disturbing about it";

        String feelsLike = "the wrapper crinkles under you hand, you can feel 6 rows of 3 squares, 18 delicious bites";   // maybe give a count for pieces left at some point - can be eaten one by one
        String usedAlone = "despite the unease from how much the mascot wanted to be eaten, you go to bite the chocolate";

        event useResult = new outputMessageEvent("outputMessage",
                "The sweetness is overwhelming, the bar must be 90% sugar", 6, "there is no more chocolate to eat");

        event touchResult = null;

        interactive chocolate = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return chocolate;
    }

    public interactive createBox(String isIn, interactive inside) {
        String itemIs =  isIn + "\"wooden box\"";
        String description = "a small wooden box, it's lid seems to slot in place\n" +
                "The lid is not aligned properly and so rests on top of the box half open.";

        String feelsLike = "the wood is smooth, it's a very nicely crafted box";
        String usedAlone = "you lift the lid of the box and look inside";
        event useResult = new addItemEvent("addItem", "sitting at the bottom of the box is a" + inside.getDisplayItemIs(), inside);

        event touchResult = null;

        interactive box = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return box;
    }

    public obstacle createWoodenDoor(String isIn, String to, interactive solution) {

        String leadsTo;
        if(to != null) leadsTo = to;
        else leadsTo = "nowhere";

        String solvedBy = solution.getFullItemIs();
        String itemIs =  isIn + "\"wooden door\"";
        String description = "A large brown wooden door with a rectangular base and rounded top\n" +
                "there is a large metal ring for a handle and below it a large keyhole";
        String feelsLike = "the rough wood of the door seems likely to leave splinters, the door rung is cold and heavy";
        String usedAlone = "the door swings open slowly, the hinges creaking slightly";

        String resolvedMsg = "the key turns, the lock clicks satisfyingly";
        String resolveFailMsg = "doors have two purposes, to open and to remain closed, this door has chosen the latter";
        String alreadyResolvedMsg = "the door is already unlocked prodding it more probably won't achieve much";
        String usedWithoutSolveMsg = "you push at the door, it doesn't budge. It must be locked";

        event useResult = new alterRoomEvent("moveRoom",
                leadsTo);

        event touchResult = null;
        event useNonResolvedResult = null;

        obstacle woodenDoor = new obstacle(solvedBy, resolvedMsg, resolveFailMsg, alreadyResolvedMsg, usedWithoutSolveMsg, useNonResolvedResult,
                itemIs, description, feelsLike, usedAlone, touchResult, useResult, false);
        return woodenDoor;
    }

}
