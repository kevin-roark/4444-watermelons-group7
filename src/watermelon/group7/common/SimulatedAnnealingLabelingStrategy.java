package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public class SimulatedAnnealingLabelingStrategy implements ILabelingStrategy {
    static final double DEFAULT_MAX_ENERGY = 0.00000001;
    static final int DEFAULT_MAX_K = 2000;
    static final double DEFAULT_STARTING_TEMPERATURE = 10000.0;
    static final int DEFAULT_MAX_SEEDS_TO_FLIP = 2;

    static Random rander = new Random();

    double maxEnergy;
    double maxK;
    double startingTemperature;
    int maxSeedsToFlip;

    public SimulatedAnnealingLabelingStrategy() {
        this(DEFAULT_MAX_ENERGY, DEFAULT_MAX_K, DEFAULT_STARTING_TEMPERATURE, DEFAULT_MAX_SEEDS_TO_FLIP);
    }

    public SimulatedAnnealingLabelingStrategy(double maxEnergy, int maxK, double startingTemperature, int maxSeedsToFlip) {
        this.maxEnergy = maxEnergy;
        this.maxK = (double) maxK;
        this.startingTemperature = startingTemperature;
        this.maxSeedsToFlip = maxSeedsToFlip;
    }

    public void labelSeeds(ArrayList<seed> seeds, double s) {
        ArrayList<seed> currentState = seedsCopy(seeds);
        double currentEnergy = energy(currentState, s);

        ArrayList<seed> bestState = currentState;
        double bestEnergy = currentEnergy;

        System.out.printf("Starting config: %f %f %f\n", currentEnergy, maxEnergy, maxK);

        int k = 0;
        while (k < maxK && currentEnergy > maxEnergy) {
            double temp = temperature(k / maxK);

            ArrayList<seed> newState = neighbor(currentState, s);
            double newEnergy = energy(newState, s);
            double p = probability(currentEnergy, newEnergy, temp);
            
            System.out.printf("%d: new energy %f // p %f // temp %f // energy %f // best %f\n", k, newEnergy, p, temp, currentEnergy, bestEnergy);

            if (p > Math.random()) {
                currentState = seedsCopy(newState);
                currentEnergy = newEnergy;
            }

            if (newEnergy < bestEnergy) {
                bestState = newState;
                bestEnergy = newEnergy;
            }

            k++;
        }

        for (int i = 0; i < seeds.size(); i++) {
            seeds.get(i).tetraploid = bestState.get(i).tetraploid;
        }
    }

    private double energy(ArrayList<seed> seeds, double s) {
        return 1 / Analysis.calculateBoardScore(seeds, s);
    }

    private double temperature(double kRatio) {
        double kToGo = 1 - kRatio;

        return startingTemperature * 5 * Math.pow(kToGo, 1.2);
    }

    private double probability(double oldEnergy, double newEnergy, double temp) {
        if (newEnergy < oldEnergy) return 1.0;

        if (newEnergy - oldEnergy < .0000001) return 0.5;

        double t = temp;
        if (t > startingTemperature) {
            t = startingTemperature;
        }
        double tempRatio = t / startingTemperature;

        double weightedRatio = tempRatio * ((newEnergy - oldEnergy) / oldEnergy * 3);

        return weightedRatio;
    }

    private ArrayList<seed> neighbor(ArrayList<seed> seeds, double s) {
        ArrayList<seed> neighborSeeds = seedsCopy(seeds);

        SelfishLabelingStrategy labeler = new SelfishLabelingStrategy(1);

        labeler.labelSeeds(neighborSeeds, s);

        int seedsToFlip = rander.nextInt(maxSeedsToFlip);
        for (int i = 0; i <= seedsToFlip; i++) {
            seed randSeed = neighborSeeds.get(rander.nextInt(neighborSeeds.size()));

            randSeed.tetraploid = !randSeed.tetraploid;
        }

        return neighborSeeds;
    }

    private ArrayList<seed> seedsCopy(ArrayList<seed> seeds) {
        ArrayList<seed> copies = new ArrayList<seed>(seeds.size());

        for (int i = 0; i < seeds.size(); i++) {
            seed oldSeed = seeds.get(i);
            seed newSeed = new seed(oldSeed.x, oldSeed.y, oldSeed.tetraploid);
            copies.add(newSeed);
        }

        return copies;
    }
}
