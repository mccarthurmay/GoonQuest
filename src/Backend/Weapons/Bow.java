package Backend.Weapons;

public class Bow extends Weapon{

    public Bow(String name, double dmg, String effect){
        super(name, dmg, effect);
    }

    @Override
    public void attack(){
        System.out.println("attacking but with a bow");
        if (effect.equals("light")){
            System.out.println("OH MY GOD HE IS SO BRIGHT");
        }
    }
}
