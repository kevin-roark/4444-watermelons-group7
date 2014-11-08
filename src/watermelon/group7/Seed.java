package watermelon.group7;

import watermelon.sim.seed;
import watermelon.group7.common.Vector2;

public class Seed extends seed {
    public boolean equals(Object obj) {
        if (!(obj instanceof Seed))
            return false;
        if (obj == this)
            return true;

        Seed rhs = (Seed)obj;
        return this.x == rhs.x & this.y == rhs.y;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(Math.round(this.x) * Math.round(this.y)).hashCode();    
    }

    public Vector2 position() {
        return new Vector2(this);
    }
}
