package towerdefence;

import towerdefence.projectiles.Projectile;
import towerdefence.towers.*;
import towerdefence.units.EnemyUnit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that controls the game, such as unit movement, hp, etc.
 * Contains all references to active towers, units and methods to reach these.
 *
 * Also handles all keyboard inputs as it extends KeyListener.
 */
public class GameController implements KeyListener, MouseListener{
    private GameBoard gameBoard;
    private GameState gameState;
    private ArrayList<Tower> allTowers;
    private ArrayList<EnemyUnit> allUnits;
    private ArrayList<Projectile> allProjectiles;
    private Tower selectedTower;

    /**
     * Constructor that initiates initial gamestate.
     */
    public GameController(GameBoard gameBoard, GameState gameState){
        this.gameBoard = gameBoard;
        this.gameState = gameState;
        allTowers = new ArrayList<>(10);
        allUnits = new ArrayList<>(50);
        allProjectiles = new ArrayList<>(50);
        selectedTower = null;
    }

    /**
     * Method that checks if you press a key and takes action.
     * Lets you select what tower you want to place.
     */
    public void keyPressed(KeyEvent e){
        if (e.getKeyChar() == KeyEvent.VK_1){
            selectedTower = new BasicTower(this);
            gameState.setSelectedTower(selectedTower);
        }else if (e.getKeyChar() == KeyEvent.VK_2){
            selectedTower = new FastTower(this);
            gameState.setSelectedTower(selectedTower);
        }else if (e.getKeyChar() == KeyEvent.VK_3){
            selectedTower = new SniperTower(this);
            gameState.setSelectedTower(selectedTower);
        }else if (e.getKeyChar() == KeyEvent.VK_4){
            selectedTower = new SplashTower(this);
            gameState.setSelectedTower(selectedTower);
        }else if (e.getKeyChar() == KeyEvent.VK_5) {
            selectedTower = new RandomTower(this);
            gameState.setSelectedTower(selectedTower);
        }else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE){
            gameState.addMoney(100);
        }else{
            selectedTower = null;
            gameState.setSelectedTower(selectedTower);
        }
    }

    /**
     * Method that attempts to place a tower when you click in the game
     * Checks that the clicked position is allowed, then if any other towers are taking up that spot, then if you can afford.
     */
    public void mousePressed(MouseEvent e) {
        // System.out.println("X: " + e.getX() + ". Y: " + e.getY());
        if  (selectedTower != null  && gameBoard.allowedPlacement(e.getX(), e.getY(), selectedTower.getWidth(), selectedTower.getHeight())
                && !towersInRange(e) && gameState.attemptToPurchase(selectedTower.getCost())) {
            // - 5 and - 30 in x & y because the "game board" starts at y = 30 due to the border.
            // - 5 to offset x difference as well
            selectedTower.moveTower(new Point(e.getX() - 5, e.getY() - 30));
            addTower(selectedTower);
            selectedTower = null;
            gameState.setSelectedTower(selectedTower);
        }
    }

    /**
     * Method that checks if any other towers are "in range" of where you clicked.
     * Bases this on the distance between the points, and the 2 towers drawn models.
     */
    private boolean towersInRange(MouseEvent e){
        for (Tower placedTower: allTowers) {
            // Calculates distance between placed tower and where you clicked
            double distance = placedTower.getPos().getDistance(new Point(e.getX() - 5, e.getY() - 30));
            int pWidth = placedTower.getWidth();
            int pHeight = placedTower.getHeight();
            int sWidth = selectedTower.getWidth();
            int sHeight = selectedTower.getHeight();
            // Testing prints
            // System.out.print("Pos distance: " + distance + ". hypot: ");
            // System.out.println(Math.hypot((pWidth + sWidth)/2, (pHeight + sHeight)/2));

            // Checks so that the distance between placed towers centerpoint and tower to place centerpoint
            // is greater than the distance covered by the towers drawn models
            if (distance <= Math.hypot((pWidth + sWidth)/2, (pHeight + sHeight)/2) - 5){
                return true;
            }
        }
        return false;
    }

    /**
     * Method used each "tick" of the game, makes the units and towers tick
     */
    public void tick(){
        spawnUnits();
        tickTowers();
        tickProjectiles();
        tickUnits();
    }

    private void tickTowers(){
        for (Tower tower: allTowers){
            tower.tick();
        }
    }

    /**
     * Moves all units and removes them if tick returns true, e.g. if the unit is out of bounds.
     */
    private void tickUnits(){
        for(Iterator<EnemyUnit> it = allUnits.iterator(); it.hasNext();){
            EnemyUnit unit = it.next();
            if (unit.isDead()){
                it.remove();
                gameState.addMoney(unit.getValue());
            }
            // If unit.tick is true, the unit is outside the map
            if (unit.tick()){
                it.remove();
                gameState.removeLife();
            }
        }
    }

    /**
     * Moves all projectiles, checks if they have hit anything and removes them if so.
     * Also remove them if the tick() method returns true, which it might if target is dead.
     */
    private void tickProjectiles(){
        for (Iterator<Projectile> it = allProjectiles.iterator(); it.hasNext();){
            Projectile projectile = it.next();
            // if projectile.tick is true the target of the projectile is dead
            if (projectile.tick()){
                it.remove();
            }else{
                EnemyUnit unit = projectile.hasCollidedWith();
                if (unit != null) {
                    boolean dead = unit.takeDamage(projectile.getDamage());
                    it.remove();
                    if (dead) {
                        removeUnit(unit);
                        gameState.addMoney(unit.getValue());
                    }
                }
            }
        }
    }

    /**
     * Asks the board to spawn units, adds a unit to the game if it returned one.
     */
    private void spawnUnits(){
        EnemyUnit toAdd = gameBoard.spawnUnit();
        if (toAdd != null)
            addUnit(toAdd);
    }

    public void addTower(Tower tower){
        allTowers.add(tower);
    }

    public boolean removeTower(Tower tower){
        return allTowers.remove(tower);
    }

    public void addUnit(EnemyUnit unit){
        allUnits.add(unit);
    }

    public boolean removeUnit(EnemyUnit unit){
        return allUnits.remove(unit);
    }

    public void addProjectile(Projectile projectile){
        allProjectiles.add(projectile);
    }

    public boolean removeProjectile(Projectile projectile){
        return allProjectiles.remove(projectile);
    }

    public ArrayList<Tower> getAllTowers(){
        return allTowers;
    }

    public ArrayList<EnemyUnit> getAllUnits(){
        return allUnits;
    }

    public ArrayList<Projectile> getAllProjectiles(){
        return allProjectiles;
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}
