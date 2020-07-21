public class obstacle extends item {
    private boolean solved;
    private item solvedBy;

    public obstacle(item solvedBy, String description, String itemIs, String feelsLike) {
        super(description, itemIs, feelsLike);
        this.solved = false;
        this.solvedBy = solvedBy;
    }
}
