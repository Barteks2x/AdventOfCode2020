package day6;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main6 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("in6.txt")).toArray(String[]::new);
        var v = Arrays.stream(lines).map(x -> x.chars().boxed().collect(Collectors.toSet())).collect(Collectors.toList());
        v.add(new HashSet<>());
        var p1set = new HashSet<Integer>();
        Set<Integer> p2set = null;
        int total = 0;
        int total2 = 0;
        for (Set<Integer> s : v) {
            p1set.addAll(s);
            if (p2set == null) {
                p2set = new HashSet<>(s);
            } else if (!s.isEmpty()){
                p2set.retainAll(s);
            }
            if (s.isEmpty()) {
                total += p1set.size();
                total2 += p2set.size();
                p1set.clear();
                p2set = null;
            }
        }
        System.out.println(total);
        System.out.println(total2);
    }
}
