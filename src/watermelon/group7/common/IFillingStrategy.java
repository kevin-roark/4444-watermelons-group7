
package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public interface IFillingStrategy {
    ArrayList<seed> fillSeeds(ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height);
}
