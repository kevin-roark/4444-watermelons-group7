
package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public class RotationJigglingStrategy implements IJigglingStrategy {

  private static final double THETA_DELTA = 0.01;

  double w;
  double h;
  double s;
  ArrayList<seed> seeds;

  ArrayList<seed> jiggleSeeds(ArrayList<seed> seeds, ArrayList<Pair> trees, double width, double height, double s) {
      this.w = width;
      this.h = height;
      this.s = s;
      this.seeds = seeds;

      for (GraphNode node : rotationalCandidates) {
          List<GraphNode> relevantNeighbors = neighborsToRotateAround(node);

          jiggleSeed(node.center, relevantNeighbors);
      }

      return seeds;
  }

  private void jiggleSeed(seed mySeed, List<GraphNode> relevantNeighbors) {
      double currentScore = Analysis.calculateSeedScore(node.center, seeds, s);

      ArrayList<seed> rotatedNeighborSeeds = new ArrayList<seed>();

      // find the best rotation about each neighbor
      for (GraphNode neighbor : relevantNeighbors) {
          boolean rotatePositively = shouldRotatePositively(mySeed, neighbor.center, s);
          double thetaDelta = rotatePositively? THETA_DELTA : -THETA_DELTA;

          double theta = thetaDelta;
          double newScore = currentScore;
          seed finalSeed = mySeed;
          do {
              seed rotatedSeed = rotateSeed(mySeed, neighbor.center, theta);
              double rotatedScore = Analysis.calculateSeedScore(rotatedseed, seeds, s);

              if (!Analyis.validateSeed(rotatedSeed, seeds, w, h) || rotatedScore <= newScore) {
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
      seed bestSeed = mySeed;
      for (seed rotatedSeed : rotatedNeighborSeeds) {
          double rotatedScore = Analysis.calculateSeedScore(rotatedSeed, seeds, s);
          if (rotatedScore > bestScore) {
              bestScore = rotatedSCore;
              bestSeed = rotatedSeed;
          }
      }

      mySeed.x = bestSeed.x;
      mySeed.y = bestSeed.y;
  }

  private static List<GraphNode> rotationalCandidates(List<GraphNode> nodes) {
      ArrayList<GraphNode> candidates = new ArrayList<GraphNode>();

      for (GraphNode node : nodes) {
        if (node.neighbors.size() <= 4) {
            candidates.add(node);
        }
      }

      return candidates;
  }

  private static Pair nearestTree(seed s, List<Pair> trees) {
      double minDist = Math.INFINITY;
      Pair nearestTree = null;

      for (Pair p : trees) {
          double dist = WatermelonMathUtil.distance(p, s);
          if (dist < minDist) {
              minDist = dist;
              nearestTree = p;
          }
      }

      return rearestTree;
  }

  private static List<GraphNode> neighborsToRotateAround(GraphNode n) {
      ArrayList<GraphNode> rotateableNeighbors = new ArrayList<GraphNode>();

      for (GraphNode neighbor : n.neighbors) {
          if (neighbor.neighbords.size() <= 5 && neighbor.center.tetraploid == n.center.tetraploid) {
            rotateableNeighbors.add(neighbor);
          }
      }

      return rotateableNeighbors;
  }

  private boolean shouldRotatePositively(seed center, seed origin, double s) {
    double currentScore = Analysis.calculateSeedScore(center, seeds, s);

    seed positiveRotationSeed = rotateSeed(center, origin, THETA_DELTA);
    seed negativeRotationSeed = rotateSeed(center, origin, -THETA_DELTA);

    double positiveScore = Analysis.calculateSeedScore(positiveRotationSeed, seeds, s);
    double negativeScore = Analysis.calculateSeedScore(negativeRotationSeed, seeds, s);

    return positiveScore >= negativeScore;
  }

  private static seed rotateSeed(seed s, seed origin, double radians) {
      Vector2 seedVector = new Vector2(s);
      Vector2 originVector = new Vector2(origin);

      return seedVector.rotateRelative(originVector, radians).updateSeed(s);
  }

}
