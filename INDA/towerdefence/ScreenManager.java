package towerdefence;

import towerdefence.projectiles.Projectile;
import towerdefence.towers.Tower;
import towerdefence.units.EnemyUnit;

import javax.swing.*;
import java.awt.*;

/**
 * Class that managers the screen/frame. Handles all the drawing, background etc.
 */
public class ScreenManager extends JPanel {
    private GameBoard GameBoard;
    private GameController Controller;
    private GameState GameState;

    public ScreenManager(GameController controller, GameBoard GameBoard, GameState GameState){
        this.GameBoard = GameBoard;
        this.Controller = controller;
        this.GameState = GameState;
    }

    // Returns preferred screensize of the frame
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(1000,1000);
    }

    // Paints all units, etc
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(GameBoard.getBackground(),0,0,null);
        for (Tower tower : Controller.getAllTowers()){
            tower.drawMe(g);
        }
        for (EnemyUnit enemyUnit: Controller.getAllUnits()){
            enemyUnit.drawMe(g);
        }
        for (Projectile projectile: Controller.getAllProjectiles()){
            projectile.drawMe(g);
        }
        GameState.drawMe(g);
    }

}
