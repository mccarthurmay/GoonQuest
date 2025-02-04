package Backend.Weapons;


public abstract class Weapon {
    String name;
    double dmg;
    String effect;
// double durability???

    public Weapon(String name, double dmg, String effect) {
        this.name = name;
        this.dmg = dmg;
        this.effect = effect;
    }

    public abstract void attack();

}
