package towerdefence.projectiles;

import towerdefence.*;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * An implementation of a projectile with splash damage.
 */
public class SplashProjectile extends Projectile {
    private int width = 10;
    private int height = 10;
    private double range = 120;
    private towerdefence.Point targetPos;
    private GameController controller;
    private boolean hasExploded = false;

    public SplashProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint, GameController controller){
        super(enemyUnit,spawnpoint, 9, 45);
        targetPos = new towerdefence.Point(enemyUnit.getPos());
        this.controller = controller;
    }

    public SplashProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint, GameController controller, int damage, double range){
        super(enemyUnit,spawnpoint, 9, 45);
        targetPos = new towerdefence.Point(enemyUnit.getPos());
        this.controller = controller;
        this.damage = damage;
        this.range = range;
    }

    // Moves the projectile, and returns true/false if the projectile has exploded.
    public boolean tick(){
        move();
        return hasExploded;
    }

    // Moves the projectile towards its target based on the angle between them
    // Makes the projectile explode if it reaches destination.
    private void move(){
        double dX = targetPos.getX() - pos.getX();
        double dY = targetPos.getY() - pos.getY();
        double angle = Math.atan2(dY, dX);

        if (speed < targetPos.getDistance(pos) ) {
            pos.addX(Math.cos(angle) * speed);
            pos.addY(Math.sin(angle) * speed);
        } else {
            pos.setLocation(targetPos);
            explode();
        }
    }

    private void explode(){
        for (EnemyUnit unit: controller.getAllUnits()) {
            if (unit.getPos().getDistance(pos) < range){
                unit.takeDamage(damage);
            }
        }
        hasExploded = true;
    }

    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 50, 50));
        Shape projectile = new Ellipse2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(projectile);
    }
}
