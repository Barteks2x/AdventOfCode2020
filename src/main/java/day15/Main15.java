package day15;

import it.unimi.dsi.fastutil.ints.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main15 {
    public static void main(String[] args) throws IOException {
        var in = Files.readAllLines(Paths.get("in15.txt")).stream().flatMap(x -> Arrays.stream(x.split(","))).mapToInt(Integer::parseInt).toArray();
        Int2IntMap lastTurn = new Int2IntOpenHashMap();

        int lastNum = -1;
        for (int i = 1; i <= 30000000; i++) {
            int num = i <= in.length ? in[i - 1] : i - lastTurn.getOrDefault(lastNum, i);
            lastTurn.put(lastNum, i);
            lastNum = num;
            if (i == 2020 || i == 30000000) {
                System.out.println(lastNum);
            }
        }
    }
}
