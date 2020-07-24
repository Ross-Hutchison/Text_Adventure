package Interaction;

public abstract class item {
     String itemIs;
     String description;
     String feelsLike;
     String tastesLike;
     String usedAlone;

    public item(){

    }

    public item(String itemIs, String description, String feelsLike, String tastesLike, String usedAlone) {
        this.itemIs = itemIs;
        this.description = description;
        this.feelsLike = feelsLike;
        this.tastesLike = tastesLike;
        this.usedAlone = usedAlone;
    }

    public String getItemIs() {
        return this.itemIs;
    }

    public void lookAt() {
        System.out.println(this.description);
    }

    public void touch() {
        System.out.println(this.feelsLike);
    }

    public void taste() {
        System.out.println(this.tastesLike);
    }

    public void use(){
        System.out.println(this.usedAlone);
    }

    public void useOn(obstacle useOn){
        useOn.resolve(this);
    }
}
