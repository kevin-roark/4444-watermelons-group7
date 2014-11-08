
package watermelon.group7.common;

import watermelon.sim.Pair;
import watermelon.sim.Point;

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

        for (Seed seed : rotationalCandidates) {
            List<Seed> relevantNeighbors = neighborsToRotateAround(seed);

            jiggleSeed(seed, relevantNeighbors);
        }

        return seeds;
    }

    private void jiggleSeed(Seed mySeed, List<Seed> relevantNeighbors) {
        double currentScore = Analysis.calculateSeedScore(mySeed, seeds, s);
        seed originalSeedCopy = seedCopy(mySeed);

        ArrayList<seed> rotatedNeighborSeeds = new ArrayList<Seed>();

        // find the best rotation about each neighbor
        for (Seed neighbor : relevantNeighbors) {
            boolean rotatePositively = shouldRotatePositively(mySeed, neighbor, s);
            double thetaDelta = rotatePositively? THETA_DELTA : -THETA_DELTA;

            double theta = thetaDelta;
            double newScore = currentScore;
            seed finalSeed = mySeed;
            do {
                seed rotatedSeed = rotateSeed(mySeed, neighbor, theta);
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
        seed bestSeed = originalSeedCopy;
        for (seed rotatedSeed : rotatedNeighborSeeds) {
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

        for (Seed seed : graph.keySet()) {
          ArrayList<Seed> neighbors = graph.get(seed);

          if (neighbors.size() <= 4) {
              candidates.add(seed);
          }
        }

        return candidates;
    }

    private List<Seed> neighborsToRotateAround(Seed seed) {
        ArrayList<Seed> rotateableNeighbors = new ArrayList<Seed>();

        ArrayList<Seed> neighbors = graph.get(seed);

        for (Seed neighbor : neighbors) {
            ArrayList<Seed> doubleNeighbors = graph.get(neighbor);

            if (doubleNeighbors.size() <= 5 && neighbor.tetraploid == seed.tetraploid) {
              rotateableNeighbors.add(neighbor);
            }
        }

        return rotateableNeighbors;
    }

    private boolean shouldRotatePositively(Seed seed, Seed origin, double s) {
      double currentScore = Analysis.calculateSeedScore(seed, seeds, s);

      seed positiveRotationSeed = rotateSeed(seed, origin, THETA_DELTA);
      seed negativeRotationSeed = rotateSeed(seed, origin, -THETA_DELTA);

      double positiveScore = Analysis.calculateSeedScore(positiveRotationSeed, seeds, s);
      double negativeScore = Analysis.calculateSeedScore(negativeRotationSeed, seeds, s);

      return positiveScore >= negativeScore;
    }

    private static seed rotateSeed(seed s, seed origin, double radians) {
        Vector2 seedVector = new Vector2(s);
        Vector2 originVector = new Vector2(origin);

        return seedVector.rotateRelative(originVector, radians).updateSeed(s);
    }

    private static void updateSeed(seed oldSeed, seed newSeed) {
      oldSeed.x = newSeed.x;
      oldSeed.y = newSeed.y;
      oldSeed.tetraploid = newSeed.tetraploid;
    }

    private static seed seedCopy(seed mySeed) {
      return new seed(mySeed.x, mySeed.y, mySeed.tetraploid);
    }

}
