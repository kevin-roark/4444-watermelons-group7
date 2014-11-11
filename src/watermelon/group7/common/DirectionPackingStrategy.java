package watermelon.group7.common;

import watermelon.group7.WatermelonMathUtil;
import watermelon.sim.*;
import java.util.*;

public class DirectionPackingStrategy implements IPackingStrategy {
    public static final double EPSILON = 0;

    Vector2 pos;
    Vector2 row_dir;
    Vector2 off_row_dir;

    public DirectionPackingStrategy(Vector2 pos, Vector2 row_dir, Vector2 off_row_dir) {
        this.pos = pos;
        this.row_dir = row_dir;
        this.off_row_dir = off_row_dir;
    }

    public ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height) {
        ArrayList<seed> results = new ArrayList<seed>();

        Vector2 current = pos;
        boolean any;
        do {
            Vector2 next_row = current.add(off_row_dir);
            any = false;
            boolean valid = false;
            seed tmp;

            int first = 0;
            do {
                tmp = current.getSeed();
                valid = tryPlaceSeed(tmp, results, trees, width, height);
                if (valid) {
                    results.add(tmp);
                    any = true;
                }
                current = current.add(row_dir);
            } while (Analysis.seedOnBoard(tmp, width, height) || first++ < 100);
            current = next_row.sub(row_dir);
        } while(any);

        return results;
    }

    private boolean tryPlaceSeed(seed tmp, ArrayList<seed> results, ArrayList<Pair> trees, double width, double height) {
        boolean current = Analysis.silentlyValidateSeed(tmp, results, trees, width, height);
        if (current) return true;
        double eps = 1e-7;

        tmp.x += eps;
        current = Analysis.silentlyValidateSeed(tmp, results, trees, width, height);
        if (current) return true;
        else tmp.x -= eps;

        tmp.x -= eps;
        current = Analysis.silentlyValidateSeed(tmp, results, trees, width, height);
        if (current) return true;
        else tmp.x += eps;

        tmp.y -= eps;
        current = Analysis.silentlyValidateSeed(tmp, results, trees, width, height);
        if (current) return true;
        else tmp.y += eps;

        tmp.y += eps;
        current = Analysis.silentlyValidateSeed(tmp, results, trees, width, height);
        if (current) return true;

        return false;
    }


    public static DirectionPackingStrategy getTopLeftHorizontalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(Constants.wall_spacing + 0.0000001, Constants.wall_spacing),
            new Vector2(Constants.seed_diameter, 0),
            new Vector2(Constants.seed_diameter * Math.sin(Math.PI / 6.0),
                        Constants.seed_diameter * Math.cos(Math.PI / 6.0)));
    }
    public static DirectionPackingStrategy getTopLeftVerticalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(Constants.wall_spacing, Constants.wall_spacing),
            new Vector2(0, Constants.seed_diameter),
            new Vector2(Constants.seed_diameter * Math.cos(Math.PI / 6.0),
                        -Constants.seed_diameter * Math.sin(Math.PI / 6.0)));
    }

    public static DirectionPackingStrategy getTopRightHorizontalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(width - Constants.actual_wall_spacing, Constants.wall_spacing),
            new Vector2(-Constants.seed_diameter, 0),
            new Vector2(Constants.seed_diameter * Math.sin(Math.PI / 6.0),
                        Constants.seed_diameter * Math.cos(Math.PI / 6.0)));
    }

    public static DirectionPackingStrategy getTopRightVerticalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(width - Constants.wall_spacing, Constants.wall_spacing),
            new Vector2(0, Constants.seed_diameter),
            new Vector2(-Constants.seed_diameter * Math.cos(Math.PI / 6.0),
                        -Constants.seed_diameter * Math.sin(Math.PI / 6.0)));
    }

    public static DirectionPackingStrategy getBottomLeftHorizontalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(Constants.wall_spacing + 0.0000001, height - Constants.actual_wall_spacing),
            new Vector2(Constants.seed_diameter, 0),
            new Vector2(Constants.seed_diameter * Math.sin(Math.PI / 6.0),
                        -Constants.seed_diameter * Math.cos(Math.PI / 6.0)));
    }
    public static DirectionPackingStrategy getBottomLeftVerticalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(Constants.wall_spacing, height - Constants.wall_spacing),
            new Vector2(0, -Constants.seed_diameter),
            new Vector2(Constants.seed_diameter * Math.cos(Math.PI / 6.0),
                        Constants.seed_diameter * Math.sin(Math.PI / 6.0)));
    }

    public static DirectionPackingStrategy getBottomRightHorizontalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(width - Constants.actual_wall_spacing, height - Constants.actual_wall_spacing),
            new Vector2(-Constants.seed_diameter, 0),
            new Vector2(Constants.seed_diameter * Math.sin(Math.PI / 6.0),
                        -Constants.seed_diameter * Math.cos(Math.PI / 6.0)));
    }

    public static DirectionPackingStrategy getBottomRightVerticalHex(double width, double height) {
        return new DirectionPackingStrategy(
            new Vector2(width - Constants.wall_spacing, height - Constants.wall_spacing),
            new Vector2(0, -Constants.seed_diameter),
            new Vector2(-Constants.seed_diameter * Math.cos(Math.PI / 6.0),
                        Constants.seed_diameter * Math.sin(Math.PI / 6.0)));
    }
}
