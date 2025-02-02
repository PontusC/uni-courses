package towerdefence;

import towerdefence.towers.Tower;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Class used to contain all information about the games state. Contains the players lives, money and hp.
 * Also counts how long the game has been going for.
 */
public class GameState {
    private int lives, points, money;
    private long startTime, elapsedTime;
    private int xPos = 805;
    private int yPos = 20;
    private Tower selectedTower = null;

    public GameState(){
        lives = 25;
        points = 0;
        money = 125;
    }

    public void startGame(){
        startTime = System.nanoTime();
    }

    private String getTimeString(){
        elapsedTime = System.nanoTime() - startTime;
        // Returns time in seconds
        return Long.toString(TimeUnit.NANOSECONDS.toSeconds(elapsedTime));
    }

    /**
     * Draws points, money and time since start.
     * Also draws what tower you have currently selected to place.
     */
    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        g2.drawString("Time: " + getTimeString(), xPos, 20);
        g2.drawString("Lives: " + Integer.toString(lives),xPos,yPos + 25);
        g2.drawString("Money: " + Integer.toString(money),xPos,yPos + 25*2);
        if (selectedTower != null){
            g2.drawString("Tower: " + selectedTower.getName(), xPos, yPos + 25*3);
            g2.drawString("Cost: " + selectedTower.getCost(), xPos, yPos + 25*4);
        }
    }

    public boolean gameOver(){
        return lives <= 0;
    }

    public void removeLife(){
        lives--;
    }

    public int getLives(){
        return lives;
    }

    public void setSelectedTower(Tower tower){
        selectedTower = tower;
    }

    public void addMoney(int moneyToAdd){
        money += moneyToAdd;
    }

    // Attempts to buy something for cost, returns true if it succeeded.
    public boolean attemptToPurchase(int cost){
        if (money >= cost){
            money -= cost;
            return true;
        }
        return false;
    }
}
