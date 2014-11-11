package watermelon.group7.common;

import watermelon.sim.Pair;
import watermelon.sim.seed;

import watermelon.group7.Seed;

public class Vector2 {
    public double x;
    public double y;

    public Vector2() {
        this(0,0);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Pair p) {
        this(p.x, p.y);
    }
    public Vector2(seed s) {
        this(s.x, s.y);
    }

    public Vector2 add(Vector2 r) {
        return new Vector2(x+r.x, y+r.y);
    }

    public Vector2 add(double dx, double dy) {
        return new Vector2(x+dx, y+dy);
    }

    public Vector2 sub(Vector2 r) {
        return new Vector2(x-r.x, y-r.y);
    }
    public Vector2 sub(double dx, double dy) {
        return new Vector2(x-dx, y-dy);
    }

    public Vector2 scale(double s) {
        return new Vector2(this.x * s, this.y * s);
    }

    public Vector2 unit() {
        return this.scale(1.0 / this.length());
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public Vector2 rotate(double rads) {
        double c = Math.cos(rads);
        double s = Math.sin(rads);

        return new Vector2(c*x - s*y,
                           s*x + c*y);
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public double dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector2 rotateRelative(Vector2 origin, double theta) {
        Vector2 adjusted = this.sub(origin);
        return adjusted.rotate(theta);
    }

    public Vector2 rotateAbout(Vector2 origin, double theta) {
      Vector2 relative = rotateRelative(origin, theta);
      return relative.add(origin);
    }

    public static Vector2 fromOrientationAndLength(double theta, double r) {
        return new Vector2(r * Math.cos(theta),
                           r * Math.sin(theta));
    }

    public Seed updateSeed(seed s) {
      return new Seed(this.x, this.y, s.tetraploid);
    }

    public Seed updateSeed(Seed s) {
      return new Seed(this.x, this.y, s.tetraploid);
    }

    public seed getSeed() {
        return new seed(this.x, this.y, false);
    }

    @Override
    public String toString() {
        return String.format("<Vector2 (%f, %f)>", x, y);
    }

    public static double dot(Vector2 l, Vector2 r) {
        return l.dot(r);
    }
}
