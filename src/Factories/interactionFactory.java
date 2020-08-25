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
        event useResult = new addItemEvent("addItem", "sitting at the bottom of the box is a " + inside.getDisplayItemIs(),
                inside, "on the floor next to where you found the box");

        event touchResult = null;

        interactive box = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return box;
    }

    public obstacle createWoodenDoor(String isIn, String to, String solutionIsIn, String solutionItemIs) {

        String leadsTo;
        if(to != null) leadsTo = to;
        else leadsTo = "nowhere";

        String solvedBy;
        if(solutionItemIs != null) solvedBy = solutionIsIn + solutionItemIs;
        else solvedBy = solutionIsIn;   // prevents NullPointerExceptions from String + null

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

    public interactive createAxe(String isIn) {
        String itemIs =  isIn + "\"axe\"";
        String description = "a sturdy looking axe, the handle is long and curves slightly near the end\n" +
                "The head is dull grey but looks to be in good condition, the blade is sharp.";

        String feelsLike = "the wood is smooth but slightly worn likely where the previous owner would hold it.";
        String usedAlone = "you lift the axe up to your shoulder and let it rest there, what a stylish pose";
        event useResult = null;
        event touchResult = null;

        interactive axe = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return axe;
    }

    public interactive createWoodBundle(String isIn) {
        String itemIs =  isIn + "\"wood bundle\"";
        String description = "a bundle of large sturdy sticks, they look like they could support your weight.";

        String feelsLike = "the branches are rough and heavy, several have jagged spikes where smaller branches were broken off.";
        String usedAlone = "put all but one of the sticks down and begin to swing the remaining one around\n" +
                "all the while making quiet \"shing, shing, whack\" noises to yourself.";
        event useResult = null;
        event touchResult = null;

        interactive woodBundle = new interactive(itemIs, description, feelsLike, usedAlone, touchResult, useResult, true);
        return woodBundle;
    }

    public obstacle createTree(String isIn, String solutionIsIn, String solutionItemIs) {

        String solvedBy;
        if(solutionItemIs != null) solvedBy = solutionIsIn + solutionItemIs;
        else solvedBy = solutionIsIn;   // prevents NullPointerExceptions from String + null

        String itemIs =  isIn + "\"tree\"";
        String description = "A young fir tree with dozens of branches\n" +
                "there are two initials - \"A.M & I.K\" carved in a heart\n" +
                " underneath the heart is carved - \"go die - I.K\"";

        String feelsLike = "the rough bark of the tree is pleasant to touch, sticky sap leaks out in places.";
        String usedAlone = "you start to pull branches from the fallen tree.";

        String resolvedMsg = "your swings eventually have an effect, the tree groans as it topples to the floor.";
        String resolveFailMsg = "you bang it against the tree... the tree seems unimpressed.";
        String alreadyResolvedMsg = "Stop! please! he's already dead : (";
        String usedWithoutSolveMsg = "you sit for a while under the tree, it's very relaxing";

        event useResult = new addItemEvent("addItem", "you gather several of the branches into a bundle", createWoodBundle(isIn), "laying near the fallen tree is");
        event touchResult = null;
        event useNonResolvedResult = null;

        obstacle tree = new obstacle(solvedBy, resolvedMsg, resolveFailMsg, alreadyResolvedMsg, usedWithoutSolveMsg, useNonResolvedResult,
                itemIs, description, feelsLike, usedAlone, touchResult, useResult, false);
        return tree;
    }

    public obstacle createVines(String isIn, String solutionIsIn, String solutionItemIs) {

        String solvedBy;
        if(solutionItemIs != null) solvedBy = solutionIsIn + solutionItemIs;
        else solvedBy = solutionIsIn;   // prevents NullPointerExceptions from String + null

        String itemIs =  isIn + "\"vines\"";
        String description = "A tangle of thick green vines all twisted and tangled together\n" +
                "they stretch far into the distance seemingly covering most of the forest in that direction.";

        String feelsLike = "the vines are tough and tightly twisted together";
        String usedAlone = "you think about how long it must have been since this place was maintained for the vines to have grown so much";
        String resolvedMsg = "you bring the " + solutionItemIs + " down into a section of the vines\n" +
                "some fall away but it takes several more swings to clear enough space to pass through.";

        String resolveFailMsg = "you try to remove the vines, but this seems to be the wrong tool for the job and you don't make much progress.";
        String alreadyResolvedMsg = "you go to clear away the vines before remembering that you already have.";
        String usedWithoutSolveMsg = "you pull at the vines to little effect, a hidden thorn pricks your finger";

        event useResult = null;
        event touchResult = null;
        event useNonResolvedResult = null;

        obstacle vines = new obstacle(solvedBy, resolvedMsg, resolveFailMsg, alreadyResolvedMsg, usedWithoutSolveMsg, useNonResolvedResult,
                itemIs, description, feelsLike, usedAlone, touchResult, useResult, false);
        return vines;
    }

    public obstacle createRiver(String isIn, String solutionIsIn, String solutionItemIs) {

        String solvedBy;
        if(solutionItemIs != null) solvedBy = solutionIsIn + solutionItemIs;
        else solvedBy = solutionIsIn;   // prevents NullPointerExceptions from String + null

        String itemIs =  isIn + "\"river\"";
        String description = "a deep, fast flowing river, the current looks Strong it would be unwise to try swim across it";

        String feelsLike = "the cold water is relaxing as it flows through your fingers, the noticeable force it exerts on your hand is less so.";
        String usedAlone = "you move to a point where the water flows fast over rocks and moss and take a quick drink, the cold water is refreshing ";
        String resolvedMsg = "you take the " + solutionItemIs + " and with it manage to bridge the gap between the river's banks";

        String resolveFailMsg = "you try to bridge the river, but this seems to be the wrong tool for the job and you don't make much progress, nearly losing the " + solutionItemIs + " several times.";
        String alreadyResolvedMsg = "you already have a way across, best not to tamper with it or you might end up in the drink.";
        String usedWithoutSolveMsg = usedAlone;

        event useResult = null;
        event touchResult = null;
        event useNonResolvedResult = null;

        obstacle river = new obstacle(solvedBy, resolvedMsg, resolveFailMsg, alreadyResolvedMsg, usedWithoutSolveMsg, useNonResolvedResult,
                itemIs, description, feelsLike, usedAlone, touchResult, useResult, false);
        return river;
    }

    public interactive createPath() {
        /*
        TODO:
            need either two types of path or for path to take some kind of variable that decided on what type of description
            this is because I need a nice path to civilisation and a wild path that goes deeper into the forest
            would prefer the variable approach since a lot of it will be the same just a different description
         */
        return null;
    }


}
