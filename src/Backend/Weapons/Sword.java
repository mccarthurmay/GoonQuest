package Backend.Weapons;



public class Sword extends Weapon{
    public Sword(String name, double dmg, String effect){
        super(name, dmg, effect);
    }

    @Override
    public void attack(){
        System.out.println("Attacking");
        if (effect.equals("fire")){
            System.out.println("Oh my god he's on fire");
        }
    }

}
