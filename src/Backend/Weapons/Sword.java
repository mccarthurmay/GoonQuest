package Backend.Weapons;

public class Sword extends Weapon{
    public Sword(String name, double dmg, String effect, double crit, double hit){
        super(name, dmg, effect, crit, hit);
    }

    @Override
    public void attack(){
        System.out.println("Attacking");
        if (effect.equals("fire")){
            System.out.println("Oh my god he's on fire");
        }
    }

}
