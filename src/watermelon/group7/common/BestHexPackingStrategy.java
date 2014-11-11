package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class BestHexPackingStrategy extends ChooseBestPackingStrategy {
    public BestHexPackingStrategy(double width, double height) {
        // this pains me to write, but it is for the best.
        super(DirectionPackingStrategy.getTopLeftHorizontalHex(width, height),
              DirectionPackingStrategy.getTopLeftVerticalHex(width, height),
              DirectionPackingStrategy.getTopRightHorizontalHex(width, height),
              DirectionPackingStrategy.getTopRightVerticalHex(width, height),
              DirectionPackingStrategy.getBottomLeftHorizontalHex(width, height),
              DirectionPackingStrategy.getBottomLeftVerticalHex(width, height),
              DirectionPackingStrategy.getBottomRightHorizontalHex(width, height),
              DirectionPackingStrategy.getBottomRightVerticalHex(width, height));
    }
}
