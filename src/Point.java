public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance2D(Point p2) {// new
        double dx = this.x - p2.x;
        double dy = this.y - p2.y;
        double t = (dx * dx + dy * dy);
        return Math.sqrt(t);
    }

}

