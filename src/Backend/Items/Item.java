package Backend.Items;
import Backend.Characters.*;

public abstract class Item {
    String name;


    public Item(String name) {
        this.name = name;
    }

    public abstract void useItem(CharacterManager character);


}
