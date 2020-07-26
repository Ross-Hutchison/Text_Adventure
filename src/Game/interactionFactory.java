package Game;

import Interaction.item;
import Interaction.obstacle;
import Rooms.room;

public class interactionFactory {

    public item createKey() {

        String itemIs = "\"large key\"";
        String description = "a heavy brass key of unusual size, the bow of which is a large ring about the size of your hand";
        String feelsLike = "the metal is cool to the touch, and smooth, the bit is made up of several rectangular teeth";
        String tastesLike = "biting the key is probably unwise, licking it leaves a metallic tang in your mouth";
        String usedAlone = "you idly toss the key up by the bit and catch it by the bow. Very stylish";

        item key = new item(itemIs, description, feelsLike, tastesLike, usedAlone);
        return key;
    }

    public item createChocolate() {
        String itemIs = "\"chocolate bar\"";
        String description = "a bar of Schokolade brand chocolate, a cartoon chocolate bar smiles on the wrapper, " +
                "\"eat me\" its eyes seem to say, something is strangely disturbing about it";

        String feelsLike = "the wrapper crinkles under you hand, you can feel 6 rows of 3 squares, 18 delicious bites";   // maybe give a count for pieces left at some point - can be eaten one by one
        String tastesLike = "despite the unease from how much the mascot wanted to be eaten, the chocolate is very delicious";
        String usedAlone = "you contemplate the marketing choice behind the mascot. You don't come up with any answers";

        item chocolate = new item(itemIs, description, feelsLike, tastesLike, usedAlone);
        return chocolate;
    }

    public obstacle createWoodenDoor(room to, item solution) {

        room leadsTo = to;
        item solvedBy = solution;
        boolean solved = false;
        String itemIs = "wooden door";
        String description = "A large brown wooden door with a rectangular base and rounded top \n" +
                " there is a large metal ring for a handle and below it a large keyhole";
        String feelsLike = "the rough wood of the door seems likely to leave splinters, the door rung is cold and heavy";
        String tastesLike = "after picking the splinter out of your tongue you note a distinct oak taste, " +
                "it pairs poorly with the metallic taste of the handle";
        String usedAlone = "the door swings open slowly, the hinges creaking slightly";

        String resolvedMsg = "the key turns, the lock clicks satisfyingly";
        String resolveFailMsg = "doors have two purposes, to open and to remain closed, this door has chosen the latter";
        String alreadyResolvedMsg = "the door is already unlocked prodding it more probably won't achieve much";
        String usedWithoutSolveMsg = "you push at the door, it doesn't budge. It must be locked";

        obstacle woodenDoor = new obstacle(solvedBy, resolvedMsg, resolveFailMsg, alreadyResolvedMsg, usedWithoutSolveMsg, itemIs, description, feelsLike, tastesLike, usedAlone);
        return woodenDoor;
    }

}
