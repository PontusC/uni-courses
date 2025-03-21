package towerdefence.units;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * An implementation of a boss unit, extends the EnemyUnit class.
 */
public class BossUnit extends EnemyUnit{

    int width = 55;
    int height = 55;

    public BossUnit(towerdefence.Point spawnPoint, towerdefence.Point[] wayPoints){
        super(spawnPoint, wayPoints, 1, 5000, 500);
    }

    public BossUnit(towerdefence.Point spawnPoint, towerdefence.Point[] wayPoints, int speed, int hp, int value){
        super(spawnPoint, wayPoints, speed, hp, value);
    }

    public void drawMe(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(100, 100, 50));
        Shape enemy = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(enemy);
        g2.setColor(Color.BLACK);
        int xPos = (int) this.pos.getX();
        int yPos = (int) this.pos.getY();
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        g2.drawString(Integer.toString(this.hp), xPos - width/2, yPos - 30);
    }
}
