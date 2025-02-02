package towerdefence.towers;

import towerdefence.*;
import towerdefence.projectiles.Projectile;
import towerdefence.projectiles.SplashProjectile;
import towerdefence.units.EnemyUnit;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * An implementation of a tower that randomizes everything it does.
 */
public class RandomTower extends Tower {
    private Random random = new Random();
    private Color randomColor;
    private int colorCounter = 0;

    public RandomTower(towerdefence.Point spawnPoint, GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(spawnPoint, Controller, 30*2, 350, 50, 50, 50);
    }

    public RandomTower(GameController Controller){
        // Creates a BasicTower from the abstract Tower-class
        super(Controller, 30*2, 400, 350, 50, 50, "Random");
    }

    // Overrides the Tower tick method to take into consideration the random aspects.
    public void tick() {
        counter++;
        if (canShoot()) {
            EnemyUnit target = findInRange();
            // Check if no target in range
            if (target == null)
                return;
            this.cooldown = 30*random.nextInt(10);
            this.range = 150 + random.nextInt(15)*50;
            controller.addProjectile(createProjectile(target));
            counter = 0;
        }
    }

    protected Projectile createProjectile(EnemyUnit target){
        return new SplashProjectile(target, new towerdefence.Point(pos), controller,random.nextInt(120), (double) random.nextInt(500));
    }

    @Override
    public void drawMe(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if (colorCounter <= 0){
            randomColor = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
            colorCounter = 30;
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(randomColor);
        Shape tower = new Rectangle2D.Double(this.pos.getX() - width/2, this.pos.getY() - height/2, width, height);
        g2.fill(tower);
        colorCounter --;
    }

}
