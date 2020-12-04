package day4;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Main4 {

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("in4.txt")).toArray(String[]::new);
        var entries = new ArrayList<List<String>>();
        var e = new ArrayList<String>();
        for (String line : lines) {
            if (line.isEmpty()) {
                entries.add(e);
                e = new ArrayList<>();
                continue;
            }
            e.addAll(Arrays.asList(line.split("\\s")));
        }
        if (!e.isEmpty()) {
            entries.add(e);
        }

        var required = new String[]{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
        var count = entries.stream().filter(entry -> validate(entry, required)).count();
        System.out.println(count);

        var validators = new HashMap<String, Predicate<String>>();
        validators.put("byr", n -> validateNumber(n, 1920, 2002));
        validators.put("iyr", n -> validateNumber(n, 2010, 2020));
        validators.put("eyr", n -> validateNumber(n, 2020, 2030));
        validators.put("hgt", n -> (n.endsWith("cm") && validateNumber(n.substring(0, n.length() - 2), 150, 193)) ||
                (n.endsWith("in") && validateNumber(n.substring(0, n.length() - 2), 59, 76)));
        validators.put("hcl", n -> n.matches("#[0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f][0-9a-f]"));
        validators.put("ecl", n -> n.matches("(amb|blu|brn|gry|grn|hzl|oth)"));
        validators.put("pid", n -> n.matches("\\d\\d\\d\\d\\d\\d\\d\\d\\d"));
        var count2 = entries.stream().filter(entry -> validateStrict(entry, validators)).count();
        System.out.println(count2);
    }

    private static boolean validateNumber(String num, int min, int max) {
        try {
            int n = Integer.parseInt(num);
            return n >= min && n <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validate(List<String> entry, String[] required) {
        var existing = entry.stream().map(x -> x.split(":")[0]).collect(Collectors.toSet());
        return Arrays.stream(required).allMatch(existing::contains);
    }

    private static boolean validateStrict(List<String> entry, Map<String, Predicate<String>> required) {
        var existing = entry.stream().collect(Collectors.toMap(x -> x.split(":")[0], x -> x.split(":")[1]));
        return required.keySet().stream().allMatch(key -> existing.containsKey(key) && required.get(key).test(existing.get(key)));
    }
}
