package towerdefence.towers;

import towerdefence.*;
import towerdefence.projectiles.Projectile;
import towerdefence.projectiles.SplashProjectile;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * An implementation of a tower with splash damage, extends Tower.
 * Implements its own drawMe and has a unique projectile.
 */
public class SplashTower extends Tower {

    public SplashTower(towerdefence.Point spawnPoint, GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(spawnPoint, Controller, 30*2, 400, 50, 50, 50);
    }

    public SplashTower(GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(Controller, 30*3, 300, 200, 60, 60, "Splash");
    }

    protected Projectile createProjectile(EnemyUnit target){
        return new SplashProjectile(target, new towerdefence.Point(this.pos.getX(), this.pos.getY()), this.controller);
    }

    @Override
    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLUE);
        Shape tower = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(tower);
    }
}
