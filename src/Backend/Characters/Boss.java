package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;
import Display.GamePanel;

public class Boss extends CharacterManager{

    public Boss(Stats stats, Weapon weapon, String name, GamePanel gp) {
        super(stats, weapon, name);
    }
}

