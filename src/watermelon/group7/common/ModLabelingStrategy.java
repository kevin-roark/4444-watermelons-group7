package watermelon.group7.common;

import watermelon.sim.*;
import java.util.*;

public class ModLabelingStrategy implements ILabelingStrategy {
    int k;
    int modulus;

    public ModLabelingStrategy(int modulus) {
        this(2, modulus);
    }

    public ModLabelingStrategy(int k, int modulus) {
        this.k = k;
        this.modulus = modulus;
    }

    public void labelSeeds(ArrayList<seed> seeds, double s) {
        int i = 0;

        for (seed seed : seeds) {
            seed.tetraploid = (i % k == 0);

            i = (i + 1) % modulus;
        }
    }
}
