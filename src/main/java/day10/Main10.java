package day10;

import java.io.*;
import java.nio.file.*;

public class Main10 {
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("in10.txt")).stream().mapToInt(Integer::parseInt).sorted().toArray();

        var list = new int[input.length + 2];
        list[0] = 0;
        list[list.length - 1] = input[input.length - 1] + 3;
        System.arraycopy(input, 0, list, 1, input.length);

        int[] diffCounts = new int[4];
        for (int i = 0; i < list.length - 1; i++) {
            diffCounts[list[i + 1] - list[i]]++;
        }
        System.out.println(diffCounts[1] * diffCounts[3]);

        System.out.println(countPossibilities(list, 0));
    }

    private static long countPossibilities(int[] list, int start) {
        for (int i = start; i < list.length - 2; i++) {
            if (list[i + 2] - list[i] <= 3) {
                int[] n = new int[list.length - 1];
                System.arraycopy(list, 0, n, 0, i + 1);
                System.arraycopy(list, i + 2, n, i + 1, list.length - (i + 2));
                if (i < list.length - 3 && list[i + 3] - list[i + 1] > 3) {
                    return 2 * countPossibilities(n, i);
                } else {
                    return countPossibilities(list, i + 1) + countPossibilities(n, i);
                }
            }
        }
        return 1;
    }
}
