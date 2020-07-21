public class item {
    private String itemIs;
    private String description;
    private String feelsLike;
    private String tastesLike;

    public item(String itemIs, String description, String feelsLike, String tastesLike) {
        this.itemIs = itemIs;
        this.description = description;
        this.feelsLike = feelsLike;
        this.tastesLike = tastesLike;
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

    public void lick() {
        System.out.println(this.tastesLike);
    }
}
