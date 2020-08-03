package Interaction;

public class obstacle extends interactive {
    private boolean solved;
    private interactive solvedBy;
    private String resolvedMsg;
    private String resolveFailMsg;
    private String alreadyResolvedMsg;
    private String usedWithoutSolveMsg;
    private String useNonResolvedResult;


    public obstacle(interactive solvedBy, String resolvedMsg, String resolveFailMsg, String alreadyResolvedMsg, String usedWithoutSolveMsg, String useNonResolvedResult,
                    String itemIs, String description, String feelsLike, String tastesLike, String usedAlone,
                    String touchResult, String tasteResult, String useResult, boolean canTake) {

        super(itemIs, description, feelsLike, tastesLike, usedAlone, touchResult, tasteResult, useResult, canTake);
        this.solved = false;
        this.solvedBy = solvedBy;
        this.resolvedMsg = resolvedMsg;
        this.resolveFailMsg = resolveFailMsg;
        this.alreadyResolvedMsg = alreadyResolvedMsg;
        this.usedWithoutSolveMsg = usedWithoutSolveMsg;
        this.useNonResolvedResult = useNonResolvedResult;
        this.canTake = canTake;
    }

    public void resolve(interactive usedWith) {
        if (!solved && usedWith == solvedBy) {
            this.solved = true;
            System.out.println(this.resolvedMsg);
        } else if (!solved) {
            System.out.println(resolveFailMsg);
        } else System.out.println(alreadyResolvedMsg);
    }

    public boolean getSolved() {
        return solved;
    }

    @Override
    public String use() {
        if (this.solved) {
            System.out.println(this.usedAlone);
            return this.useResult;
        } else {
            System.out.println(this.usedWithoutSolveMsg);
            return this.useNonResolvedResult;
        }
    }

    @Override
    public void useOn(obstacle useOn) {   // should never happen
        System.out.println("why have you tried this - stop");
    }
}
