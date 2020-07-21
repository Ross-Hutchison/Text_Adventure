public class player {
    private int inventorySize = 5;
    private int currentlyHolding = 0;
    private item[] inventory = new item[inventorySize];

    public boolean addToInventory(item toAdd) {
        if(currentlyHolding != inventorySize) {
            inventory[currentlyHolding++] = toAdd;
            return true;
        }
        else return false;
    }

    public void switchWith(item toLeave, item toTake, room whereToLeave){

    }
}
