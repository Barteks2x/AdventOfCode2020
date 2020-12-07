package day7;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

public class Main7 {
    record Rule(String color, Map<String, Integer> contents) {
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("in7.txt")).toArray(String[]::new);

        var rules = new ArrayList<Rule>();
        for (var line : lines) {
            var split = line.split("\\s?bags?\\.?,?\\s?(contain\\s?)?");
            var container = split[0];
            if (split.length == 2 && split[1].equals("no other")) {
                rules.add(new Rule(container, new HashMap<>()));
                continue;
            }
            Map<String, Integer> contents = new HashMap<>();
            for (int i = 1; i < split.length; i++) {
                int s = split[i].indexOf(' ');
                int value = Integer.parseInt(split[i].substring(0, s));
                String color = split[i].substring(s + 1);
                contents.put(color, value);
            }
            rules.add(new Rule(container, contents));
        }
        record Entry1(Rule rule, String content) {
        }
        var entries1 = rules.stream().flatMap(x -> x.contents.keySet().stream().map(v -> new Entry1(x, v)))
                .collect(groupingBy(x -> x.content, mapping(x -> x.rule, toList())));

        var rules1 = new HashSet<>();
        var toProcess1 = new ArrayDeque<>(entries1.getOrDefault("shiny gold", new ArrayList<>()));
        while (!toProcess1.isEmpty()) {
            Rule x = toProcess1.pop();
            rules1.add(x);
            List<Rule> newRules = entries1.get(x.color);
            if (newRules != null)
                toProcess1.addAll(newRules);
        }
        System.out.println(rules1.size());

        var entries2 = rules.stream().collect(Collectors.toMap(x -> x.color, x -> x));
        int totalBags = 0;

        record Entry2(String color, int amount) {
        }
        var toProcess2 = new ArrayDeque<Entry2>();
        toProcess2.add(new Entry2("shiny gold", 1));
        while (!toProcess2.isEmpty()) {
            Entry2 x = toProcess2.pop();
            Rule r = entries2.get(x.color);
            totalBags += r.contents.values().stream().mapToInt(b -> b).sum() * x.amount;
            r.contents.keySet().forEach(b -> toProcess2.add(new Entry2(b, x.amount * r.contents.get(b))));
        }
        System.out.println(totalBags);
    }
}
