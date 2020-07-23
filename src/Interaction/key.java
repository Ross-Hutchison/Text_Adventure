package Interaction;

public class key extends item {

    public key() {
        this.itemIs = "large key";
        this.description = "a heavy brass key of unusual size, the bow of which is a large ring about the size of your hand";
        this.feelsLike = "the metal is cool to the touch, and smooth, the bit is made up of several rectangular teeth";
        this.tastesLike = "biting the key is probably unwise, licking it leaves a metallic tang in your mouth";
        this.usedAlone = "you idly toss the key up by the bit and catch it by the bow. Very stylish";
    }

    public key(String itemIs, String description, String feelsLike, String tastesLike, String usedAlone){
        super(itemIs, description, feelsLike, tastesLike, usedAlone);
    }
}
