package Interaction;

import Events.event;

public class obstacle extends interactive {
    private boolean solved;
    private interactive solvedBy;
    private String resolvedMsg;
    private String resolveFailMsg;
    private String alreadyResolvedMsg;
    private String usedWithoutSolveMsg;
    private event useNonResolvedResult;


    public obstacle(interactive solvedBy, String resolvedMsg, String resolveFailMsg, String alreadyResolvedMsg, String usedWithoutSolveMsg,
                    event useNonResolvedResult, String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                    event useResult, boolean canTake) {

        super(itemIs, description, feelsLike, usedAlone, touchResult, useResult, canTake);
        this.solved = false;
        this.solvedBy = solvedBy;
        this.resolvedMsg = resolvedMsg;
        this.resolveFailMsg = resolveFailMsg;
        this.alreadyResolvedMsg = alreadyResolvedMsg;
        this.usedWithoutSolveMsg = usedWithoutSolveMsg;
        this.useNonResolvedResult = useNonResolvedResult;
        this.canTake = false;   // no matter the input canTake - find a way to remove it, maybe give interactive a constructor without canTake
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
    public event use() {
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
