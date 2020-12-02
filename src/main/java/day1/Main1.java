package day1;

import java.nio.file.*;

public class Main1 {
    public static void main(String... args) throws Throwable {
        var n = Files.readAllLines(Paths.get("in1.txt")).stream().mapToInt(Integer::parseInt).toArray();
        part1(n);
        part2(n);
    }

    private static void part1(int[] n) {
        System.out.println("Part1:");
        for (int i = 0; i < n.length; i++) {
            for (int j = i + 1; j < n.length; j++) {
                if (n[i] + n[j] == 2020) {
                    System.out.println(n[i] * n[j]);
                }
            }
        }
    }

    private static void part2(int[] n) {
        System.out.println("Part2:");
        for (int i = 0; i < n.length; i++) {
            for (int j = i + 1; j < n.length; j++) {
                for (int k = j + 1; k < n.length; k++) {
                    if (n[i] + n[j] + n[k] == 2020) {
                        System.out.println(n[i] * n[j] * n[k]);
                    }
                }
            }
        }
    }
}
