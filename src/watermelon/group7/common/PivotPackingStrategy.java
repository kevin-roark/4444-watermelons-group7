package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class PivotPackingStrategy implements IPackingStrategy {
    public static final double EPSILON = 0.00;

    double delta;

    public PivotPackingStrategy() {
       this.delta = Math.PI / 2;
    }

    public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
        Set<seed> free_seeds = new HashSet<seed>();
        ArrayList<seed> seeds = new ArrayList<seed>();

        for (Pair tree : trees) {
            free_seeds.addAll(placeRing(new Vector2(tree), 0.0, this.delta, seeds, trees, width, height));
        }

        return generatePacking(free_seeds, 0.0, this.delta, trees, width, height);
    }

    public ArrayList<seed> generatePacking(Set<seed> free_seeds, double orientation, double theta_delta, ArrayList<Pair> trees, double width, double height) {
        ArrayList<seed> seeds = new ArrayList<seed>();
        do {
            ArrayList<seed> free = new ArrayList<seed>();
            free.addAll(free_seeds);
            for (seed s : free) {
                ArrayList<seed> placed = placeRing(new Vector2(s), orientation, theta_delta, seeds, trees, width, height);
                //System.err.format("Placed %d seeds.\n", placed.size());
                free_seeds.remove(s);
                free_seeds.addAll(placed);
            }
        } while (free_seeds.size() > 0);

        return seeds;
    }

    void addSeed(seed s, ArrayList<seed> list) {
        list.add(s);
    }

    ArrayList<seed> placeHex(Vector2 center, double orientation, ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height) {
        return placeRing(center, orientation, Constants.PI_OVER_3, seeds, trees, width, height);
    }

    ArrayList<seed> placeRing(Vector2 center, double orientation, double theta_delta, ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height) {
        ArrayList<seed> placed = new ArrayList<seed>();
        for (double d = orientation; d < Constants.TWO_PI; d += theta_delta) {
            seed potential = getClosestValid(center, d, Constants.seed_diameter, seeds, trees, width, height);

            if (potential != null) {
                placed.add(potential);
                addSeed(potential, seeds);
            } else {
                //System.out.println("Failed to validate a seed.");
            }
        }

        return placed;
    }

    seed getClosestValid(Vector2 center, double d, double radius, ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height) {
        double epsilon = 0.0;
        seed closest = null;

        boolean check;
        do {
            Vector2 offset = Vector2.fromOrientationAndLength(d, Constants.seed_diameter - epsilon);
            Vector2 placement = offset.add(center);
            seed tmp = new seed(placement.x, placement.y, false);
            epsilon -= 1e-6;

            check = Analysis.silentlyValidateSeed(tmp, seeds, trees, width, height);
            if (check) {
                closest = tmp;
                break; // KILL IT
            }
        } while(check);

        return closest;
    }
}
