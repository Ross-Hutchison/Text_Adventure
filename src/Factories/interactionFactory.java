package Factories;

import Interaction.interactive;
import Interaction.obstacle;
import Rooms.room;

public class interactionFactory {

    public interactive createKey() {

        String itemIs = "\"large key\"";
        String description = "a heavy brass key of unusual size, the bow of which is a large ring about the size of your hand";
        String feelsLike = "the metal is cool to the touch, and smooth, the bit is made up of several rectangular teeth";
        String usedAlone = "you idly toss the key up by the bit and catch it by the bow. Very stylish";
        String useResult = null;
        String touchResult = null;

        interactive key = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return key;
    }

    public interactive createChocolate() {
        String itemIs = "\"chocolate bar\"";
        String description = "a bar of Schokolade brand chocolate, a cartoon chocolate bar smiles on the wrapper, " +
                "\"eat me\" its eyes seem to say, something is strangely disturbing about it";

        String feelsLike = "the wrapper crinkles under you hand, you can feel 6 rows of 3 squares, 18 delicious bites";   // maybe give a count for pieces left at some point - can be eaten one by one
        String usedAlone = "despite the unease from how much the mascot wanted to be eaten, you go to bite the chocolate";
        String useResult = "outputMessage-" + itemIs + "-The sweetness is overwhelming, the bar must be 90% sugar-useResult-1";
        String touchResult = null;

        interactive chocolate = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return chocolate;
    }

    public interactive createBox(interactive inside) {
        String itemIs = "\"wooden box\"";
        String description = "a small wooden box, it's lid seems to slot in place\n" +
                "The lid is not aligned properly and so rests on top of the box half open.";

        String feelsLike = "the wood is smooth, it's a very nicely crafted box";
        String usedAlone = "you lift the lid of the box and look inside";
        String useResult = "revealItem-" + itemIs + "-" + inside.getItemIs() + "-" + "useResult-1"; // can happen once
        String touchResult = null;

        interactive box = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        inside.setVisible(false);
        return box;
    }

    public obstacle createWoodenDoor(room to, interactive solution) {

        room leadsTo = to;
        interactive solvedBy = solution;
        String itemIs = "\"wooden door\"";
        String description = "A large brown wooden door with a rectangular base and rounded top \n" +
                " there is a large metal ring for a handle and below it a large keyhole";
        String feelsLike = "the rough wood of the door seems likely to leave splinters, the door rung is cold and heavy";
        String usedAlone = "the door swings open slowly, the hinges creaking slightly";

        String resolvedMsg = "the key turns, the lock clicks satisfyingly";
        String resolveFailMsg = "doors have two purposes, to open and to remain closed, this door has chosen the latter";
        String alreadyResolvedMsg = "the door is already unlocked prodding it more probably won't achieve much";
        String usedWithoutSolveMsg = "you push at the door, it doesn't budge. It must be locked";

        String useResult = "winGame-" + itemIs + "-Congratulations you left the room!!!: you have won the game-" + "useResult-1"; //can only win once
        String touchResult = null;
        String useNonResolvedResult = null;

        obstacle woodenDoor = new obstacle(solvedBy, resolvedMsg, resolveFailMsg, alreadyResolvedMsg, usedWithoutSolveMsg, useNonResolvedResult,
                itemIs, description, feelsLike, usedAlone, touchResult, useResult, false);
        return woodenDoor;
    }

}
