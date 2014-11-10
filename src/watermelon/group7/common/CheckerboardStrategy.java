package watermelon.group7.common;

import java.util.*;
import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;

public class CheckerboardStrategy extends Strategy {
    public CheckerboardStrategy() {
        super(new CheckboardPackingStrategy(), new EmptySpaceFillingStrategy(), new SelfishLabelingStrategy(), new RotationalJigglingStrategy());

        this.name = "CHECKERBOARD";
    }

    public static class CheckboardPackingStrategy implements IPackingStrategy {
        public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
            ArrayList<seed> results = new ArrayList<seed>();

            boolean row_value = true;
            for (double y = Constants.wall_spacing;
                        y < height - Constants.wall_spacing;
                        y += Constants.seed_diameter) {
                boolean value = row_value;
                row_value = !row_value;

                for (double x = Constants.wall_spacing;
                            x < width - Constants.wall_spacing;
                            x += Constants.seed_diameter) {
                    seed tmp = new seed(x, y, value);
                    value = !value;

                    boolean add = true;
                    if (tmp.x >= width - Constants.wall_spacing) {
                        add = false;
                    }

                    for (Pair tree : trees) {
                        if (WatermelonMathUtil.distance(tmp, tree) < Constants.tree_diameter) {
                            add = false;
                            break;
                        }
                    }

                    if (add) {
                        results.add(tmp);
                    }
                }
            }
            return results;
        }
    }
}
