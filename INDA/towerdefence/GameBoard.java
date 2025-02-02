package towerdefence;

import towerdefence.units.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Class that contains information about the board. Handles the map(background) to be drawn, the waypoints for units to move along,
 * the spawn information for the units (how many per wave, when, what type), and where towers can be placed.
 *
 * If we want the board to contain multiple maps, change the load functions to take a parameter such as 0-5, indicating different
 * map and therefore loading different settings.
 */
public class GameBoard {
    private int counter, internalCounter, amountToSpawn, waves;
    private Point unitSpawnPoint;
    private Image background;
    private Point[] waypoints;
    // Placeholder to be used later to define where towers can be placed
    private Rectangle2D.Double[] towerPoints;

    public GameBoard(){
        unitSpawnPoint = loadSpawnpoint();
        waypoints = loadWaypoints();
        background = loadBackground();
        towerPoints = loadTowerpoints();
        counter = 150;
        internalCounter = 0;
        amountToSpawn = 10;
        waves = 1;
    }

    // Returns the EnemyUnit to spawn based on time in game
    public EnemyUnit spawnUnit(){
        counter ++;
        if (counter >= 300){
            if (internalCounter < 1) {
                internalCounter  = 20;
                amountToSpawn --;
                EnemyUnit toSpawn = unitSpawnHandler();
                if (amountToSpawn == 0){
                    amountToSpawn = 10;
                    internalCounter = 0;
                    counter = 0;
                    waves ++;
                }
                return toSpawn;
            }else{
                internalCounter --;
            }

        }
        return null;
    }

    // Returns a specified type of EnemyUnit depending on what wave it currently is.
    private EnemyUnit unitSpawnHandler(){
        if ((waves % 10) == 0){
            amountToSpawn = 0;
            return new BossUnit(unitSpawnPoint, waypoints, 1, 60*waves, 15*waves);
        }
        if ((waves % 4) == 0){
            return new TankyUnit(unitSpawnPoint, waypoints, 2, 15*waves, 8);
        }
        if ((waves % 6) == 0){
            return new FastUnit(unitSpawnPoint, waypoints, 5, 3*waves, 6);
        }
        return new BasicUnit(unitSpawnPoint, waypoints, 3, 32 + 6*waves, 4);
    }

    // Method that checks that given range is in allowed area, parameters are used to draw a rectangle
    public boolean allowedPlacement(double x, double y, double width, double height){
        for (int i = 0; i < towerPoints.length; i++) {
            if (towerPoints[i].contains(x - width/2, y - height/2, width, height))
                return true;
        }
        return false;
    }

    // Loads the areas "rectangles" where you're allowed to place towers
    private Rectangle2D.Double[] loadTowerpoints(){
        Rectangle2D.Double[] areas = {
                new Rectangle2D.Double(0,145,655,125),
                new Rectangle2D.Double(500, 145, 150, 585),
                new Rectangle2D.Double(100, 375, 300, 550),
                new Rectangle2D.Double(510, 850, 490, 180),
                new Rectangle2D.Double(800, 205, 200, 525),
        };

        return areas;
    }

    // Loads the background image
    private Image loadBackground(){
        return new ImageIcon("src//towerdefence//background.png").getImage();
    }

    // Loads the waypoints for the board
    private Point[] loadWaypoints(){
        return new Point[]{createPoint(720, 50), createPoint(720, 750), createPoint(445, 750),
                createPoint(445, 960), createPoint(50,960), createPoint(50, 290), createPoint(445, 290)
        , createPoint(445, 750), createPoint(1050, 750)};
    }

    // Helper method
    private Point createPoint(double xPos, double yPos){
        return new Point(xPos, yPos);
    }

    // Loads spawnpoint
    private Point loadSpawnpoint(){
        return new Point(20,50);
    }

    public Point getSpawnPoint(){
        return unitSpawnPoint;
    }

    public Image getBackground(){
        return background;
    }

    public Point[] getWaypoints(){
        return waypoints;
    }

    public void getTowerpoints(){
        return;
    }
}
