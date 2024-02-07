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
            //expandIslandAroundSeed(rSeed, cSeed);
        }
    }
    /*
    private void expandIslandAroundSeed(int seedRow, int seedColumn) {
        int islandColor = seedColor; // Color representing the island

        // Set the seed tile as part of the island
        worldIntMap[seedRow][seedColumn] = islandColor;

        // Start flood-fill algorithm to fill the island
        floodFill(seedRow, seedColumn, islandColor);
    }

    private void floodFill(int row, int col, int targetColor) {
        if (row < 0 || row >= worldIntMap.length || col < 0 || col >= worldIntMap[row].length || worldIntMap[row][col] != 0) {
            return; // Exit if out of bounds or not an empty space
        }

        // Set the current tile to the island color
        worldIntMap[row][col] = targetColor;

        // Recursively fill neighboring tiles
        floodFill(row - 1, col, targetColor); // Up
        floodFill(row + 1, col, targetColor); // Down
        floodFill(row, col - 1, targetColor); // Left
        floodFill(row, col + 1, targetColor); // Right
        floodFill(row - 1, col - 1, targetColor);
        floodFill(row - 1, col + 1, targetColor);
        floodFill(row + 1, col - 1, targetColor);
        floodFill(row + 1, col + 1, targetColor);
    }

     */



    //Goal:
    // Make islands of different sizes, not just a circle or a square
    // Grow seed in any direction and get different results
    private void randomIslandExpansion(int row, int column) {
        int expansionRadius = 30;
        Set<int[]> visited = new HashSet<>();
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                if (worldIntMap[r][c] == seedColor) {
                    for (int i = 0; i < expansionRadius; i++) {
                        int directions = MathUtils.random(10, 15);
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


    private void randomIslandExpansionHelper(int row, int column, int expansionRadius, int seedColor, int islandRepresentation, Set<int[]> visited) {
        expandNeighboringTiles(row - 1, column, seedColor, islandRepresentation, visited); // Up
        expandNeighboringTiles(row, column + 1, seedColor, islandRepresentation, visited); // Right
        expandNeighboringTiles(row+ 1, column, seedColor, islandRepresentation, visited); //Down
        expandNeighboringTiles(row, column - 1, seedColor, islandRepresentation, visited); //Left
        expandNeighboringTiles(row - 1, column - 1, seedColor, islandRepresentation, visited);
        expandNeighboringTiles(row - 1, column + 1, seedColor, islandRepresentation, visited);
        expandNeighboringTiles(row + 1, column - 1, seedColor, islandRepresentation, visited);
        expandNeighboringTiles(row + 1, column + 1, seedColor, islandRepresentation, visited);
    }

    private void expandNeighboringTiles(int row, int col, int targetColor, int newColor, Set<int[]> visited) {
        int[] currentTile = {row, col};

        if (row < 0 || row >= worldIntMap.length || col < 0 || col >= worldIntMap[row].length || visited.contains(currentTile) || worldIntMap[row][col] != targetColor) {
            return; // Exit if out of bounds, already visited, or not the target color
        }

        visited.add(currentTile);
        worldIntMap[row][col] = newColor; // Set the current tile to the new color


    }

    private void expandIsland(int row, int column) {
        if (row >= 0 && row < worldIntMap.length && column >= 0 && column < worldIntMap[row].length && worldIntMap[row][column] != seedColor) {
            worldIntMap[row][column] = 6; // Assuming 6 represents part of the island, you can adjust this value
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