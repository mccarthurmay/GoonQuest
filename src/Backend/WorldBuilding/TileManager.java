package Backend.WorldBuilding;

import Display.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[40000];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        loadMap("/Backend/Maps/data2.txt");
        getTileImage();
    }



    public void getTileImage() {
        File dir = new File("src/Backend/Images/unique_tiles");
        File[] directoryListing = dir.listFiles();
        int i = 0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String filename = child.getName();
                String myNum = "";
                if (filename.length() > 12){
                    myNum = filename.substring(4, 9);
                } else {
                    myNum = filename.substring(4, 8);
                }
                int newNum = Integer.parseInt(myNum);
                tile[newNum] = new Tile();
                try {
                    System.out.println(child.getAbsolutePath());
                    tile[newNum].image = ImageIO.read(new File(child.getAbsolutePath()));

                } catch(Exception e){
                    System.out.println(e);
                }
                i++;
                // Do something with child
            }
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
                System.out.println(tileNum);
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
