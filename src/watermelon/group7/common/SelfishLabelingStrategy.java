package watermelon.group7.common;

import watermelon.group7.*;
import watermelon.sim.*;
import java.util.*;

public class SelfishLabelingStrategy implements ILabelingStrategy {
    int passes;

    public SelfishLabelingStrategy() {
        this(3);
    }

    public SelfishLabelingStrategy(int passes) {
        this.passes = passes;
    }

    public void labelSeeds(ArrayList<seed> seeds, double s) {
        for (int i = 0; i < passes; i++) {
            if (!doValuePass(seeds, s)) {
                break;
            }
        }
    }

    boolean doNeighborsPass(ArrayList<seed> seeds, double s) {
        boolean flipped_seed = false;
        ArrayList<GraphNode> graph = GraphNode.getGraph(seeds);

        for (GraphNode g : graph) {
            if (g.oppositeRatio() < 0.5) {
                g.getCenter().tetraploid = !g.getCenter().tetraploid;
                flipped_seed = true;
            }
        }
        return flipped_seed;
    }

    boolean doValuePass(ArrayList<seed> seeds, double s) {
        int i = 0;
        boolean flipped_seed = false;

        HashMap<seed, Double> current_scores = new HashMap<seed, Double>();

        for (seed seed : seeds) {
            if (!current_scores.containsKey(seed)) {
                current_scores.put(seed, Analysis.calculateSeedScore(seed, seeds, s));
            }

            double current_score = current_scores.get(seed);
            seed.tetraploid = !seed.tetraploid;
            double flipped_score = Analysis.calculateSeedScore(seed, seeds, s);

            if (flipped_score > current_score) {
                current_scores.put(seed, flipped_score);
                flipped_seed = true;
            } else {
                // flip back.
                seed.tetraploid = !seed.tetraploid;
            }
        }

        return flipped_seed;
    }
}
