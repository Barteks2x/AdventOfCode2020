package day9;

import it.unimi.dsi.fastutil.longs.*;

import java.io.*;
import java.nio.file.*;

public class Main9 {
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("in9.txt")).stream().filter(x -> !x.isEmpty()).mapToLong(Long::parseLong).toArray();

        Long2IntMap previousCounts = new Long2IntOpenHashMap();
        final int nPrevious = 25;

        long part1 = -1;
        for (int i = 0; i < input.length; i++) {
            long newNum = input[i];
            if (i >= nPrevious) {
                boolean found = previousCounts.keySet().stream().filter(x -> x != newNum - x)
                        .mapToLong(x -> newNum - x).anyMatch(previousCounts::containsKey);
                if (!found) {
                    part1 = input[i];
                    break;
                }
            }
            if (i - nPrevious >= 0) {
                long toRemove = input[i - nPrevious];
                previousCounts.compute(toRemove, (n, count) -> count - 1);
                if (previousCounts.get(toRemove) <= 0) {
                    previousCounts.remove(toRemove);
                }
            }
            previousCounts.compute(newNum, (n, count) -> count == null ? 1 : count + 1);
        }
        System.out.println(part1);

        int start = 0;
        long total = 0;
        for (int i = 0; i < input.length; i++) {
            total += input[i];
            while (total > part1) {
                total -= input[start];
                start++;
            }
            if (total == part1 && i - start >= 2) {
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                for (int j = start; j < i; j++) {
                    min = Math.min(min, input[j]);
                    max = Math.max(max, input[j]);
                }
                System.out.println(min + max);
                break;
            }
        }
    }
}
