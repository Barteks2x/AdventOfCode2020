package day15;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main15 {
    public static void main(String[] args) throws IOException {
        long t = System.nanoTime();
        var in = Files.readAllLines(Paths.get("in15.txt")).stream().flatMap(x -> Arrays.stream(x.split(","))).mapToInt(Integer::parseInt).toArray();
        int[] lastTurn = new int[30000000];

        int lastNum = in[0];
        for (int i = 2; i <= 30000000; i++) {
            int num = i <= in.length ? in[i - 1] : i - (lastTurn[lastNum] == 0 ? i : lastTurn[lastNum]);
            lastTurn[lastNum] = i;
            lastNum = num;
            if (i == 2020 || i == 30000000) {
                System.out.println(lastNum);
            }
        }
        System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t));
    }
}
