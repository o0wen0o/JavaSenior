import java.util.HashSet;

/**
 * if pointA and pointB are on the same row, x.length = 1, y.length = 2
 * if pointA and pointB are on the same column, x.length = 2, y.length = 1
 *
 * @author o0wen0o
 * @create 2024-02-01 5:47 PM
 */
public class StrongRelationshipPoints {
    private Point pointA;

    private Point pointB;

    private HashSet<Integer> x; // from 1 to 9

    private HashSet<Integer> y; // from 1 to 9

    private String numRelated;

    public StrongRelationshipPoints() {
    }

    public StrongRelationshipPoints(Point pointA, Point pointB, String numRelated) {
        this.pointA = pointA;
        this.pointB = pointB;
        initXY();
        this.numRelated = numRelated;
    }

    public Point getPointA() {
        return pointA;
    }

    public void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public void setPointB(Point pointB) {
        this.pointB = pointB;
    }

    public HashSet<Integer> getX() {
        return x;
    }

    public void setX(HashSet<Integer> x) {
        this.x = x;
    }

    public HashSet<Integer> getY() {
        return y;
    }

    public void setY(HashSet<Integer> y) {
        this.y = y;
    }

    public String getNumRelated() {
        return numRelated;
    }

    public void setNumRelated(String numRelated) {
        this.numRelated = numRelated;
    }

    private void initXY() {
        x = new HashSet<>();
        y = new HashSet<>();

        x.add(pointA.getX());
        x.add(pointB.getX());
        y.add(pointA.getY());
        y.add(pointB.getY());
    }

    @Override
    public String toString() {
        return "StrongRelationshipPoints{" +
                "pointA=" + pointA +
                ", pointB=" + pointB +
                ", x=" + x +
                ", y=" + y +
                ", numRelated='" + numRelated + '\'' +
                '}';
    }
}
