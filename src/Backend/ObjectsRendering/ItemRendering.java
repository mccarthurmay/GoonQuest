package Backend.ObjectsRendering;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ItemRendering extends Superobjects{
    public ItemRendering(String path){
        name  = "Item";
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;

    }
}
