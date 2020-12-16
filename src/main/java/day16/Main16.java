package day16;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class Main16 {
    record Rule(int min, int max) implements IntPredicate {
        @Override
        public boolean test(int value) {
            return value >= min && value <= max;
        }

        public static Rule parse(String s) {
            var parts = s.split("-");
            return new Rule(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    record Field(String name, Rule[] rules) implements IntPredicate {
        @Override
        public boolean test(int value) {
            return Arrays.stream(rules).anyMatch(r -> r.test(value));
        }

        public static Field parse(String s) {
            var name = s.substring(0, s.indexOf(":"));
            var rules = Arrays.stream(s.substring(s.indexOf(":") + 2).split("\\sor\\s")).map(Rule::parse).toArray(Rule[]::new);
            return new Field(name, rules);
        }
    }

    public static void main(String[] args) throws IOException {
        var in = String.join("\n", Files.readAllLines(Paths.get("in16.txt"))).split("\n\n");
        var fields = Arrays.stream(in[0].split("\n")).map(Field::parse).toArray(Field[]::new);
        int[] myTicket = Arrays.stream(in[1].split("\n")[1].split(",")).mapToInt(Integer::parseInt).toArray();
        String[] nearbyStrings = in[2].split("\n");
        int[][] nearbyTickets = Arrays.stream(nearbyStrings, 1, nearbyStrings.length).map(x -> Arrays.stream(x.split(",")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);

        System.out.println(Arrays.stream(nearbyTickets).flatMapToInt(Arrays::stream).filter(i -> Arrays.stream(fields).noneMatch(f -> f.test(i))).sum());

        Map<Field, Integer> fieldIds = new HashMap<>();

        while (fieldIds.size() != fields.length) {
            for (Field field : fields) {
                if (fieldIds.containsKey(field)) {
                    continue;
                }
                BitSet possibleMatches = new BitSet(fields.length);
                possibleMatches.set(0, fields.length);
                fieldIds.values().forEach(possibleMatches::clear);
                for (int[] ticket : nearbyTickets) {
                    if (Arrays.stream(ticket).anyMatch(i -> Arrays.stream(fields).noneMatch(f -> f.test(i)))) {
                        continue;
                    }
                    for (int ticketField = 0; ticketField < ticket.length; ticketField++) {
                        if (possibleMatches.get(ticketField) && !field.test(ticket[ticketField])) {
                            possibleMatches.clear(ticketField);
                        }
                    }
                }
                if (possibleMatches.stream().count() == 1) {
                    fieldIds.put(field, possibleMatches.nextSetBit(0));
                }
            }
        }

        System.out.println(fieldIds.entrySet().stream().filter(e -> e.getKey().name.startsWith("departure"))
                .mapToLong(e -> myTicket[e.getValue()]).reduce(1L, (a, b) -> a * b));
    }
}
