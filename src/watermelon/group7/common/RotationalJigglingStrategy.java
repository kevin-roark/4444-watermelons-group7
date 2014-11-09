
package watermelon.group7.common;

import watermelon.sim.*;

import watermelon.group7.*;
import java.util.*;

public class RotationalJigglingStrategy implements IJigglingStrategy {

    private static final double THETA_DELTA = 0.005;

    double w;
    double h;
    double s;
    ArrayList<seed> seeds;
    ArrayList<Pair> trees;
    HashMap<Seed, ArrayList<Seed>> graph;

    public ArrayList<seed> jiggleSeeds(ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height, double s) {
        this.w = width;
        this.h = height;
        this.s = s;
        this.seeds = seeds;
        this.trees = trees;

        this.graph = GraphNode.getMapGraph(seeds);
        List<Seed> rotationalCandidates = rotationalCandidates(this.graph);

        for (Seed mySeed : rotationalCandidates) {
            List<Seed> relevantNeighbors = neighborsToRotateAround(mySeed);

            seed greatSeed = getseedofSeed(mySeed, seeds);
            jiggleSeed(greatSeed, mySeed, relevantNeighbors);

            updateSeed(greatSeed, mySeed);
        }

        return seeds;
    }

    seed getseedofSeed(Seed needle, ArrayList<seed> haystack) {
      double best = Double.POSITIVE_INFINITY;
      seed result = null;
      for (seed s : haystack) {
        double d = WatermelonMathUtil.distance(s, needle);
        if (d < best) {
          best = d;
          result = s;
        }
      }
      return result;
    }

    private void jiggleSeed(seed originalSeed, Seed mySeed, List<Seed> relevantNeighbors) {
        double currentScore = Analysis.calculateBoardScore(seeds, s);
        Seed originalSeedCopy = seedCopy(mySeed);

        ArrayList<Seed> rotatedNeighborSeeds = new ArrayList<Seed>();

        // find the best rotation about each neighbor
        for (Seed neighbor : relevantNeighbors) {
            if (!anyRotationDirectionIsValid(originalSeed, neighbor)) continue;

            boolean rotatePositively = shouldRotatePositively(originalSeed, neighbor, s);
            double thetaDelta = rotatePositively? THETA_DELTA : -THETA_DELTA;

            //System.out.printf("Rotating %s around %s with %f\n", originalSeedCopy, neighbor, thetaDelta);

            double theta = thetaDelta;
            double newScore = currentScore;
            Seed finalSeed = null;
            do {
                Seed rotatedSeed = rotateSeed(mySeed, neighbor, theta);
                updateSeed(mySeed, rotatedSeed);
                updateSeed(originalSeed, mySeed);

                double rotatedScore = Analysis.calculateBoardScore(seeds, s);
                if (!Analysis.validateBoard(seeds, trees, w, h) || rotatedScore <= newScore) {
                    break;
                }

                updateSeed(mySeed, originalSeedCopy);
                newScore = rotatedScore;
                theta += thetaDelta;
                finalSeed = rotatedSeed;
            } while (true);

            if (finalSeed != null) {
              //System.out.printf("Score diff from above: %f\n", (newScore - currentScore));
              rotatedNeighborSeeds.add(finalSeed);
            }
        }

        // find the neighbor that gave us best results
        double bestScore = currentScore;
        Seed bestSeed = originalSeedCopy;

        for (Seed rotatedSeed : rotatedNeighborSeeds) {
            updateSeed(originalSeed, rotatedSeed);
            double rotatedScore = Analysis.calculateBoardScore(seeds, s);
            updateSeed(originalSeed, originalSeedCopy);

            if (rotatedScore > bestScore) {
                bestScore = rotatedScore;
                bestSeed = rotatedSeed;
            }
        }

        double scoreDiff = bestScore - currentScore;
        if (scoreDiff > 0) {
            //System.out.printf("GOOD::: best: %s // original: %s // diff: %f\n", bestSeed, originalSeedCopy, scoreDiff);
        }
        else if (scoreDiff < 0) {
            System.out.printf("BAD::: best: %s // original: %s // diff: %f\n", bestSeed, originalSeedCopy, scoreDiff);
        }

        updateSeed(mySeed, bestSeed);
        updateSeed(originalSeed, bestSeed);
    }

    private static List<Seed> rotationalCandidates(HashMap<Seed, ArrayList<Seed>> graph) {
        ArrayList<Seed> candidates = new ArrayList<Seed>();

        for (Seed mySeed : graph.keySet()) {
          ArrayList<Seed> neighbors = graph.get(mySeed);

          if (neighbors.size() <= 4) {
              candidates.add(mySeed);
          }
        }

        return candidates;
    }

    private List<Seed> neighborsToRotateAround(Seed mySeed) {
        ArrayList<Seed> rotateableNeighbors = new ArrayList<Seed>();

        ArrayList<Seed> neighbors = graph.get(mySeed);

        for (Seed neighbor : neighbors) {
            if (neighbor.tetraploid == mySeed.tetraploid) continue;

            ArrayList<Seed> doubleNeighbors = graph.get(neighbor);

            if (doubleNeighbors != null && doubleNeighbors.size() <= 5) {
              rotateableNeighbors.add(neighbor);
            }
        }

        return rotateableNeighbors;
    }

    private boolean rotationIsValid(seed originalSeed, Seed origin, double theta) {
        Seed originalSeedCopy = seedCopy(originalSeed);
        Seed mySeed = new Seed(originalSeed);

        Seed rotatedSeed = rotateSeed(mySeed, origin, theta);
        updateSeed(originalSeed, rotatedSeed);

        boolean valid = Analysis.validateBoard(seeds, trees, w, h);

        updateSeed(originalSeed, originalSeedCopy);

        return valid;
    }

    private boolean anyRotationDirectionIsValid(seed originalSeed, Seed origin) {
        boolean canRotate = rotationIsValid(originalSeed, origin, THETA_DELTA) || rotationIsValid(originalSeed, origin, -THETA_DELTA);
        return canRotate;
    }

    private boolean shouldRotatePositively(seed originalSeed, Seed origin, double s) {
        Seed mySeed = new Seed(originalSeed);
        Seed originalSeedCopy = seedCopy(originalSeed);

        double currentScore = Analysis.calculateBoardScore(seeds, s);

        double positiveScore, negativeScore;

        if (!rotationIsValid(originalSeed, origin, THETA_DELTA)) {
            positiveScore = 0.0f;
        } else {
            Seed positiveRotationSeed = rotateSeed(mySeed, origin, THETA_DELTA);
            updateSeed(originalSeed, positiveRotationSeed);
            positiveScore = Analysis.calculateBoardScore(seeds, s);
        }

        if (!rotationIsValid(originalSeed, origin, -THETA_DELTA)) {
            negativeScore = 0.0f;
        } else {
            Seed negativeRotationSeed = rotateSeed(mySeed, origin, THETA_DELTA);
            updateSeed(originalSeed, negativeRotationSeed);
            negativeScore = Analysis.calculateBoardScore(seeds, s);
        }

        updateSeed(originalSeed, originalSeedCopy);

        return positiveScore >= negativeScore;
    }

    private static Seed rotateSeed(seed s, seed origin, double radians) {
        Vector2 seedVector = new Vector2(s);
        Vector2 originVector = new Vector2(origin);

        Vector2 rotatedSeed = seedVector.rotateAbout(originVector, radians);

        return rotatedSeed.updateSeed(s);
    }

    private static void updateSeed(seed oldSeed, seed newSeed) {
      oldSeed.x = newSeed.x;
      oldSeed.y = newSeed.y;
      oldSeed.tetraploid = newSeed.tetraploid;
    }

    private static Seed seedCopy(seed mySeed) {
      return new Seed(mySeed.x, mySeed.y, mySeed.tetraploid);
    }

}
