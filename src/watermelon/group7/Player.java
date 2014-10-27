package watermelon.group7;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Player extends watermelon.sim.Player {
	public static final double distowall = 1.1;
	public static final double distotree = 2.2;
	public static final double distoseed = 2.01;

	public void init() {

	}

    public double getHexXOffset() {
        return distoseed * Math.sin(Math.PI / 6.0);
    }

    public double getHexYOffset() {
        return distoseed * Math.cos(Math.PI / 6.0);
    }

    public static final double EPSILON = 0.001;

	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width, double length, double s) {
		ArrayList<seed> seedlist = new ArrayList<seed>();
        boolean shift = false;
        int label = 0;
        for (double j = distowall; j < length - distowall; j = j + getHexYOffset() + EPSILON) {
            for (double i = distowall; i < width - distowall; i = i + distoseed + EPSILON) {
                boolean parity = (label % 2 == 0);

				Random random = new Random();

                seed tmp = new seed(i, j, parity);

                if (shift) {
                    tmp.x += getHexXOffset();
                }

				boolean add = true;
				for (int f = 0; f < treelist.size(); f++) {
					if (WatermelonMathUtil.distance(tmp, treelist.get(f)) < distotree) {
						add = false;
						break;
					}
				}

				if (add) {
					seedlist.add(tmp);
				}

                label = (label + 1) % 41;
			}
            shift = !shift;
		}

        if (!validateSeeds(seedlist)) {
            System.out.printf("Invalid setup!");
        }

		System.out.printf("seedlist size is %d", seedlist.size());
		return seedlist;
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
