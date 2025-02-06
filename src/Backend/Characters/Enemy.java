package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;

import java.util.ArrayList;

public class Enemy extends CharacterManager{

    public Enemy(Stats stats, Weapon weapon, String name, ArrayList<Item> ownedItems){
        super(stats, weapon, name);
        this.ownedItems = ownedItems;
    }

}



