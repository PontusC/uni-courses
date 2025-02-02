package towerdefence.towers;

import towerdefence.*;
import towerdefence.projectiles.FastProjectile;
import towerdefence.projectiles.Projectile;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * An implementation of a Tower that shoots very fast, extends the abstract class Tower.
 * Implements its own drawMe method and projectile
 */
public class FastTower extends Tower {

    public FastTower(towerdefence.Point spawnPoint, GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(spawnPoint, Controller, 4, 200, 25, 30, 30);
    }

    public FastTower(GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(Controller, 4, 200, 25, 30, 30, "Fast");
    }


    protected Projectile createProjectile(EnemyUnit target){
        return new FastProjectile(target, new towerdefence.Point(this.pos.getX(), this.pos.getY()));
    }

    @Override
    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GREEN);
        Shape tower = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(tower);
    }
}
