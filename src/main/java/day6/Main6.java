package day6;

import util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main6 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("in6.txt")).toArray(String[]::new);
        var v = Arrays.stream(lines).map(x -> x.chars().boxed()
                .collect(Collectors.toSet()))
                .collect(Utils.splittingBy(Set::isEmpty));
        System.out.println(v.stream().map(x -> x.stream().reduce((a, b) -> {
            var chars = new HashSet<>(a);
            chars.addAll(b);
            return chars;
        }).orElse(new HashSet<>())).mapToInt(Set::size).sum());
        System.out.println(v.stream().map(x -> x.stream().reduce((a, b) -> {
            var chars = new HashSet<>(a);
            chars.retainAll(b);
            return chars;
        }).orElse(new HashSet<>())).mapToInt(Set::size).sum());
    }
}
