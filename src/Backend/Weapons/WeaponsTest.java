package Backend.Weapons;

public class WeaponsTest {
    public static void main(String[] args) {
        Sword FlameSword = new Sword("Dog poop", 200000, "fire");
        FlameSword.attack();

        Bow LightBow = new Bow("Bow", 200000, "light");
        LightBow.attack();

        Axe NormalAxe = new Axe("Bow", 1, "none");
        NormalAxe.attack();
    }
}