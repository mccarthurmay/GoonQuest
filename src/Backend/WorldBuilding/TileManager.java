package Backend.WorldBuilding;

import Display.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
                    tile[newNum].image = ImageIO.read(new File(child.getAbsolutePath()));

                } catch(Exception e){
                    System.out.println(e);
                }
                i++;
                // Do something with child
            }
        }

        ArrayList<Integer> collisionTiles = new ArrayList<Integer>(Arrays.asList(0,1,2,60,74,75,120,121,135,136,260,274,275,341,342,460,476,477,478,479,507,519,541,542,676,677,678,679,696,697,698,707,714,715,720,722,735,740,741,742,743,800,801,802,860,874,875,890,891,892,896,897,898,941,942,
                1060,1074,1075, 1090,1091,1092, 1093, 1096, 1097, 1098, 1109, 1110, 1128, 1141,1142, 1260, 1290, 1291, 1292, 1293, 1294, 1295, 1296, 1308, 1309, 1310, 1311, 1486, 1487, 1488, 1493, 1494, 1495, 1496, 1497, 1498, 1508, 1509, 1510, 1514, 1515, 1693, 1694, 1695, 1696, 1697, 1698,
                1700, 1701, 1702, 1708, 1709, 1710, 1711, 1714, 1715, 1880, 1881, 1906, 1908, 1909, 1910, 1911, 1912, 2000, 2012, 2013, 2080, 2081, 2085, 2086, 2106, 2107, 2200, 2212, 2213, 2279, 2280, 2281, 2282, 2284, 2285, 2286, 2287, 2400, 2412, 2413, 2484, 2485, 2486, 2487, 2525, 2548,
                2549, 2550, 2610, 2611, 2684, 2685, 2686, 2087, 2748, 2749, 2750, 2815, 2816, 2817, 2818, 2819, 2820, 2821, 2822, 2827, 2828, 2829, 2830, 2836, 2847, 2848, 2849, 2948, 2949, 2950 , 3015, 3016, 3017, 3018, 3019, 3020, 3021, 3022, 3023, 3024, 3025, 3027, 3028, 3030, 3047, 3048,
                3049, 3050, 3097, 3102, 3103, 3104, 3215, 3216, 3217, 3218, 3219, 3220, 3221, 3222, 3223, 3224, 3225, 3226, 3227, 3228, 3229, 3230, 3231, 3232, 3233, 3247, 3248, 3249, 3250, 3297, 3302, 3303, 3304, 3308, 3309, 3310, 3311, 3418, 3442, 3451, 3452, 3503, 3504, 3508, 3509, 3510,
                3511, 3613, 3614, 3615, 3651, 3652, 3702, 3703, 3704, 3708, 3709, 3710, 3711, 3813, 3815, 3820, 3851, 3852, 3855, 3904, 4013, 4014, 4015, 4020, 4021, 4022, 4052, 4053, 4054, 4055, 4093, 4096, 4097, 4098, 4252, 4253, 4254, 4280, 4293, 4297, 4312, 4377, 4392, 4452, 4453, 4454,
                4480, 4631, 4632, 4636, 4641, 4643, 4684, 4685, 4820, 4823, 4824, 5022, 5025, 5027, 5045, 5215, 5216, 5227, 5245, 5298, 5299, 5306, 5414, 5417, 5422, 5425, 5427, 5445, 5499, 5617, 5622, 5623, 5624, 5625, 5645, 5654, 5655, 5657, 5686, 5687, 5688, 5689, 5690, 5691, 5698, 5701,
                5706, 5814, 5817, 5836, 5837, 5886, 5887, 5888, 5889, 5890, 5891, 5895, 5896, 6006, 6010, 6011, 6014, 6015, 6016, 6017, 6020, 6027, 6036, 6037, 6045, 6053, 6054, 6060, 6074, 6075, 6081, 6082, 6083, 6084, 6086, 6087, 6088, 6089, 6090, 6091, 6095, 6096, 6106, 6107, 6109, 6110,
                6111, 6113, 6114, 6119, 6120, 6130, 6135, 6161, 6162, 6197, 6198, 6200, 6210, 6211, 6223, 6224, 6227, 6245, 6260, 6274, 6275, 6276, 6277, 6298, 6299, 6300, 6301, 6306, 6307, 6309, 6310, 6311, 6313, 6314, 6397, 6398, 6400, 6420, 6422, 6423, 6424, 6425, 6427, 6445, 6460, 6476,
                6477, 6495, 6496, 6497, 6505, 6506, 6507, 6508, 6530, 6531, 6532, 6597, 6598, 6613, 6614, 6615, 6623, 6624, 6625, 6695, 6730, 6731, 6732, 6733, 6813, 6814, 6815, 6822, 6823, 6824, 6825, 6887, 6888, 6889, 6928, 6930, 6931, 6932, 6933, 7013, 7014, 7015, 7022, 7023, 7024, 7025,
                7028, 7087, 7088, 7089, 7096, 7097, 7103, 7104, 7109, 7213, 7214, 7215, 7258, 7259, 7287, 7288, 7289, 7291, 7295, 7298, 7303, 7309, 7326, 7384, 7385, 7386, 7491, 7495, 7499, 7522, 7584, 7585, 7586, 7614, 7615, 7616, 7646, 7647, 7695, 7698, 7699, 7784, 7785, 7786, 7814, 7815, 7816,
                7817, 7829, 7830, 7831, 7851, 7852, 7895, 7896, 7898, 7909, 7915, 7916, 7917, 8014, 8015, 8016, 8017, 8029, 8030, 8031, 8051, 8052, 8058, 8059, 8085, 8087, 8100, 8106, 8107, 8108, 8113, 8114, 8115, 8117, 8229, 8230, 8231, 8232, 8233, 8285, 8287, 8290, 8300, 8306, 8307, 8308, 8313,
                8314, 8315, 8317, 8429, 8430, 8431, 8432, 8433, 8485, 8486, 8487, 8496, 8503, 8505, 8509, 8514, 8515, 8516, 8517, 8629, 8630, 8631, 8634, 8636, 8685, 8686, 8812, 8813, 8814, 8885, 8886, 8887, 8889, 8890, 8891, 8892, 9010, 9011, 9012, 9013, 9014, 9034, 9036, 9089, 9090,
                9091, 9092, 9201, 9202, 9210, 9211, 9212, 9273, 9289, 9290, 9291, 9292, 9401, 9420, 9466, 9467, 9468, 9666, 9667, 9668, 9677, 9686, 9687, 9726, 9863, 9864, 9865, 9866, 9867, 9868, 10063, 10064, 10065, 10066, 10124, 10264, 10265, 10266, 10286, 10296, 10297, 10312, 10464, 10465, 10466,
                10496, 10497, 10498, 10499, 10500, 10512, 10550, 10551, 10552, 10664, 10665, 10666, 10696, 10697, 10698, 10700, 10750, 10751, 10752, 10898, 10900, 10950, 10951, 10952, 11125, 11128, 11135, 11190, 11191, 11197, 11198, 11201, 11203, 11204, 11209, 11269, 11274, 11275, 11310, 11319, 11326,
                11327, 11397, 11398, 11400, 11474, 11475, 11510, 11597, 11598, 11787, 11788, 11789, 11987, 11988, 11989, 12000, 12001, 12002, 12059, 12075, 12157, 12158, 12159, 12187, 12188, 12200, 12201, 12243, 12244, 12357, 12358, 12359, 12400, 12401, 12402, 12443, 12444, 12557, 12558, 12559, 12726,
                12822, 13416, 14270, 14271, 14274, 14275, 14319, 14330, 14331, 14470, 14471, 14473, 14474, 15371, 15372, 15373, 15571, 15572, 15573, 15771, 15773, 16018, 16019, 16074, 16076, 16077, 16082, 16083, 16084, 16121, 16217, 16218, 16219, 16220, 16275, 16276, 16277, 16278, 16417, 16418, 16419, 16420,
                16475, 16476, 16477, 16478, 16482, 16484, 16597, 16598, 16600, 16601, 16602, 16608, 16674, 16675, 16676, 16720, 16721, 16722, 16724, 16730, 16800, 16801, 16802, 16864, 16865, 16877, 16878, 16880, 16881, 16882, 16996, 16997, 16998, 16999, 17000, 17001, 17064, 17065, 17080, 17081, 17082, 17126, 17277,
                17278, 17279, 17281, 17411, 17412, 17460, 17461, 17611, 17612, 17661, 17662, 17674, 17690, 17713, 17721, 17861, 17862, 17874, 17895, 17937, 17938, 18061, 18062, 18063, 18074, 18085, 18086, 18138, 18172, 18236, 18261, 18262, 18263, 18284, 18285, 18336, 18337, 18372, 18661, 18662, 18663, 18735, 18736, 18861,
                18862, 18863, 18879, 18880, 18900, 18934, 18935, 18936, 18937, 19079, 19080, 19200, 19217, 19218, 19219, 19400, 19401, 19406, 19407, 19408, 19409, 19417, 19418, 19419, 19428, 19460, 19479, 19563, 19564, 19600, 19601, 19606, 19607, 19608, 19609, 19610, 19611, 19612, 19613, 19614, 19615, 19617, 19618, 19619, 19636, 19681,
                19682, 19713, 19763, 19764, 19800, 19801, 19806, 19807, 19808, 19809, 19810, 19811, 19812, 19813, 19814, 19815, 19817, 19818, 19819, 19832, 19881, 19882, 19913, 20006, 20007, 20008, 20009, 20010, 20011, 20012, 20013, 20014, 20015, 20017, 20018, 20019, 20059, 20060, 20081, 20082, 20150, 20175, 20204, 20205, 20213, 20214,
                20215, 20217, 20218, 20219, 20259, 20260, 20281, 20350, 20375, 20376, 20377, 20378, 20404, 20405, 20418, 20497, 20532, 20576, 20577, 20578, 20589, 20590, 20591, 20592, 20604, 20605, 20618, 20732, 20734, 20777, 20789, 20790, 20791, 20792, 20823, 20824, 20935, 20954, 20955, 20956, 21023, 21024, 21100, 21101, 21154, 21155,
                21156, 21245, 21246, 21300, 21301, 21354, 21356, 21369, 21371, 21446, 21447, 21465, 21466, 21467, 21474, 21476, 21569, 21604, 21605, 21606, 21617, 21618, 21619, 21646, 21647, 21665, 21666, 21667, 21727, 21730, 21764, 21765, 21766, 21796, 21797, 21798, 21799, 21800, 21801, 21804, 21805, 21806, 21825, 21826, 21859, 21860,
                21861, 21866, 21875, 21881, 21882, 21883, 21887, 21909, 21915, 21962, 21964, 21965, 21966, 21975, 21976, 21977, 21978, 21997, 21998, 22000, 22001, 22004, 22005, 22006, 22025, 22026, 22059, 22060, 22096, 22162, 22164, 22165, 22175, 22176, 22177, 22178, 22197, 22198, 22204,
                22205, 22206, 22207, 22208, 22214, 22215, 22220, 22242, 22281, 22313, 22318, 22326, 22372, 22373, 22376, 22378, 22404, 22405, 22406, 22407, 22408, 22409, 22420, 22442, 22461, 22572, 22573, 22604, 22605, 22606, 22607, 22608, 22609, 22617, 22619, 22661, 22662, 22878, 22879, 22913, 22804, 22805, 22984, 22985, 22986, 23004, 23005, 23006, 23007, 23017,
                23076, 23080, 23184, 23185, 23186, 23191, 23208, 23276, 23319, 23323, 23345, 23384, 23386, 23408, 23417, 23418, 23471, 23472, 23502, 23503, 23504, 23519, 23545, 23608, 23617, 23618, 23671, 23672, 23702, 23703, 23704, 23705, 23792, 23793, 23797, 23902, 23903, 23904, 23905, 23992, 23993, 24075, 24076, 24077, 24085, 24093, 24094, 24095, 24096, 24097, 24110,
                24275, 24276, 24277, 24293, 24294, 24295, 24296, 24297, 24509, 24709, 24745, 24839, 24909, 25109, 25168, 25410, 25459, 25461, 25462, 25463, 25487, 25488, 25509, 25515, 25516, 25517, 25611, 25659, 25660, 25661, 25662, 25663, 25687, 25688, 25859, 25860, 25861, 25862, 25863, 25887, 25888, 26016, 26216, 26607, 26608, 26656, 26677, 26678, 26877, 26878, 27086, 27099,
                27119, 27130, 27200, 27201, 27212, 27251, 27259, 27260, 27292, 27294, 27295, 27315, 27400, 27401, 27412, 27459, 27460, 27463, 27464, 27471, 27472, 27473, 27659, 27660, 27663, 27664, 27671, 27672, 27673, 27717, 27718, 27872, 27917, 27918, 28000, 28001, 28062, 28099, 28130, 28131, 28139, 28200, 28201, 28254, 28255, 28266, 28324, 28325, 28410, 28411, 28610, 28611,
                28886, 28887, 28888, 28967, 28968, 28969, 28974, 28975, 29086, 29087, 29088, 29089, 29155, 29167, 29168, 29173, 29174, 29175, 29271, 29286, 29287, 29288, 29289, 29349, 29354, 29356,
                29379, 29380, 29381, 29395, 29533, 29562, 29563, 29564, 29579, 29580, 29581, 29676, 29677, 29678, 29762, 29764, 29779, 29780, 29781, 29876, 29877, 29878, 29879, 29896, 29897, 29926, 30076, 30077, 30078, 30079, 30096, 30097, 30131, 30132, 30154, 30155, 30296, 30297, 30331, 30332, 30354, 30355, 30531, 30532, 30553, 30554, 30555, 30556,
                30713, 30789, 30790, 30980, 30981, 30989, 30990, 31149, 31150, 31151, 31184, 31185, 31196, 31197, 31385, 31386, 31396, 31397, 31398, 31583, 31584, 31585, 31586, 31784, 31785, 31786, 31971, 31972, 31973, 31974, 31983, 31984, 31985, 31986, 32168, 32169, 32170, 32171, 32172, 32173, 32174, 32175, 32176, 32177, 32178, 32368, 32369, 32370, 32371,
                32372, 32373, 32374, 32375, 32376, 32377, 32378, 32637, 32638, 32662, 32673, 32686, 32687, 32688, 32689, 32696, 32697, 32707, 32708, 32726, 32727, 32741, 32772, 32773, 32794, 32795, 32830, 32831, 32897, 32964, 32965, 32972, 32973, 32994, 32995, 33153, 33154, 33155, 33161, 33162, 33163, 33164, 33165, 33166, 33353, 33354, 33355, 33361, 33362, 33363,
                33364, 33365, 33366, 33563, 33564, 33565, 33566, 33583, 33584, 33726, 33727, 33744, 33745, 33746, 33763, 33764, 33765, 33766, 33783, 33784, 33926, 33927, 33944, 33945, 33946, 33968, 34085, 34086, 34087, 34126, 34127, 34144, 34145, 34146, 34168, 34169, 34285, 34286, 34287, 34288, 34344, 34345, 34346, 34373, 34374, 34475, 34476, 34485, 34486, 34487, 34488, 34544, 34545, 34546, 34573, 34574, 34590, 34591, 34610, 34611,
                34675, 34676, 34750, 34789, 34790, 34791, 34792, 34810, 34811, 34990, 34991, 34992, 35174, 35189, 35190, 35191, 35192, 35373, 35374, 35389, 35390, 35391, 35392, 35549, 35550, 35723, 35749, 35750, 35936, 35937, 35938, 36090, 36091, 36182, 36641, 36642, 36644, 36736, 36737, 36840, 36843, 36844, 36988, 36989, 37040, 37043, 37144, 37188, 37189, 37240,
                37243, 37145, 37386, 37387, 37388, 37389, 37440, 37441, 37443, 38242, 38248, 38249, 38254, 38255, 38260, 38261, 38262, 38276, 38303, 38322, 38361, 38362, 38386, 38387, 38399, 38446, 38460, 38461, 38646, 38732, 38733, 38734, 38741, 38742, 38771, 38772, 38902, 38903, 38932, 38933, 38934, 38941, 38942, 39102, 39103, 39132, 39133, 39134, 39302, 39303, 39332, 39333, 39334, 39532, 39533, 39534, 39961, 39998, 39999));

        for (Integer collisionTile : collisionTiles) {
            //System.out.println(collisionTile);
            tile[collisionTile].collision = true;
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
                //System.out.println(tileNum);
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
