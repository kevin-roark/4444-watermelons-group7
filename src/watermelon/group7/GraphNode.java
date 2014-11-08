package watermelon.group7;


import watermelon.sim.*;
import java.util.*;

public class GraphNode {
    public seed center;
    public ArrayList<seed> neighbors;

    public GraphNode(seed s, ArrayList<seed> neighbors) {
        center = s;
        this.neighbors = neighbors;
    }

    public static final double THRESHOLD_DISTANCE = 2.1;
    public static GraphNode fromAllSeeds(seed s, ArrayList<seed> seedlist, double threshold) {
        ArrayList<seed> neighbors = new ArrayList<seed>();

        for (seed n : seedlist) {
            if (n != s && WatermelonMathUtil.distance(s, n) < threshold) {
                neighbors.add(n);
            }
        }

        return new GraphNode(s, neighbors);
    }

    public static ArrayList<GraphNode> getGraph(ArrayList<seed> seeds) {
        return getGraph(seeds, THRESHOLD_DISTANCE);
    }

    public static HashMap<Seed, ArrayList<Seed>> getMapGraph(ArrayList<Seed> seeds) {
        return getMapGraph(seeds, THRESHOLD_DISTANCE);
    }

    public static ArrayList<GraphNode> getGraph(ArrayList<seed> seeds, double threshold) {
        ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();

        for (seed s : seeds) {
            nodes.add(fromAllSeeds(s, seeds, threshold));
        }

        return nodes;
    }

    public static HashMap<Seed, ArrayList<Seed>> getMapGraph(ArrayList<Seed> seeds, double threshold) {
        HashMap<Seed, ArrayList<Seed>> map = new HashMap<Seed, ArrayList<Seed>>();

        for (Seed seed : seeds) {
            GraphNode node = fromAllSeeds(seed, seeds, threshold);
            map.put(seed, node.neighbors);
        }

        return map;
    }

    public seed getCenter() { return center; };

    public double oppositeRatio() {
        return (double)countOppositeNeighbors() / neighbors.size();
    }
    public int countOppositeNeighbors() {
        int count = 0;
        for (seed n : neighbors) {
            if (n.tetraploid != center.tetraploid) {
                count += 1;
            }
        }
        return count;
    }
}
