package towerdefence.towers;

import towerdefence.*;
import towerdefence.projectiles.Projectile;
import towerdefence.projectiles.SniperProjectile;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * An implementation of a longrange sniper tower, extends the Tower class.
 * Implements its own drawMe method and has its own type of projectile.
 */
public class SniperTower extends Tower {

    public SniperTower(towerdefence.Point spawnPoint, GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(spawnPoint, Controller, 30*2, 400, 50, 50, 50);
    }

    public SniperTower(GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(Controller, 30*5, 650, 120, 40, 40, "Sniper");
    }

    protected Projectile createProjectile(EnemyUnit target){
        return new SniperProjectile(target, new towerdefence.Point(this.pos.getX(), this.pos.getY()));
    }

    @Override
    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        Shape tower = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(tower);
    }
}
