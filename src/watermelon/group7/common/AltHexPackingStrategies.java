package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class AltHexPackingStrategies {
    public static class TopHexPackingStrategy implements IPackingStrategy {
        public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
            HexPackingStrategy hex = new HexPackingStrategy();
            return hex.generatePacking(trees, width, height);
        }
    }

    public static class BottomHexPackingStrategy implements IPackingStrategy {
        ArrayList<Pair> flipTrees(ArrayList<Pair> trees, double height) {
            ArrayList<Pair> copy = new ArrayList<Pair>(trees);

            return copy;
        }
        ArrayList<seed> flipSeeds(ArrayList<seed> seeds, double height) {
            ArrayList<seed> copy = new ArrayList<seed>(seeds);

            return copy;
        }
        public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
            HexPackingStrategy hex = new HexPackingStrategy();

            ArrayList<Pair> flipped_trees = flipTrees(trees, height);
            ArrayList<seed> flipped_seeds = hex.generatePacking(flipped_trees, width, height);

            return flipSeeds(flipped_seeds, height);
        }
    }
}
