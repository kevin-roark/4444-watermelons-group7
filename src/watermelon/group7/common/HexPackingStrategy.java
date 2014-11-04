package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class HexPackingStrategy implements IPackingStrategy {
    double getHexXOffset() {
        return Constants.seed_diameter * Math.sin(Math.PI / 6.0);
    }

    double getHexYOffset() {
        return Constants.seed_diameter * Math.cos(Math.PI / 6.0) - 0.00095;
    }

    public static final double EPSILON = 0.001;

    public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
        ArrayList<seed> results = new ArrayList<seed>();

        boolean shift = false;
        for (double y = Constants.wall_spacing; 
                    y < height - Constants.wall_spacing; 
                    y = y + getHexYOffset() + EPSILON) {

            for (double x = Constants.wall_spacing; 
                        x < width - Constants.wall_spacing; 
                        x = x + Constants.seed_diameter + EPSILON) {
                seed tmp = new seed(x, y, false);

                if (shift) {
                    tmp.x += getHexXOffset();
                }

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
            shift = !shift;
		}

        return results;
    }
}
