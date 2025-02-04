package Backend.Weapons;

public class Axe extends Weapon{

    public Axe(String name, double dmg, String effect){
        super(name, dmg, effect);
    }

    @Override
    public void attack(){
        System.out.println("attacking but with an axe");
    }
}
