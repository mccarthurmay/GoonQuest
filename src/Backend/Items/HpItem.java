package Backend.Items;


public class HpItem {

    String name;
    double hpGain;

    public HpItem(String name, double hpGain) {
        this.name = name;
        this.hpGain = hpGain;
    }

    public String getName() {
        return name;
    }

    public double getHpGain() {
        return hpGain;
    }

}


// Milk
// Tree Bark
// Etc... we can have fun with item stuff