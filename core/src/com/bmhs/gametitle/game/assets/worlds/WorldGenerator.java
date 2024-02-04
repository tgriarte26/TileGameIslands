package com.bmhs.gametitle.game.assets.worlds;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;
import jdk.internal.net.http.common.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;


public class WorldGenerator {

    private int worldMapRows, worldMapColumns;

    private int[][] worldIntMap;

    private int seedColor,lightGreen;




    public WorldGenerator (int worldMapRows, int worldMapColumns) {
        this.worldMapRows = worldMapRows;
        this.worldMapColumns = worldMapColumns;

        worldIntMap = new int[worldMapRows][worldMapColumns];

        //call methods to build 2D array

        seedColor = 2;
        lightGreen = 18;

        Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
        System.out.println(mapSeed.y + "" + mapSeed.x);

        worldIntMap[(int)mapSeed.y][(int)mapSeed.x] = 4;

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = 6;
                }
            }
        }


        //randomize();
        //seedMap(4);
        water();
        seedIslands((int)(Math.random() * 3 + 3));
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
                worldIntMap[r][c] = 20;
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
        int expansionRadius = MathUtils.random(20,50);
        Set<int[]> visited = new HashSet<>();
        //expandNeighboringTiles(row, column, seedColor, 6, visited);
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if (worldIntMap[r][c] == seedColor) {
                    for (int i = 0; i < expansionRadius; i++) {
                        int directions = MathUtils.random(5, 10);
                        for (int j = 0; j < directions; j++){
                            int direction = MathUtils.random(0,7);
                        switch (direction) {
                            case 0:
                                expandIsland(row - 1, column);
                                row--;
                                break;
                            case 1:
                                expandIsland(row, column + 1);
                                column++;
                                break;
                            case 2:
                                expandIsland(row + 1, column);
                                row++;
                                break;
                            case 3:
                                expandIsland(row, column - 1);
                                column--;
                                break;
                            case 4:
                                expandIsland(row - 1, column - 1);
                                row--;
                                column--;
                                break;
                            case 5:
                                expandIsland(row - 1, column + 1);
                                row--;
                                column++;
                                break;
                            case 6:
                                expandIsland(row + 1, column - 1);
                                row++;
                                column--;
                                break;
                            case 7:
                                expandIsland(row + 1, column + 1);
                                row++;
                                column++;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    /*
    private void expandNeighboringTiles(int row, int col, int targetColor, int newColor, Set<int[]> visited) {
        int[] currentTile = {row, col};

        if (row < 0 || row >= worldIntMap.length || col < 0 || col >= worldIntMap[row].length || visited.contains(currentTile) || worldIntMap[row][col] != targetColor) {
            return; // Exit if out of bounds, already visited, or not the target color
        }

        visited.add(currentTile);
        worldIntMap[row][col] = newColor; // Set the current tile to the new color

        // Recursively expand to neighboring tiles
        expandNeighboringTiles(row - 1, col, targetColor, newColor, visited); // Up
        expandNeighboringTiles(row, col + 1, targetColor, newColor, visited); // Right
        expandNeighboringTiles(row + 1, col, targetColor, newColor, visited); // Down
        expandNeighboringTiles(row, col - 1, targetColor, newColor, visited); // Left
        expandNeighboringTiles(row - 1, col - 1, targetColor, newColor, visited); // Up-Left
        expandNeighboringTiles(row - 1, col + 1, targetColor, newColor, visited); // Up-Right
        expandNeighboringTiles(row + 1, col - 1, targetColor, newColor, visited); // Down-Left
        expandNeighboringTiles(row + 1, col + 1, targetColor, newColor, visited); // Down-Right
    }

     */

    private void expandIsland(int row, int column) {
        if (row >= 0 && row < worldIntMap.length && column >= 0 && column < worldIntMap[row].length && worldIntMap[row][column] != seedColor) {
            worldIntMap[row][column] = 6; // Assuming 6 represents part of the island, you can adjust this value
            if (row < 0 || row >= worldIntMap.length || column < 0 || column >= worldIntMap[row].length || worldIntMap[row][column] == seedColor) {
                return;  // Terminate recursion if out of bounds or already part of the island
            }
            expandIsland(row - 1, column);
            expandIsland(row, column + 1);
            expandIsland(row + 1, column);
            expandIsland(row, column - 1);
            expandIsland(row - 1, column - 1);
            expandIsland(row - 1, column + 1);
            expandIsland(row + 1, column - 1);
            expandIsland(row + 1, column + 1);
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