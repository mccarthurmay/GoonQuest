package Backend.WorldBuilding;

import Display.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class TileManager {

    GamePanel gp;
    Tile[] tile;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        getTileImage();

    }

    public void getTileImage() {
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("src/Backend/Images/Tiles/output_tileset/basic_tile_sprites14.png"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col< gp.maxScreenCol && row< gp.maxScreenRow) {
            g2.drawImage(tile[0].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x+=gp.tileSize;
            if(col == gp.maxScreenCol) {
                col = 0;
                row++;
                x=0;
                y+= gp.tileSize;
            }
        }
    }
}
