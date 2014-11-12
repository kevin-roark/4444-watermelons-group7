package watermelon.group7;

import java.util.*;

import watermelon.group7.common.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Player extends watermelon.sim.Player {
	public void init() {
    }
    public void initStrategy(ArrayList<Pair> trees, double width, double height) {
        ILabelingStrategy labeling = new ProgressiveLabelingStrategy(Arrays.asList(new ModLabelingStrategy(2),
                                                                                   new SelfishLabelingStrategy(41)));

        IJigglingStrategy jiggling = new RotationalJigglingStrategy();

        IPackingStrategy finalPackingStrategy = new ChooseBestPackingStrategy(
                    new BestHexPackingStrategy(width, height), 
                    TreePackingStrategy.getCheckAllTrees(trees),
                    new PivotPackingStrategy());

        Strategy finalStrategy = new Strategy(finalPackingStrategy, null, labeling, jiggling, "YES");
        Strategy checkerboardStrategy = new CheckerboardStrategy();

        strategy = new ChooseBestStrategyStrategy(finalStrategy, checkerboardStrategy);
	}

    // the class actually responsible for making moves.
    Strategy strategy = null;
	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width, double length, double s) {
        if (strategy == null) {
            initStrategy(treelist, width, length);
        }

        return strategy.move(treelist, width, length, s);
	}
}
