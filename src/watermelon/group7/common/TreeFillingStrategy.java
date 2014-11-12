
package watermelon.group7.common;

import watermelon.sim.*;

import watermelon.group7.*;
import java.util.*;

public class TreeFillingStrategy extends EmptySpaceFillingStrategy {

    private static final double THETA_DELTA = 0.05;

    public ArrayList<seed> fillSeeds(ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height) {
        double spacing = Constants.tree_diameter;

        for (Pair tree : trees) {
            for (double theta = 0; theta < Math.PI * 2; theta += THETA_DELTA) {
                double x = Math.cos(theta) * spacing;
                double y = Math.sin(theta) * spacing;

                seed s = new Seed(x, y, false);
                if (Analysis.silentlyValidateSeed(s, seeds, trees, width, height)) {
                    System.out.println("FOUND U A SEED NEAR A TREE");
                    seeds.add(s);
                    break;
                }
            }
        }

        return performSeedFill(seeds, trees, width, height);
    }
}
