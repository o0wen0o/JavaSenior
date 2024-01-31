/**
 * @author o0wen0o
 * @create 2022-12-28 11:51 AM
 */
public class Point {
    private int x; // from 1 to 9
    private int y; // from 1 to 9
    private int z; // from 1 to 9
    private String num; // may concat multiple numbers

    public String color = "\u001B[0m"; // "\u001B[34m" blue

    public Point() {
    }

    public Point(int x, int y, int z, String num) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.num = num;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "{ " + x + " " + y + " " + z + " " + num + " }";
    }
}
