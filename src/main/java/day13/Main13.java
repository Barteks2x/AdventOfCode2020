package day13;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main13 {
    public static void main(String[] args) throws IOException {
        var in = Files.readAllLines(Paths.get("in13.txt")).toArray(String[]::new);
        var minTime = Long.parseLong(in[0]);
        record Entry(long id, long time) {
        }
        var min = Arrays.stream(in[1].split(",")).filter(x -> !x.equals("x")).mapToLong(Long::parseLong)
                .mapToObj(x -> new Entry(x, x - (minTime % x))).min(Comparator.comparingLong(x -> x.time)).orElseThrow();
        System.out.println(min.id * min.time);

        var ids = Arrays.stream(in[1].split(",")).mapToInt(x -> x.equals("x") ? -1 : Integer.parseInt(x)).toArray();
        record Entry2(int id, int offset) {
        }
        var entries = IntStream.range(0, ids.length).filter(i -> ids[i] >= 0).mapToObj(i -> new Entry2(ids[i], i)).toArray(Entry2[]::new);

        long time = -1;
        long step = 1;
        long stepIdx = -1;

        search:
        while (true) {
            time += step;
            for (int j = 0; j < entries.length; j++) {
                long m = (time + entries[j].offset) % entries[j].id;
                if (m != 0) {
                    continue search;
                }
                if (j > stepIdx) {
                    stepIdx = j;
                    step *= entries[j].id;
                }
            }
            System.out.println(time);
            break;
        }
    }
}
