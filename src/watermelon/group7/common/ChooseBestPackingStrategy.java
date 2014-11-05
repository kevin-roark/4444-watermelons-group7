
package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class ChooseBestPackingStrategy implements IPackingStrategy {
    ArrayList<IPackingStrategy> strategies;

    public ChooseBestPackingStrategy(ArrayList<IPackingStrategy> strategies) {
        if (strategies.size() == 0) {
            throw new RuntimeException("ChooseBestPackingStrategy was given an empty menu of strategies in its constructor!");
        }
        this.strategies = strategies;
    }

    public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
        int best = 0;
        ArrayList<seed> best_packing = null;

        for (IPackingStrategy s : strategies) {
            ArrayList<seed> result = s.generatePacking(trees, width, height);
            if (result.size() > best) {
                best = result.size();
                best_packing = result;
            }
        }

        return best_packing;
    }
}