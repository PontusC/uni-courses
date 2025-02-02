package towerdefence.towers;

import towerdefence.*;
import towerdefence.projectiles.BasicProjectile;
import towerdefence.projectiles.Projectile;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A basic implementation of a Tower, extends the abstract class Tower.
 * Implements its own drawMe method and projectile
 */
public class BasicTower extends Tower {

    public BasicTower(towerdefence.Point spawnPoint, GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(spawnPoint, Controller, 30*2, 400, 50, 50, 50);
    }

    public BasicTower(GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(Controller, 30*2, 400, 50, 50, 50, "Basic");
    }

    protected Projectile createProjectile(EnemyUnit target){
        return new BasicProjectile(target, new towerdefence.Point(this.pos.getX(), this.pos.getY()));
    }

    @Override
    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        Shape tower = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(tower);
    }
}