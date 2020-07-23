package Interaction;

public class chocolate extends item {

    public chocolate() {
        this.itemIs = "\"chocolate\"";
        this.description = "a bar of Schokolade brand chocolate, a cartoon chocolate bar smiles on the wrapper, " +
                "\"eat me\" its eyes seem to say, something is strangely disturbing about it";

        this.feelsLike = "the wrapper crinkles under you hand, you can feel 6 rows of 3 squares, 18 delicious bites";
        this.tastesLike = "despite the unease from how much the mascot wanted to be eaten, the chocolate is very delicious";
        this.usedAlone = "you contemplate the marketing choice behind the mascot. You don't come up with any answers";
    }

    public chocolate(String itemIs, String description, String feelsLike, String tastesLike, String usedAlone){
        super(itemIs, description, feelsLike, tastesLike, usedAlone);
    }
}
