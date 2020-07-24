package Interaction;

public class woodenDoor extends obstacle {

    public woodenDoor(item solvedBy) {
        super(solvedBy);
        this.solved = false;
        this.itemIs = "wooden door";
        this.description = "A large brown wooden door with a rectangular base and rounded top \n" +
                " there is a large metal ring for a handle and below it a large keyhole";
        this.feelsLike = "the rough wood of the door seems likely to leave splinters, the door rung is cold and heavy";
        this.tastesLike = "after picking the splinter out of your tongue you note a distinct oak taste, it pairs poorly with the metallic taste of the handle";
        this.usedAlone = "the door swings open slowly, the hinges creaking slightly";

        this.resolvedMsg = "the key turns, the lock clicks satisfyingly";
        this.resolveFailMsg = "doors have two purposes, to open and to remain closed, this door has chosen the latter";
        this.alreadyResolvedMsg = "the door is already unlocked prodding it more probably won't achieve much";
        this.usedWithoutSolveMsg = "you push at the door, it doesn't budge. It must be locked";
    }

    @Override
    public void use() {
        super.use();
        if(solved) {
            System.out.println("congratulations you win");
            System.exit(1);
        }
    }
}
