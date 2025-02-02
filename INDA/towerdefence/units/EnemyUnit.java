package towerdefence.units;

import towerdefence.*;

import java.awt.*;
import java.awt.Point;

/**
 * Abstract class that contains the general implementations of the units, and some abstract methods to create  different
 * units.
 *
 * Methods to be implemented are how the unit should be drawn.
 */
public abstract class EnemyUnit {
    protected int hp, turns, value;
    protected double xSpeed, ySpeed, unitSpeed;
    protected towerdefence.Point[] wayPoints;
    protected towerdefence.Point pos, wayPoint;

    public EnemyUnit(towerdefence.Point spawnPoint, towerdefence.Point[] wayPoints, int unitSpeed, int hp, int value) {
        pos = new towerdefence.Point(spawnPoint);
        // How fast the unit will move
        this.unitSpeed = unitSpeed;
        this.hp = hp;
        this.value = value;
        turns = 0;
        this.wayPoints = wayPoints;
        wayPoint = wayPoints[turns];
        // Sets initial movement direction, xSpeed and ySpeed.
        setDirection();
    }

    // Ticks the unit, makes it move.
    public boolean tick() {
        move();
        return outsideMap();
    }

    // Sets initial movement direction based on the first waypoint of the map.
    private void setDirection() {
        if (Math.abs(pos.getX() - wayPoint.getX()) < 0.1) {
            if (pos.getY() - wayPoint.getY() > 0){
                ySpeed = -unitSpeed;
            }else{
                ySpeed = unitSpeed;
            }
            xSpeed = 0;
        } else {
            if (pos.getX() - wayPoint.getX() > 0){
                xSpeed = -unitSpeed;
            }else{
                xSpeed = unitSpeed;
            }
            ySpeed = 0;
        }
    }

    // Returns true if the unit is outside the map (1000x1000), false if not outside
    protected boolean outsideMap(){
        return (pos.getX() > 1000 || pos.getX() < 0 || pos.getY() > 1000 || pos.getY() < 0);
    }

    // Moves the unit, and changes its direction if it reaches a waypoint using helperfunctions.
    private void move() {
        //System.out.println("Current pos: " + pos.toString() + ". Waypoint: " + wayPoint.toString() + ". xs: " + xSpeed + " ys: " + ySpeed + ". Distance: " + pos.getDistance(wayPoint));
        if (pos.getDistance(wayPoint) < Math.abs(xSpeed)) {
            pos = new towerdefence.Point(wayPoint);
            // do new speed calculations here
            if (getNewWaypoint())
                setNewSpeed();
            return;
        } else {
            pos.addX(xSpeed);
        }
        if (pos.getDistance(wayPoint) < Math.abs(ySpeed)) {
            pos = new towerdefence.Point(wayPoint);
            // do new speed shit
            if (getNewWaypoint())
                setNewSpeed();
            return;
        } else {
            pos.addY(ySpeed);
        }
    }


    // Updates xSpeed and ySpeed so it moves in correct direction.
    private void setNewSpeed() {
        // If xSpeed == 0 means it is time to move in x-axis direction
        if (xSpeed == 0) {
            // If current xPos - next xPos is positive, xSpeed needs to be negative.
            if (pos.getX() - wayPoint.getX() > 0) {
                xSpeed -= Math.abs(ySpeed);
                ySpeed = 0;
            } else {
                xSpeed = Math.abs(ySpeed);
                ySpeed = 0;
            }
        } else {
            if (pos.getY() - wayPoint.getY() > 0) {
                ySpeed -= Math.abs(xSpeed);
                xSpeed = 0;
            } else {
                ySpeed = Math.abs(xSpeed);
                xSpeed = 0;
            }
        }
    }

    /**
     * Changes to the next waypoint. if there are no more waypoints stops the movement.
     * Returns true if there is a new waypoint.
     */
    private boolean getNewWaypoint() {
        // +2?
        if (turns + 2 > wayPoints.length) {
            xSpeed = 0;
            ySpeed = 0;
            return false;
        }
        turns += 1;
        wayPoint = new towerdefence.Point(wayPoints[turns]);
        return true;
    }

    public boolean isDead(){
        return hp <= 0;
    }

    public towerdefence.Point getPos(){
        return pos;
    }

    // Returns true if unit died, false if not
    public boolean takeDamage(int damage) {
        hp -= damage;
        return hp <= 0;
    }
    public int getValue(){
        return value;
    }

    public abstract void drawMe(Graphics g);
}
