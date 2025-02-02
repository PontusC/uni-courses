package towerdefence.towers;

import towerdefence.*;
import towerdefence.projectiles.Projectile;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.util.ArrayList;

/**
 * Abstract class that contains all the general implementations of the towers, and some abstract method used to
 * create different towers.
 *
 * Methods to be implemented are how the tower should be drawn, and what kind of projectile it should fire.
 */
public abstract class Tower{
    protected int cooldown, counter, cost, width, height;
    protected towerdefence.Point pos;
    protected double range;
    protected GameController controller;
    protected String name;

    public Tower(towerdefence.Point spawnPoint, GameController Controller, int cooldown, double range, int cost, int width, int height){
        pos = spawnPoint;
        this.range = range;
        this.cooldown = cooldown;
        counter = cooldown;
        this.controller = Controller;
        this.cost = cost;
        this.width = width;
        this.height = height;
        name = null;
    }

    public Tower(GameController Controller, int cooldown, double range, int cost, int width, int height, String name){
        pos = null;
        this.range = range;
        this.cooldown = cooldown;
        counter = cooldown;
        this.controller = Controller;
        this.cost = cost;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    /**
     * Method that ticks the tower, checks if it can shoot, if there is any target in range and then fires.
     */
    public void tick() {
        counter++;
        if (canShoot()) {
            EnemyUnit target = findInRange();
            // Check if no target in range
            if (target == null)
                return;
            controller.addProjectile(createProjectile(target));
            counter = 0;
        }
    }

    /**
     * Method that checks through all units alive if any is in range.
     */
    protected EnemyUnit findInRange() {
        ArrayList<EnemyUnit> allUnits = controller.getAllUnits();
        for (EnemyUnit unit: allUnits) {
            if (pos.getDistance(unit.getPos()) <= range){
                return unit;
            }
        }
        return null;
    }

    protected boolean canShoot() {
        return counter >= cooldown;
    }

    public String getName(){
        return name;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public towerdefence.Point getPos(){
        return pos;
    }

    public void moveTower(towerdefence.Point newPoint){
        pos = newPoint;
    }

    public int getCost(){
        return cost;
    }

    protected abstract Projectile createProjectile(EnemyUnit target);

    public abstract void drawMe(Graphics g);
}
