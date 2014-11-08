package watermelon.group7;

import watermelon.sim.seed;
import watermelon.group7.common.Vector2;

public class Seed extends seed {
    public Seed(seed s) {
        this.x = s.x;
        this.y = s.y;
        this.tetraploid = s.tetraploid;
    }

    public Seed(double x, double y, boolean t) {
      this.x = x;
      this.y = y;
      this.tetraploid = t;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof seed))
            return false;
        if (obj == this)
            return true;

        seed rhs = (seed) obj;
        return Math.round(this.x) == Math.round(rhs.x) & Math.round(this.y) == Math.round(rhs.y);
    }

    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }

    @Override
    public int hashCode() {
        return Double.valueOf(Math.round(this.x) * Math.round(this.y)).hashCode();
    }

    public Vector2 position() {
        return new Vector2(this);
    }
}
