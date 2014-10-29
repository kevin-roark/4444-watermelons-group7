package watermelon.group7;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class WatermelonMathUtil {

  static double distance(seed tmp, Pair pair) {
    return Math.sqrt((tmp.x - pair.x) * (tmp.x - pair.x) + (tmp.y - pair.y) * (tmp.y - pair.y));
  }

  static double distance(seed a, Point b) {
    return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
  }
  
  static double distance(seed a, seed b) {
    return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
  }

	static double calculateScore(ArrayList<seed> seedlist, double s) {
		double total = 0;
		
		for (int i = 0; i < seedlist.size(); i++) {
			double score;
			double chance = 0.0;
			double totaldis = 0.0;
			double difdis = 0.0;
			for (int j = 0; j < seedlist.size(); j++) {
				if (j != i) {
					totaldis = totaldis
							+ Math.pow(
									distance(seedlist.get(i),
											seedlist.get(j)), -2);
				}
			}
			for (int j = 0; j < seedlist.size(); j++) {
				if (j != i
						&& ((seedlist.get(i).tetraploid && !seedlist.get(j).tetraploid) || (!seedlist
								.get(i).tetraploid && seedlist.get(j).tetraploid))) {
					difdis = difdis
							+ Math.pow(
									distance(seedlist.get(i),
											seedlist.get(j)), -2);
				}
			}
			//System.out.println(totaldis);
			//System.out.println(difdis);
			chance = difdis / totaldis;
			score = chance + (1 - chance) * s;
			seedlist.get(i).score = score;
			total = total + score;
		}
		return total;
	}
}
