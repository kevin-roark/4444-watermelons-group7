package group.common;

import watermelon.sim.*;
import java.lang.Math;

public class Analysis {
    static double calculatePackingEfficiency(ArrayList<seed> seeds, 
                                             ArrayList<Pair> trees, 
                                             double length, double width) {
        double total_area = ((watermelon.length - watermelon.distowall) * 
                             (watermelon.width - watermelon.distowall));

        double area_available = total_area - treeArea() * trees.size();

        double seeds_max = Math.floor(area_available / seedArea());

        return seeds.size() / seeds_max;
    }

    static double seedArea() {
        return watermelon.distoseed * watermelon.distoseed * Math.PI;
    }
    static double treeArea() {
        return watermelon.distotree * watermelon.distotree * Math.PI;
    }
}
