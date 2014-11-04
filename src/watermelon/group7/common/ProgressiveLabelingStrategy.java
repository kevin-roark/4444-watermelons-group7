package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public class ProgressiveLabelingStrategy implements ILabelingStrategy {
    List<ILabelingStrategy> strategies;

    public ProgressiveLabelingStrategy(List<ILabelingStrategy> strategies) {
        this.strategies = strategies;
    }

    public void labelSeeds(ArrayList<seed> seeds, double s) {
        for (ILabelingStrategy strategy : strategies) {
            strategy.labelSeeds(seeds, s);
        }
    }
}
