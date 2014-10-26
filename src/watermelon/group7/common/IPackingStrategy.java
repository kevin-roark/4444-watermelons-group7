package group7.common;

import watermelon.sim.*;

public interface IPackingStrategy {
    ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height);
}
