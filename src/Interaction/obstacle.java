package Interaction;

public abstract class obstacle extends item {
    private boolean solved;
    private item solvedBy;
    private String resolvedMsg;
    private String resolveFailMsg;

    public obstacle(item solvedBy, String resolvedMsg, String resolveFailMsg, String itemIs, String description, String feelsLike, String tastesLike, String usedAlone) {
        super(itemIs, description, feelsLike, tastesLike, usedAlone);
        this.solved = false;
        this.solvedBy = solvedBy;
        this.resolvedMsg = resolvedMsg;
        this.resolveFailMsg = resolveFailMsg;
    }

    public void resolve(item usedWith){
        if(!solved && usedWith == solvedBy) {
            this.solved = true;
            System.out.println(this.resolvedMsg);
        }
        else if(!solved) {
            System.out.println(resolvedMsg);
        }
    }

    public boolean getSolved() {
         return solved;
    }
}
