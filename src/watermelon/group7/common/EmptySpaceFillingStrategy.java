
package watermelon.group7.common;

import watermelon.sim.*;

import watermelon.group7.*;
import java.util.*;

public class EmptySpaceFillingStrategy implements IFillingStrategy {

    private static final double DEFAULT_SPACE_DELTA = 0.05;

    double spaceDelta;

    public EmptySpaceFillingStrategy() {
        this.spaceDelta = DEFAULT_SPACE_DELTA;
    }

    public EmptySpaceFillingStrategy(double delta) {
        this.spaceDelta = delta;
    }

    public ArrayList<seed> fillSeeds(ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height) {
        double wall = Constants.wall_spacing;

        for (double y = wall; y < height - wall; y += spaceDelta) {
            for (double x = wall; x < width - wall; x += spaceDelta) {
                seed s = new Seed(x, y, false);
                if (Analysis.silentlyValidateSeed(s, seeds, trees, width, height)) {
                    System.out.printf("mmm found a empty zone at %f %f\n", x, y);
                    seeds.add(s);
                }
            }
        }

        return seeds;
    }
}
