package watermelon.group7;

import java.util.*;

import watermelon.group7.common.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Player extends watermelon.sim.Player {
	public static final double distowall = 1.1;
	public static final double distotree = 2.2;
	public static final double distoseed = 2.01;

	public void init() {
    }
    public void initStrategy(double width, double height) {
        IPackingStrategy hexPacking = new HexPackingStrategy();
        IPackingStrategy pivotPacking = new PivotPackingStrategy();

        IFillingStrategy filling = new EmptySpaceFillingStrategy();

        // I"m using a ProgressiveLabelingStrategy, which means do the ModLabelingStrategy first and
        // then do the SelfishLabelingStrategy.
        ILabelingStrategy labeling = new ProgressiveLabelingStrategy(Arrays.asList(new ModLabelingStrategy(2),
                                                                                   new SelfishLabelingStrategy(41)));

        IJigglingStrategy jiggling = new RotationalJigglingStrategy();

        Strategy hexStrategy = new Strategy(new BestHexPackingStrategy(width, height), filling, labeling, jiggling, "HEX");
        Strategy checkerboardStrategy = new CheckerboardStrategy();
        Strategy pivotStrategy = new Strategy(pivotPacking, filling, labeling, jiggling, "PIVOT");

        strategy = new ChooseBestStrategyStrategy(hexStrategy, checkerboardStrategy, pivotStrategy);
	}

    public double getHexXOffset() {
        return distoseed * Math.sin(Math.PI / 6.0);
    }

    public double getHexYOffset() {
        return distoseed * Math.cos(Math.PI / 6.0);
    }

    public static final double EPSILON = 0.001;

    // the class actually responsible for making moves.
    Strategy strategy = null;
	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width, double length, double s) {
        if (strategy == null) {
            initStrategy(width, length);
        }

        return strategy.move(treelist, width, length, s);
	}

    public void performFlips(ArrayList<seed> seedlist, ArrayList<GraphNode> nodes, double s) {
        // flipping phase
        Random random = new Random();
        double MAX_THRESHOLD = 7;
        double threshold = MAX_THRESHOLD;
        int NUM_THRESHOLDS = 12;
        for (int k = 0; k <= NUM_THRESHOLDS; k++) {
            nodes = GraphNode.getGraph(seedlist, threshold);
            threshold = MAX_THRESHOLD * (NUM_THRESHOLDS - k) / NUM_THRESHOLDS;
            System.out.format("\nDecreased threshold to: \033[1;32m%f\033[0m\n", threshold);
            int time_spent = (k + 1) * 5;

            for (int i = 0; i < time_spent; i++) {
                GraphNode node = nodes.get(random.nextInt(nodes.size()));
                double score_before = WatermelonMathUtil.calculateScore(seedlist, s);
                flipHex(node);
                double score_after = WatermelonMathUtil.calculateScore(seedlist, s);

                String scoreString = "score by " + (score_after - score_before) + " with threshold " + threshold;
                if (score_after < score_before) {
                    flipHex(node);
                    System.out.format("\033[1;31mWorsened\033[0m %s\n", scoreString);
                } else {
                    System.out.format("\033[1;34mImproved\033[0m %s\n", scoreString);
                }
            }
        }
    }


    public void flipHex(GraphNode n) {
        n.center.tetraploid = !n.center.tetraploid;
        for (seed s : n.neighbors) {
            s.tetraploid = !s.tetraploid;
        }
    }

    public boolean validateSeeds(ArrayList<seed> seeds) {
        for (int i = 0; i < seeds.size(); i++) {
            for (int j = 0; j < seeds.size(); j++) {
                if (j == i) continue;
                double dist = WatermelonMathUtil.distance(seeds.get(i), seeds.get(j));
                if (dist < distoseed - EPSILON) {
                    System.out.printf("Invalid setup! Seeds %d and %d are %f apart.\n", i, j, dist);
                    return false;
                }
            }
        }
        return true;
    }
}
