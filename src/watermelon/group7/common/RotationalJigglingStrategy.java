
package watermelon.group7.common;

import watermelon.sim.*;

import watermelon.group7.*;
import java.util.*;

public class RotationalJigglingStrategy implements IJigglingStrategy {

    private static final double THETA_DELTA = 0.01;

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

            jiggleSeed(mySeed, relevantNeighbors);
        }

        return seeds;
    }

    private void jiggleSeed(Seed mySeed, List<Seed> relevantNeighbors) {
        double currentScore = Analysis.calculateSeedScore(mySeed, seeds, s);
        Seed originalSeedCopy = seedCopy(mySeed);

        ArrayList<Seed> rotatedNeighborSeeds = new ArrayList<Seed>();

        // find the best rotation about each neighbor
        for (Seed neighbor : relevantNeighbors) {
            boolean rotatePositively = shouldRotatePositively(mySeed, neighbor, s);
            double thetaDelta = rotatePositively? THETA_DELTA : -THETA_DELTA;

            double theta = thetaDelta;
            double newScore = currentScore;
            Seed finalSeed = mySeed;
            do {
                Seed rotatedSeed = rotateSeed(mySeed, neighbor, theta);
                updateSeed(mySeed, rotatedSeed);

                double rotatedScore = Analysis.calculateSeedScore(rotatedSeed, seeds, s);
                if (!Analysis.validateSeed(mySeed, seeds, trees, w, h) || rotatedScore <= newScore) {
                    break;
                }

                newScore = rotatedScore;
                theta += thetaDelta;
                finalSeed = rotatedSeed;
            } while (true);

            rotatedNeighborSeeds.add(finalSeed);
        }

        // find the neighbor that gave us best results
        double bestScore = currentScore;
        Seed bestSeed = originalSeedCopy;
        for (Seed rotatedSeed : rotatedNeighborSeeds) {
            double rotatedScore = Analysis.calculateSeedScore(rotatedSeed, seeds, s);
            if (rotatedScore > bestScore) {
                bestScore = rotatedScore;
                bestSeed = rotatedSeed;
            }
        }

        updateSeed(mySeed, bestSeed);
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
            ArrayList<Seed> doubleNeighbors = graph.get(neighbor);

            if (doubleNeighbors != null && doubleNeighbors.size() <= 5 && neighbor.tetraploid == mySeed.tetraploid) {
              rotateableNeighbors.add(neighbor);
            }
        }

        System.out.println("rotateable neighbors size: " + rotateableNeighbors.size());

        return rotateableNeighbors;
    }

    private boolean shouldRotatePositively(Seed mySeed, Seed origin, double s) {
      double currentScore = Analysis.calculateSeedScore(mySeed, seeds, s);

      Seed positiveRotationSeed = rotateSeed(mySeed, origin, THETA_DELTA);
      Seed negativeRotationSeed = rotateSeed(mySeed, origin, -THETA_DELTA);

      double positiveScore = Analysis.calculateSeedScore(positiveRotationSeed, seeds, s);
      double negativeScore = Analysis.calculateSeedScore(negativeRotationSeed, seeds, s);

      return positiveScore >= negativeScore;
    }

    private static Seed rotateSeed(seed s, seed origin, double radians) {
        Vector2 seedVector = new Vector2(s);
        Vector2 originVector = new Vector2(origin);

        return seedVector.rotateRelative(originVector, radians).updateSeed(s);
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
