package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.lang.Math;
import java.util.*;

public class Analysis {
    public static double calculatePackingEfficiency(ArrayList<seed> seeds,
                                                    ArrayList<Pair> trees,
                                                    double length, double width) {
        return seeds.size() / (double)maxSeedsPossible(trees, length, width);
    }

    public static int maxSeedsPossible(ArrayList<Pair> trees, double length, double width) {
        double total_area = ((length - Constants.wall_spacing) *
                             (width  - Constants.wall_spacing));

        double area_available = total_area - treeArea() * trees.size();

        return (int)Math.floor(area_available / seedArea());
    }

    static double seedArea() {
        return 0.25 * Constants.seed_diameter * Constants.seed_diameter * Math.PI;
    }
    static double treeArea() {
        return 0.25 * Constants.tree_diameter * Constants.tree_diameter * Math.PI;
    }

    static double calculateSeedScore(seed seed, ArrayList<seed> seedlist, double s) {
        double chance   = 0.0;
        double totaldis = 0.0;
        double difdis   = 0.0;

        for (seed other : seedlist) {
            if (other != seed) {
                if (seed.tetraploid != other.tetraploid) {
                    difdis += Math.pow(WatermelonMathUtil.distance(seed, other), -2);
                }

                totaldis += Math.pow(WatermelonMathUtil.distance(seed, other), -2);
            }
        }

        chance = difdis / totaldis;
        return chance + (1 - chance) * s;
    }

	static double calculateBoardScore(ArrayList<seed> seedlist, double s) {
        double score = 0.0;
        for (seed seed : seedlist) {
            score += calculateSeedScore(seed, seedlist, s);
        }
        return score;
	}
}
