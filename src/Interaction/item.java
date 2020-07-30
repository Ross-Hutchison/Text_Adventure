package Interaction;

public class item {
     String itemIs;
     String description;
     String feelsLike;
     String tastesLike;
     String usedAlone;
     String touchResult;
     String tasteResult;
     String useResult;

    public item(String itemIs, String description, String feelsLike, String tastesLike, String usedAlone, String touchResult, String tasteResult, String useResult) {
        this.itemIs = itemIs;
        this.description = description;
        this.feelsLike = feelsLike;
        this.tastesLike = tastesLike;
        this.usedAlone = usedAlone;
        this.touchResult = touchResult;
        this.tasteResult = tasteResult;
        this.useResult = useResult;
    }

    public String getItemIs() {
        return this.itemIs;
    }

    public void setUseResult(String useResult) { this.useResult = useResult; }

    public void lookAt() {
        System.out.println(this.description);
    }

    public String touch() {
        System.out.println(this.feelsLike);
        return this.touchResult;
    }

    public String taste() {
        System.out.println(this.tastesLike);
        return this.tasteResult;
    }

    public String use(){
        System.out.println(this.usedAlone);
        return this.useResult;
    }

    public void useOn(obstacle useOn){
        useOn.resolve(this);
    }
}
