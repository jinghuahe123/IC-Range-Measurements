public class WavePair extends Wave {
    public final double[] originalWave;
    public final double[] delayedWave;

    public WavePair(double[] originalWave, double[] delayedWave) {
        this.originalWave = originalWave;
        this.delayedWave = delayedWave;
    }


    /**
     *
     * @param sineWave          Array of sine wave values
     * @param timeBase          Time between samples in milliseconds
     * @param timeDelay         Total time delay in seconds
     * @return                  Array containing the delayed sine wave values
     */
    static WavePair reboundDelay(double[] sineWave, double timeBase, double timeDelay) {
        if (timeDelay < 0) {
            throw new IllegalArgumentException("Time delay must be non-negative");
        }

        int delaySamples = (int) Math.round(timeDelay / timeBase);
        int newLength = sineWave.length + delaySamples;

        // Create delayed wave with leading zeros
        double[] paddedWave = new double[newLength];
        for (int i=0; i<delaySamples; i++) {
            paddedWave[i] = 0;
        }
        System.arraycopy(sineWave, 0, paddedWave, delaySamples, sineWave.length);

        // Create original wave with trailing zeros
        double[] originalWave = new double[newLength];
        System.arraycopy(sineWave, 0, originalWave, 0, sineWave.length);
        for (int i=sineWave.length; i<originalWave.length; i++) {
            originalWave[i] = 0;
        }

        return new WavePair(originalWave, paddedWave);
    }

    /**
     *
     * @param sineWave          Input original array
     * @param paddedWave        Input padded array
     * @return                  New array of sum of two inputs
     */
    double[] addWaves(double[] sineWave, double[] paddedWave) {
        if (sineWave.length != paddedWave.length) {
            throw new IllegalArgumentException("Input arrays must be the same length.");
        }

        double[] result = new double[sineWave.length];

        for (int i=0; i<sineWave.length; i++) {
            result[i] = sineWave[i] + paddedWave[i];
        }

        return result;
    }

    /**
     *
     * @param received      Combined received waveform of transmit and reflected
     * @param sine          Transmit waveform
     * @return              Transmit wave offset time
     */
    int sendDelayPos(double[] received, double[] sine) {

        double[] receivedWave = received.clone();
        double[] sineWave = sine.clone();

        if (receivedWave.length < sineWave.length) {
            throw new IllegalArgumentException("Received wave cannot be shorter than sent wave.");
        }

        int sineWavePos = 0;
        double EPSILON = 1e-17; // Tolerance for "close enough to zero"

        for (int i=0; i<receivedWave.length-sineWave.length; i++) {
            for (int j = 0; j < sineWave.length; j++) {
                receivedWave[j + i] -= sineWave[j];
            }

            double total = 0;
            for (int j = 0; j < receivedWave.length; j++) {
                total += receivedWave[j];
                //System.out.println(result[i]);
            }
            total /= receivedWave.length;
            //System.out.println(total);

            if (Math.abs(total) < EPSILON) {
                sineWavePos = i;
                break;
            }
        }
        return sineWavePos;
    }
}