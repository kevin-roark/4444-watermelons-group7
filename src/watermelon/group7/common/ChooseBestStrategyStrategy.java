
package watermelon.group7.common;

import java.util.*;
import watermelon.sim.*;

public class ChooseBestStrategyStrategy extends Strategy {
    List<Strategy> strategies;

    public ChooseBestStrategyStrategy(Strategy... strategies) {
        this(Arrays.asList(strategies));
    }
    public ChooseBestStrategyStrategy(List<Strategy> strategies) {
        super(null, null, null);
        this.strategies = strategies;
    }

    public void init() {
        super.init();
    }

	public ArrayList<seed> move(ArrayList<Pair> trees, double w, double l, double s) {
        double best_score = 0.0;
        ArrayList<seed> best_placement = null;

        for (Strategy strat : strategies) {
            System.out.printf("Trying %s...\n", strat.name);

            ArrayList<seed> tmp = strat.move(trees, w, l, s);
            double score = Analysis.calculateBoardScore(tmp, s);
            
            if (score > best_score) {

                best_score = score;
                best_placement = tmp;
            }
        }

        return best_placement;
    }
}
