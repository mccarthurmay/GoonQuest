package Backend.Weapons;

public class Axe extends Weapon{

    public Axe(String name, double dmg, String effect, double crit, double hit){
        super(name, dmg, effect, crit, hit);
    }

    @Override
    public void attack(){
        System.out.println("attacking but with an axe");
    }
}
