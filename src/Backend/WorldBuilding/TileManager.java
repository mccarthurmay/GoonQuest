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
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        loadMap("/Backend/Maps/larger_map.txt");
        getTileImage();
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

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();
                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");

                    // Changing the numbers from string to integers
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if( col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();


        }catch(Exception e){}
    }




    public void draw(Graphics g2) {

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol< gp.maxWorldCol && worldRow< gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.hero.worldX + gp.hero.screenX;
            int screenY = worldY - gp.hero.worldY + gp.hero.screenY;

            if(worldX + gp.tileSize > gp.hero.worldX - gp.hero.screenX && worldX - gp.tileSize < gp.hero.worldX + gp.hero.screenX && worldY + gp.tileSize > gp.hero.worldY - gp.hero.screenY && worldY - gp.tileSize < gp.hero.worldY + gp.hero.screenY){
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;

            }
        }
    }
}
