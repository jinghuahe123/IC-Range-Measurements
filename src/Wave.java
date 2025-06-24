import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Wave {
    /**
     * Generates a sine wave array.
     *
     * @param amplitude      Maximum amplitude of the sine wave
     * @param timeBase       Time between samples in milliseconds
     * @param numSamples     Number of samples to generate
     * @param frequencyHz    Frequency of the sine wave in Hertz
     * @return               Array containing the sine wave values
     */
    static double[] generate(double amplitude, double timeBase, int numSamples, double frequencyHz) {
        double timeStepSeconds = timeBase / 1000.0;
        double[] sineWave = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double timeSeconds = i * timeStepSeconds;
            sineWave[i] = amplitude * Math.sin(2 * Math.PI * frequencyHz * timeSeconds);
        }

        return sineWave;
    }

    /**
     * Writes sine wave data to a CSV file.
     *
     * @param sineWave       Array of sine wave values
     * @param timeBase       Time between samples in milliseconds
     * @param fileName       Name of the output CSV file
     */
    static void writeToCSV(double[] sineWave, double timeBase, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write header
            writer.write("Sample,Time (ms),Amplitude");
            writer.newLine();

            // Write data rows
            for (int i = 0; i < sineWave.length; i++) {
                double timeMs = i * timeBase; // Time in milliseconds
                double amplitude = sineWave[i];
                writer.write(String.format("%d,%.3f,%.6f", i, timeMs, amplitude));
                writer.newLine();
            }

            System.out.println("Sine wave data written to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}