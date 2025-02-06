package Backend.WorldBuilding;

import Display.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap("/Backend/Maps/pre_map.txt");

    }



    public void getTileImage() {
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("src/Backend/Images/Tiles/output_tileset/basic_tile_sprites14.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("src/Backend/Images/Tiles/output_tileset/basic_tile_sprites10.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("src/Backend/Images/Tiles/output_tileset/basic_tile_sprites25.png"));





        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(String map){
        try{
            InputStream is = getClass().getResourceAsStream(map);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine();
                while(col < gp.maxScreenCol){
                    String numbers[] = line.split(" ");

                    // Changing the numbers from string to integers
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if( col == gp.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close();


        }catch(Exception e){}
    }




    public void draw(Graphics g2) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col< gp.maxScreenCol && row< gp.maxScreenRow) {

            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
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
