package day5;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main5 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("in5.txt")).toArray(String[]::new);
        System.out.println(Arrays.stream(lines).mapToInt(Main5::toBinary).max().orElse(-1));
        BitSet exists = new BitSet(1024);
        Arrays.stream(lines).map(Main5::toBinary).forEach(exists::set);
        var value = exists.stream().filter(i -> exists.get(i) && !exists.get(i+1) && exists.get(i+2)).findAny().orElse(-2) + 1;
        System.out.println(value);
    }

    private static int toBinary(String s) {
        return Integer.parseInt(s.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1'), 2);
    }
}
