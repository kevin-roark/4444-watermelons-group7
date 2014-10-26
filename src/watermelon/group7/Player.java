package watermelon.group7;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Player extends watermelon.sim.Player {
	static double distowall = 1.1;
	static double distotree = 2.2;
	static double distoseed = 1.01;

	public void init() {

	}

    public double getHexXOffset() {
        return 2 * distoseed / Math.cos(Math.PI / 4.0);
    }

    public double getHexYOffset() {
        return 2 * distoseed / Math.sin(Math.PI / 4.0);
    }

	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width, double length, double s) {
		ArrayList<seed> seedlist = new ArrayList<seed>();
        boolean shift = false;
		for (double i = distowall; i < width - distowall; i = i + distoseed) {
			for (double j = distowall; j < length - distowall; j = j + getHexYOffset()) {
				Random random = new Random();

				seed tmp;
				if (random.nextInt(2) == 0)
					tmp = new seed(i, j, false);
				else
					tmp = new seed(i, j, true);

                if (shift) {
                    tmp.i += getHexXOffset();
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
			}

            shift = !shift;
		}

		System.out.printf("seedlist size is %d", seedlist.size());
		return seedlist;
	}
}
