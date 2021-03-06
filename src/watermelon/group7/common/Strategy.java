package watermelon.group7.common;

import java.util.*;
import watermelon.sim.*;

public class Strategy extends Player {
    IPackingStrategy  packing;
    IFillingStrategy  filling;
    ILabelingStrategy labeling;
    IJigglingStrategy jiggling;

    public String name;

    public Strategy(IPackingStrategy packing, ILabelingStrategy labeling) {
        this(packing, labeling, null);
    }

    public Strategy(IPackingStrategy packing, ILabelingStrategy labeling, IJigglingStrategy jiggler) {
        this(packing, null, labeling, jiggler);
    }

    public Strategy(IPackingStrategy packing, IFillingStrategy filling, ILabelingStrategy labeling, IJigglingStrategy jiggler) {
        this(packing, filling, labeling, jiggler, "STRATEGY");
    }

    public Strategy(IPackingStrategy packing, IFillingStrategy filling, ILabelingStrategy labeling, IJigglingStrategy jiggler, String name) {
        this.packing = packing;
        this.filling = filling;
        this.labeling = labeling;
        this.jiggling = jiggler;
        this.name = name;
    }

    public void init() {
    }

	public ArrayList<seed> move(ArrayList<Pair> trees, double w, double l, double s) {
        ArrayList<seed> seeds = packing.generatePacking(trees, w, l);

        if (this.filling != null) {
            filling.fillSeeds(seeds, trees, w, l);
        }

        labeling.labelSeeds(seeds, s);

        if (this.jiggling != null) {
            jiggling.jiggleSeeds(seeds, trees, w, l, s);
        }

        printSummary(seeds, trees, w, l, s);

        return seeds;
    }

    private static final String BOLD_BLUE="\033[1;34m";
    private static final String CLEAR    ="\033[0m";

    public void printSummary(ArrayList<seed> seeds, ArrayList<Pair> trees, double w, double l, double s) {
        System.out.println();
        System.out.printf(BOLD_BLUE + "Seeds Placed:          " + CLEAR +" %d\n", seeds.size());
        System.out.printf(BOLD_BLUE + "Max Seeds Possible:    " + CLEAR +" %d\n", Analysis.maxSeedsPossible(trees, l, w));
        System.out.printf(BOLD_BLUE + "Packing Efficiency:    " + CLEAR +" %1.2f%%\n", 100.0 * Analysis.calculatePackingEfficiency(seeds, trees, l, w));
        System.out.printf("------------------------------------------\n");
        System.out.printf(BOLD_BLUE + "Average Score Per Seed:" + CLEAR +" %f\n", Analysis.calculateBoardScore(seeds, s) / seeds.size());
        System.out.println();
        System.out.printf(BOLD_BLUE + "Total Score:           " + CLEAR +" %f\n", Analysis.calculateBoardScore(seeds, s));
        System.out.println();
    }
}
