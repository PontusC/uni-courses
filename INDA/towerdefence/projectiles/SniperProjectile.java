package towerdefence.projectiles;

import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * An implementation of a fast-moving high damage projectile.
 */
public class SniperProjectile extends Projectile {

    int width = 6;
    int height = 6;

    public SniperProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint){
        super(enemyUnit,spawnpoint, 25, 100);
    }

    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        Shape projectile = new Ellipse2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(projectile);
    }
}
