package Interaction;

public class obstacle extends item {
     boolean solved;
     item solvedBy;
     String resolvedMsg;
     String resolveFailMsg;
     String alreadyResolvedMsg;
     String usedWithoutSolveMsg;

     public  obstacle(item solvedBy) {   // empty constructor allows each extension to have a default version
         this.solvedBy = solvedBy;
     }

    public obstacle(item solvedBy, String resolvedMsg, String resolveFailMsg, String alreadyResolvedMsg, String usedWithoutSolveMsg,
                    String itemIs, String description, String feelsLike, String tastesLike, String usedAlone) {

        super(itemIs, description, feelsLike, tastesLike, usedAlone);
        this.solved = false;
        this.solvedBy = solvedBy;
        this.resolvedMsg = resolvedMsg;
        this.resolveFailMsg = resolveFailMsg;
        this.alreadyResolvedMsg = alreadyResolvedMsg;
        this.usedWithoutSolveMsg = usedWithoutSolveMsg;
    }

    public void resolve(item usedWith){
        if(!solved && usedWith == solvedBy) {
            this.solved = true;
            System.out.println(this.resolvedMsg);
        }
        else if(!solved) {
            System.out.println(resolveFailMsg);
        }
        else System.out.println(alreadyResolvedMsg);
    }

    public boolean getSolved() {
         return solved;
    }

    @Override
    public void use() {
        if(this.solved) {
            System.out.println(this.usedAlone);
        }
        else System.out.println(this.usedWithoutSolveMsg);
    }

    @Override
    public void useOn(obstacle useOn) {   // should never happen
        System.out.println("why have you tried this - stop");
    }
}
