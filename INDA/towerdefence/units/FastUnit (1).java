package towerdefence.units;

import towerdefence.*;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * An implementation of a fast unit, extends the EnemyUnit class.
 */
public class FastUnit extends EnemyUnit {

    int width = 18;
    int height = 18;

    public FastUnit(towerdefence.Point spawnPoint, towerdefence.Point[] wayPoints) {
        super(spawnPoint, wayPoints, 3, 50, 10);
    }

    public FastUnit(towerdefence.Point spawnPoint, towerdefence.Point[] wayPoints, int speed, int hp, int value) {
        super(spawnPoint, wayPoints, speed, hp, value);
    }

    public void drawMe(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLUE);
        Shape enemy = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(enemy);
        g2.setColor(Color.BLACK);
        int xPos = (int) this.pos.getX();
        int yPos = (int) this.pos.getY();
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        g2.drawString(Integer.toString(this.hp), xPos - width/2, yPos - 10);
    }
}
