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
        double chance = 0.0;

        double total_distances           = 0.0;
        double opposite_ploidy_distances = 0.0;

        for (seed other : seedlist) {
            if (other != seed) {
                if (seed.tetraploid != other.tetraploid) {
                    opposite_ploidy_distances += Math.pow(WatermelonMathUtil.distance(seed, other), -2);
                }

                total_distances += Math.pow(WatermelonMathUtil.distance(seed, other), -2);
            }
        }

        chance = opposite_ploidy_distances / total_distances;
        return chance + (1 - chance) * s;
    }

	static double calculateBoardScore(ArrayList<seed> seedlist, double s) {
        double score = 0.0;
        for (seed seed : seedlist) {
            score += calculateSeedScore(seed, seedlist, s);
        }
        return score;
	}

	static boolean validateBoard(ArrayList<seed> seeds, ArrayList<Pair> trees, double W, double L) {
        boolean result = true;

        for (seed s : seeds) {
            result &= validateSeed(s, seeds, trees, W, L);
        }

        return result;
    }

	static boolean validateSeed(seed s, ArrayList<seed> seeds, ArrayList<Pair> trees, double W, double L) {
		for (seed other : seeds) {
            if (s == other) continue;

            double dist = WatermelonMathUtil.distance(s, other);
            if (dist < Constants.seed_diameter) {
                System.out.printf("Seeds too close! The distance between two seeds is %f.\n", dist);
                return false;
            } else {
                //System.out.printf("YO. The distance between two seeds is %f.\n", dist);
            }
		}

        if ((s.x < Constants.wall_spacing || W - s.x < Constants.wall_spacing) ||
            (s.y < Constants.wall_spacing || L - s.y < Constants.wall_spacing)) {
            System.out.printf("The seed at (%f, %f) is too close to the wall\n", s.x, s.y);
            return false;
        }

		for (Pair tree : trees) {
            double dist = WatermelonMathUtil.distance(s, tree);
            if (dist < Constants.tree_diameter) {
                System.out.printf("The seed at (%f, %f) is too close to the tree at (%f, %f), %f\n",
                                s.x,
                                s.y,
                                tree.x,
                                tree.y,
                                dist);
                return false;
            }
		}

		return true;
	}
}
