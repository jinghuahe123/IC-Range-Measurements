public class Main {
    public static void main(String[] args) {

        // Parameters for the sine wave
        double amplitude = 1.0;           // Maximum amplitude
        double timeBase = 0.01;            // Time between samples in milliseconds
        int numSamples = 1000;              // Number of samples (e.g., 1 ms / 0.1 ms = 10 samples)
        double frequencyHz = 1000.0;      // Frequency in Hertz
        double timeDelay = 0.3;

        System.out.printf("%d samples of sine wave with at %f frequency with amplitude %f and timebase %f generated. \nAdded artificial timeDelay of %f. \n", numSamples, frequencyHz, amplitude, timeBase, timeDelay);

        System.out.println();

        // Generate the sine waves
        double[] wave = sineWave.generate(amplitude, timeBase, numSamples, frequencyHz);
        WavePair waves = sineWave.reboundDelay(wave, timeBase, timeDelay);
        double [] result = sineWave.addWaves(waves.originalWave, waves.delayedWave);


        // Print the original sine wave values & write to csv
        //System.out.println("Sine Wave Samples:");
        //for (int i = 0; i < waves.originalWave.length; i++) {
        //    System.out.printf("Sample %d: %.6f%n", i, waves.originalWave[i]);
        //}
        String csvFileName = "original-sine.csv";
        sineWave.writeToCSV(waves.originalWave, timeBase, csvFileName);

        // Print the delayed sine wave values & write to csv
        //System.out.println("Delayed Sine Wave Samples:");
        //for (int i = 0; i < waves.delayedWave.length; i++) {
        //    System.out.printf("Sample %d: %.6f%n", i, waves.delayedWave[i]);
        //}
        String delayedCSV = "artificial-delayed-sine.csv";
        sineWave.writeToCSV(waves.delayedWave, timeBase, delayedCSV);

        // Write added waves to csv
        String addedSCV = "addedWaves.csv";
        sineWave.writeToCSV(result, timeBase, addedSCV);


        int sineWavePos = sineWave.sendDelayPos(result, wave);
        int totalDelay = sineWavePos + numSamples;

        double[] receivedData = result.clone();

        //System.out.println(sineWavePos);

        for (int i=sineWavePos-1; i<wave.length; i++) {
            receivedData[i] -= waves.originalWave[i];
        }

        //System.out.println(totalDelay);
        double[] integrateData = new double[result.length-totalDelay];

        for (int i=totalDelay; i<result.length; i++) {
            integrateData[i-totalDelay] = result[i];
        }

        //for (int i=0; i<integrateData.length; i++) {
        //    System.out.println(integrateData[i]);
        //}

        String integrated = "to-parse_data-output.csv";
        sineWave.writeToCSV(integrateData, timeBase, integrated);

    }
}