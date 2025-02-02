package towerdefence.projectiles;

import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * An implementation of a fast-moving low damage projectile.
 */

public class FastProjectile extends Projectile {
    private int width = 4;
    private int height = 4;

    public FastProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint){
        super(enemyUnit,spawnpoint, 12, 1);
    }

    public FastProjectile(EnemyUnit enemyUnit, towerdefence.Point spawnpoint, int damage){
        super(enemyUnit,spawnpoint, 12, damage);
    }

    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.ORANGE);
        Shape projectile = new Ellipse2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(projectile);
    }

}