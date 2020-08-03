package util.recorder;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import algorithm.sort.Insertion;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


public class BenchmarkWatch<Input> {

    public static final int DEFAULT_LIMIT = 0xffff;
    public static final int DEFAULT_GROW_FACTOR = 2;

    public final BiFunction<Integer, Integer, Integer> LINEAR_GROW =
            Integer::sum;

    public final BiFunction<Integer, Integer, Integer> DEFAULT_GROW = LINEAR_GROW;


    protected BiFunction<Integer, Input, Void> target;
    protected Function<Integer, Input> supplier;

    protected BenchmarkWatch() {

    }

    /**
     * get an instance of benchmark watch
     * test subject is a BiFunction
     * the first param is an integer represent input size
     * the second is an object represent input
     * in order to keep consistence of input data
     * an input supplier function must be supplied
     * the supplier take an integer as input size
     * return input object represent input
     *
     * @param target test subject, whose input is given by a supplier
     * @param supplier supply input for test function
     * @param <Input> input type
     * @return instance of benchmark watch
     */
    public static <Input> BenchmarkWatch<Input> of(
            BiFunction<Integer, Input, Void> target,
            Function<Integer, Input> supplier) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(supplier);

        BenchmarkWatch<Input> watch =
                new BenchmarkWatch<>();
        watch.target = target;
        watch.supplier = supplier;
        return watch;
    }

    /**
     *
     * @param times how many times need to repeated per input
     * @param factor gap between input size sequence
     * @param minimumInputSize minimum input size
     * @param maximumInputSize maximum input size(included)
     * @return a map of input size and runtime duration
     */
    public Map<Integer, Double> watch(int times,
                                      int factor,
                                      int minimumInputSize,
                                      int maximumInputSize) {
        TreeMap<Integer, Double> records = new TreeMap<>();
        for(int i = minimumInputSize;
            i <= maximumInputSize;
            i = DEFAULT_GROW.apply(i, factor)) {
            Input input = supplier.apply(i);
            double time = run(times, i, input, target);
            records.put(i, time);
        }
        return records;
    }

    /**
     * @param times repeated times
     * @param inputSize size of input
     * @return runtime
     */
    public double watch(int times, int inputSize) {
        return run(times,
                inputSize,
                supplier.apply(inputSize),
                target);
    }

    private double run(int times,
                             int inputSize,
                             Input input,
                             BiFunction<Integer, Input, Void> target) {
        long start = System.nanoTime();
        for(int i = 0; i < times; i++) {
            target.apply(inputSize, input);
        }
        long end = System.nanoTime();

        return (double)(end - start) / times;
    }

    public static class Test {

        public static void main(String[] args) {
            Map<Integer, Double> records = BenchmarkWatch.of(
                    Test::run,
                    Test::supply)
                    .watch(1, 10, 100, 10000);
            System.out.println("records: " + records);

            double[] keys = new double[records.size()];
            double[] values = new double[records.size()];

            var context = new Object() {
                int counter = 0;
            };

            records.forEach((key, value) -> {
                keys[context.counter] = key;
                values[context.counter] = value;
                context.counter++;
            });

            plot(keys, values);
        }

        public static Void run(int size, Object[] input) {
            Insertion.sort(input, (a, b) -> Integer.compare((int)a, (int)b));
            Arrays.sort(input);
            return null;
        }

        public static Object[] supply(int size) {
            return new Random().ints(size).boxed().toArray();
        }

        public static void plot(double[] x, double[] y) {

            XYChart chart = QuickChart
                    .getChart("Sample Chart", "X", "Y", "y(x)", x, y);
            new SwingWrapper(chart).displayChart();
        }

    }

}
