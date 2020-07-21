public abstract class obstacle extends item {
    private boolean solved;
    private item solvedBy;

    public obstacle(item solvedBy, String itemIs, String description, String feelsLike, String tastesLike, String usedAlone) {
        super(itemIs, description, feelsLike, tastesLike, usedAlone);
        this.solved = false;
        this.solvedBy = solvedBy;
    }

    public void resolve(item usedWith){

    }
}
