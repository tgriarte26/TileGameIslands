package com.bmhs.gametitle.game.assets.worlds;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;

import static com.badlogic.gdx.math.MathUtils.random;


public class WorldGenerator {

    private int worldMapRows, worldMapColumns;

    private int[][] worldIntMap;

    private int seedColor,grass, lightGrass, dirt, lightDirt, sand, water;

    public WorldGenerator (int worldMapRows, int worldMapColumns) {
        this.worldMapRows = worldMapRows;
        this.worldMapColumns = worldMapColumns;

        worldIntMap = new int[worldMapRows][worldMapColumns];

        //call methods to build 2D array

        seedColor = 17;
        grass = 78;
        lightGrass = 62;
        dirt = 49;
        lightDirt = 40;
        sand = 30;
        water = 20;

        Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
        System.out.println(mapSeed.y + "" + mapSeed.x);

        worldIntMap[(int)mapSeed.y][(int)mapSeed.x] = 4;

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = seedColor;
                }
            }
        }

        //randomize();
        //seedMap(4);
        water();
        seedIslands((int)(Math.random() * 4 + 4));

        /*
        searchAndExpand(MathUtils.random(8,9), seedColor, 39, 0.10);
        searchAndExpand(MathUtils.random(6,7), seedColor, 31, 0.20);
        searchAndExpand(MathUtils.random(4,5), seedColor, 49, 0.30);
        searchAndExpand(MathUtils.random(2,3), seedColor, 62, 0.40);
        searchAndExpand(MathUtils.random(1,2), seedColor, 78, 0.50);
        */

        generateWorldTextFile();

        Gdx.app.error("WorldGenerator", "WorldGenerator(WorldTile[][][])");
    }

    public String getWorld3DArrayToString() {
        String returnString = "";

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                returnString += worldIntMap[r][c] + " ";
            }
            returnString += "\n";
        }

        return returnString;
    }

    public void water(){
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = water;
            }
        }
    }

    public void seedMap(int num) {
        for(int i = 0; i < num; i++){
            Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
            for(int r = 0; r < worldIntMap.length; r++) {
                for(int c = 0; c < worldIntMap[r].length; c++) {
                    Vector2 tempVector = new Vector2(c,r);
                    if(tempVector.dst(mapSeed) < 10) {
                        worldIntMap[r][c] = 56;
                    }
                }
            }
        }
    }

    private void seedIslands(int num) {
        for(int i = 0; i < num; i++) {
            int rSeed = random(worldIntMap.length-1);
            int cSeed = random(worldIntMap[0].length-1);
            worldIntMap[rSeed][cSeed] = seedColor;
            randomIslandExpansion(rSeed, cSeed);
        }
    }

    //Goal:
    // Make islands of different sizes, not just a circle or a square
    // Grow seed in any direction and get different results
    private void randomIslandExpansion(int row, int column) {
        int expansionRadius = MathUtils.random(4,7);

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if (worldIntMap[r][c] == seedColor) {
                    for (int i = 0; i < expansionRadius; i++) {
                        int directions = MathUtils.random(5,7);
                        for (int j = 0; j < directions; j++){
                            int direction = MathUtils.random(0,8);
                            switch (direction) {
                                case 0:
                                    row--;
                                    surroundTileWithGrass(row - 1, column);
                                    surroundTileWithLightGrass(row - 2, column);
                                    surroundTileWithDirt(row - 3, column);
                                    surroundTileWithLightDirt(row - 4, column);
                                    surroundTileWithSand(row - 5, column);
                                    expandIsland(row - 1, column);
                                    break;
                                case 1:
                                    column++;
                                    surroundTileWithGrass(row, column + 1);
                                    surroundTileWithLightGrass(row, column + 2);
                                    surroundTileWithDirt(row, column + 3);
                                    surroundTileWithLightDirt(row, column + 4);
                                    surroundTileWithSand(row, column + 5);
                                    expandIsland(row, column + 1);
                                    break;
                                case 2:
                                    row++;
                                    surroundTileWithGrass(row + 1, column);
                                    surroundTileWithLightGrass(row + 2, column);
                                    surroundTileWithDirt(row + 3, column);
                                    surroundTileWithLightDirt(row + 4, column);
                                    surroundTileWithSand(row + 5, column);
                                    expandIsland(row + 1, column);
                                    break;
                                case 3:
                                    column--;
                                    surroundTileWithGrass(row, column - 1);
                                    surroundTileWithLightGrass(row, column - 2);
                                    surroundTileWithDirt(row, column - 3);
                                    surroundTileWithLightDirt(row, column - 4);
                                    surroundTileWithSand(row, column - 5);
                                    expandIsland(row, column - 1);
                                    break;
                                case 4:
                                    row--;
                                    column--;
                                    surroundTileWithGrass(row - 1, column - 1);
                                    surroundTileWithLightGrass(row - 2, column - 2);
                                    surroundTileWithDirt(row - 3, column - 3);
                                    surroundTileWithLightDirt(row - 4, column - 4);
                                    surroundTileWithSand(row - 5, column - 5);
                                    expandIsland(row - 1, column - 1);
                                    break;
                                case 5:
                                    row--;
                                    column++;
                                    surroundTileWithGrass(row - 1, column + 1);
                                    surroundTileWithLightGrass(row - 2, column + 2);
                                    surroundTileWithDirt(row - 3, column + 3);
                                    surroundTileWithLightDirt(row - 4, column + 4);
                                    surroundTileWithSand(row - 5, column + 5);
                                    expandIsland(row - 1, column + 1);
                                    break;
                                case 6:
                                    row++;
                                    column--;
                                    surroundTileWithGrass(row + 1, column - 1);
                                    surroundTileWithLightGrass(row + 2, column - 2);
                                    surroundTileWithDirt(row + 3, column - 3);
                                    surroundTileWithLightDirt(row + 4, column - 4);
                                    surroundTileWithSand(row + 5, column - 5);
                                    expandIsland(row + 1, column - 1);
                                    break;
                                case 7:
                                    row++;
                                    column++;
                                    surroundTileWithGrass(row + 1, column + 1);
                                    surroundTileWithLightGrass(row + 2, column + 2);
                                    surroundTileWithDirt(row + 3, column + 3);
                                    surroundTileWithLightDirt(row + 4, column + 4);
                                    surroundTileWithSand(row + 5, column + 5);
                                    expandIsland(row + 1, column + 1);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void surroundTileWithGrass(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if(worldIntMap[r][c] != seedColor){
                        worldIntMap[r][c] = grass;
                    }
                }
            }
        }
    }

    private void surroundTileWithLightGrass(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass) {
                        worldIntMap[r][c] = lightGrass;
                    }
                }
            }
        }
    }

    private void surroundTileWithDirt(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != lightGrass) {
                        worldIntMap[r][c] = dirt;
                    }
                }
            }
        }
    }

    private void surroundTileWithLightDirt(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != lightGrass && worldIntMap[r][c] != dirt) {
                        worldIntMap[r][c] = lightDirt;
                    }
                }
            }
        }
    }

    private void surroundTileWithSand(int centerRow, int centerCol) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {
                    if (worldIntMap[r][c] != seedColor && worldIntMap[r][c] != grass && worldIntMap[r][c] != lightGrass && worldIntMap[r][c] != dirt && worldIntMap[r][c] != lightDirt) {
                        worldIntMap[r][c] = sand;
                    }
                }
            }
        }
    }

    private void expandIsland(int row, int column) {
        if (row >= 0 && row < worldIntMap.length && column >= 0 && column < worldIntMap[row].length && worldIntMap[row][column] != seedColor) {
            worldIntMap[row][column] = grass;
        }
    }
    private void searchAndExpand(int radius, int numToFind, int numToWrite, double probability) {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if(worldIntMap[r][c] == numToFind) {
                    for(int subRow = r-radius; subRow <= r+radius; subRow++) {
                        for(int subCol = c-radius; subCol <= c+radius; subCol++) {
                            if(subRow >= 0 && subCol >= 0 && subRow <= worldIntMap.length-1 && subCol <= worldIntMap[0].length-1 && worldIntMap[subRow][subCol] != numToFind) {
                                if(Math.random() > probability) {
                                    worldIntMap[subRow][subCol] = numToWrite;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void randomize() {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = random(TileHandler.getTileHandler().getWorldTileArray().size-1);
            }
        }
    }

    public WorldTile[][] generateWorld() {
        WorldTile[][] worldTileMap = new WorldTile[worldMapRows][worldMapColumns];
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldTileMap[r][c] = TileHandler.getTileHandler().getWorldTileArray().get(worldIntMap[r][c]);
            }
        }
        return worldTileMap;
    }

    private void generateWorldTextFile() {
        FileHandle file = Gdx.files.local("assets/worlds/world.text");
        file.writeString(getWorld3DArrayToString(), false);
    }

}