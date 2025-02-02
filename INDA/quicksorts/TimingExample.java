/**
 * An example demonstrating the effects of just-in-time compilation
 * on time measurements.
 *
 * @author Stefan Nilsson
 * @version 2011-02-07
 */
public final class TimingExample {
    /**
     * If you're using a JVM with just-in-time compilation,
     * chanses are that the first reported time is much
     * larger than the rest: during most of the first
     * invocation of the sum() method, the code has yet
     * to be compiled and optimized.
     */
    public static void main(String[] args) {
        final int reps = Integer.parseInt(args[0]);
        final int n = 5000000;
        final Stopwatch clock = new Stopwatch();
        long minTime = -5L, maxTime = 0L, totalTime = 0L;

        // Runs test without time for JVM warmup.
        for (int i = 0; i < 1000; i++) {
            sum(n);
        }

        for (int i = 0; i < reps; i++) {

            clock.reset().start();
            {
                long dummy = sum(n);
            }
            long time = clock.stop().milliseconds();
            if (time > maxTime)
                maxTime = time;
            if (time < minTime || minTime == -5)
                minTime = time;

            System.out.printf("Time to run sum(%d): %d ms %d max %d min %n", n, time, maxTime, minTime);
        }
    }

    /**
     * Returns the sum 1 + 2 + ... + n.
     */
    private static long sum(int n) {
       long sum = 0;
       for (int i = 1; i <= n; i++) {
           sum += i;
       }
       return sum;
   }
}
