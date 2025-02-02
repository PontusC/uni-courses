package towerdefence;

/**
 * Class used to represent a point in 2D space.
 * Also contains a method to check distance between 2 points.
 */
public class Point{
    double xPos, yPos;

    public Point(double xPos,double yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    // Constructor to be used when setting position to that of another Point
    public Point(Point pointToCopy){
        xPos = pointToCopy.getX();
        yPos = pointToCopy.getY();
    }

    public double getX(){
        return xPos;
    }

    public double getY(){
        return yPos;
    }

    public void setX(double xPos) {
        this.xPos = xPos;
    }

    public void setY(double yPos){
        this.yPos = yPos;
    }

    public void setLocation(Point pos) { this.xPos = pos.getX(); this.yPos = pos.getY(); }

    public void addX(double xDistance){
        xPos += xDistance;
    }

    public void addY(double yDistance){
        yPos += yDistance;
    }

    // Checks distance between this point and the given point
    public double getDistance(Point otherPoint){
        return Math.hypot(xPos - otherPoint.getX(), yPos - otherPoint.getY());
    }

    @Override
    public String toString(){
        return new String("x: " + xPos + ". y: " + yPos);
    }
}
