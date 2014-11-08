
package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public interface IJigglingStrategy {
    ArrayList<seed> jiggleSeeds(ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height);
}
