package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class TreePackingStrategy extends PivotPackingStrategy {
    public static final double EPSILON = 0.00;

    double delta;

    int i;
    public TreePackingStrategy(int i) {
       this.delta = Math.PI / 3;
       this.i = i;
    }

    public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
        Set<seed> free_seeds = new HashSet<seed>();
        ArrayList<seed> seeds = new ArrayList<seed>();

        free_seeds.addAll(placeRing(new Vector2(trees.get(i)), 0.0, this.delta, seeds, trees, width, height));

        return generatePacking(free_seeds, 0.0, this.delta, trees, width, height);
    }

    public static ChooseBestPackingStrategy getCheckAllTrees(ArrayList<Pair> trees) {
        ArrayList<IPackingStrategy> strategies = new ArrayList<IPackingStrategy>();
        for (int i = 0; i < trees.size(); i++) {
            strategies.add(new TreePackingStrategy(i));
        }

        return new ChooseBestPackingStrategy(strategies);
    }
}
