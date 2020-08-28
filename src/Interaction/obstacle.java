package Interaction;

import Events.event;

import java.util.HashMap;

public class obstacle extends interactive {
    private boolean solved;
    private String solvedBy;
    private String resolvedMsg;
    private String resolveFailMsg;
    private String alreadyResolvedMsg;
    private String usedWithoutSolveMsg;
    private event useNonResolvedResult;
    private event resolveEvent;


    public obstacle(String solvedBy, String resolvedMsg, String resolveFailMsg, String alreadyResolvedMsg, String usedWithoutSolveMsg,
                    event useNonResolvedResult, event resolveEvent, String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                    event useResult, boolean canTake) {

        super(itemIs, description, feelsLike, usedAlone, touchResult, useResult, canTake);
        this.type = "obsta";
        this.solvedBy = solvedBy;
        this.solved = (this.solvedBy == null);  // only receives this if it is created already solved e.g. a door in the room the doors leads to
        this.resolvedMsg = resolvedMsg;
        this.resolveFailMsg = resolveFailMsg;
        this.alreadyResolvedMsg = alreadyResolvedMsg;
        this.usedWithoutSolveMsg = usedWithoutSolveMsg;
        this.useNonResolvedResult = useNonResolvedResult;
        if(this.useNonResolvedResult != null) this.useNonResolvedResult.setBelongsTo(this);
        this.resolveEvent = resolveEvent;
        if(this.resolveEvent != null) this.resolveEvent.setBelongsTo(this);
        this.canTake = false;   // no matter the input canTake - find a way to remove it, maybe give interactive a constructor without canTake
    }

    /*
        used to generate obstacles that are already solved - prevents needing to add solved to the standard constructor
        - used with loading obstacles in previously visited rooms
     */
    public obstacle(String solvedBy, String resolvedMsg, String resolveFailMsg, String alreadyResolvedMsg, String usedWithoutSolveMsg,
                    event useNonResolvedResult, event resolveEvent, boolean solved, String type, String itemIs, String description, String feelsLike, String usedAlone, event touchResult,
                    event useResult, boolean canTake) {
        super(type, itemIs, description, feelsLike, usedAlone, touchResult, useResult, canTake);
        this.solved = solved;
        this.solvedBy = solvedBy;
        this.resolvedMsg = resolvedMsg;
        this.resolveFailMsg = resolveFailMsg;
        this.alreadyResolvedMsg = alreadyResolvedMsg;
        this.usedWithoutSolveMsg = usedWithoutSolveMsg;
        this.useNonResolvedResult = useNonResolvedResult;
        if(this.useNonResolvedResult != null) this.useNonResolvedResult.setBelongsTo(this);
        this.resolveEvent = resolveEvent;
        if(this.resolveEvent != null) this.resolveEvent.setBelongsTo(this);
        this.canTake = false;   // no matter the input canTake - find a way to remove it, maybe give interactive a constructor without canTake
    }

    public event resolve(interactive usedWith) {
        String usedWithId = usedWith.getFullItemIs();

        if (!solved && usedWithId.endsWith(solvedBy)) {
            this.solved = true;
            System.out.println(this.resolvedMsg);
            return this.resolveEvent;   // if resolve succeeds return the resolve event
        }
        else if (!solved) System.out.println(resolveFailMsg);
        else System.out.println(alreadyResolvedMsg);

        return null;    // if either already resolved or resolve failed return null
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
    public event useOn(obstacle useOn) {   // should never happen
        System.out.println("why have you tried this - stop");
        return null;
    }


     public String addNumberObstacle(HashMap<String, obstacle> obstaclesPresent) {
        String newItemIs = this.getDisplayItemIs();

        if (obstaclesPresent.containsKey(this.getDisplayItemIs())) {

            int version = 2;
            newItemIs = formatItemIsToNumbered(this.getDisplayItemIs(), version);

            while (obstaclesPresent.containsKey(newItemIs)) {
                newItemIs = formatItemIsToNumbered(this.getDisplayItemIs(), version++);
            }
        }

        this.displayItemIs = newItemIs;

        return this.displayItemIs;
    }



    public boolean getSolved() {
        return solved;
    }

    public String getResolvedMsg() {
        return resolvedMsg;
    }

    public event getUseNonResolvedResult() {
        return useNonResolvedResult;
    }

    public String getUsedWithoutSolveMsg() {
        return usedWithoutSolveMsg;
    }

    public String getAlreadyResolvedMsg() {
        return alreadyResolvedMsg;
    }

    public String getResolveFailMsg() {
        return resolveFailMsg;
    }

    public String getSolvedBy() {
        return solvedBy;
    }

    public event getResolveEvent() { return resolveEvent; }
}
