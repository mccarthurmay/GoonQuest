package Backend.Weapons;


public abstract class Weapon {
    String name;
    double dmg;
    String effect;
    double crit;
    double hit;
// double durability???

    public Weapon(String name, double dmg, String effect, double crit, double hit) {
        this.name = name;
        this.dmg = dmg;
        this.effect = effect;
        this.crit = crit;
        this.hit = hit;
    }

    public abstract void attack();

}
