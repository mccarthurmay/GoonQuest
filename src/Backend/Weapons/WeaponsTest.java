package Backend.Weapons;

public class WeaponsTest {
    public static void main(String[] args) {
        Sword FlameSword = new Sword("Dog poop", 200000, "fire", 0.035, 0.80);
        FlameSword.attack();

        Bow LightBow = new Bow("Bow", 200000, "light", 0.2, 0.65);
        LightBow.attack();

        Axe NormalAxe = new Axe("Bow", 1, "none", 0.15, 0.70);
        NormalAxe.attack();
    }
}