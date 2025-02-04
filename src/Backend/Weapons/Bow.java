package Backend.Weapons;

public class Bow extends Weapon{

    public Bow(String name, double dmg, String effect, double crit, double hit){
        super(name, dmg, effect, crit, hit);
    }

    @Override
    public void attack(){
        System.out.println("attacking but with a bow");
        if (effect.equals("light")){
            System.out.println("OH MY GOD HE IS SO BRIGHT");
        }
    }
}
