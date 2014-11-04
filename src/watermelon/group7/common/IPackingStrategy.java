package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public interface IPackingStrategy {
    ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height);
}
