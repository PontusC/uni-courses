package towerdefence.projectiles;

import towerdefence.*;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.Point;

/**
 * Abstract class that contains all the general implementations for the projectiles, and some abstract methods to create
 * different projectiles.
 *
 * Methods to be implemented are how to be drawn.
 */
public abstract class Projectile {
    protected towerdefence.Point pos;
    protected double speed;
    protected EnemyUnit target;
    protected int damage;

    public Projectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint, int speed, int damage){
        target = enemyUnit;
        pos = new towerdefence.Point(spawnpoint);
        this.speed = speed;
        this.damage = damage;
    }

    // Moves the projectile, and returns true/false if its target is dead.
    public boolean tick(){
        move();
        return targetIsDead();
    }

    public int getDamage(){
        return damage;
    }

    private boolean targetIsDead(){
        return target.isDead();
    }

    // Moves the projectile towards its target based on the angle between them
    private void move(){
        towerdefence.Point targetPos = target.getPos();
        double dX = targetPos.getX() - pos.getX();
        double dY = targetPos.getY() - pos.getY();
        double angle = Math.atan2(dY, dX);

        if (speed < targetPos.getDistance(pos) ) {
            pos.addX(Math.cos(angle) * speed);
            pos.addY(Math.sin(angle) * speed);
        } else {
            pos.setLocation(targetPos);
        }
    }

    // Checks if it has hit the unit it has targeted
    public EnemyUnit hasCollidedWith() {
        if (pos.getDistance(target.getPos()) <= 0.5) {
            return target;
        } else {
            return null;
        }
    }

    public abstract void drawMe(Graphics g);
}
