package towerdefence.projectiles;

import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * A basic implementation of a Projectile, extends the abstract class projectile.
 * Implements its own drawMe and its own speed/damage.
 */
public class BasicProjectile extends Projectile {

    int width = 7;
    int height = 7;

    public BasicProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint){
        super(enemyUnit,spawnpoint, 9, 25);
    }

    public BasicProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint, int damage){
        super(enemyUnit,spawnpoint, 9, damage);
    }

    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.CYAN);
        Shape projectile = new Ellipse2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(projectile);
    }

}
