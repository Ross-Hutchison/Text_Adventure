import java.util.HashMap;

public abstract class room {
    private String description; // a brief description o the current room
    private item[] items;   // an array of the items in the current room - if later allow dropping may need to make a list but for now number of items per room is fixed
    private obstacle[] obstacles;   // an array of all obstacles in the current room
    private HashMap<item, obstacle> blocked;    // a map that shows what obstacle blocks each item (if any) - so the program knows if the user can approach them

}
