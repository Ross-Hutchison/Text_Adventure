public class obstacle extends item {
    private boolean solved;
    private item solvedBy;

    public obstacle(item solvedBy, String itemIs, String description, String feelsLike, String tastesLike) {
        super(itemIs, description, feelsLike, tastesLike);
        this.solved = false;
        this.solvedBy = solvedBy;
    }
}
