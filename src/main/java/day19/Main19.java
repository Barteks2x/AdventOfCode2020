package day19;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Main19 {
    public static void main(String[] args) throws IOException {
        var in = String.join("\n", Files.readAllLines(Paths.get("in19.txt"))).split("\n\n");
        var rules = Arrays.stream(in[0].split("\n"))
                .collect(Collectors.toMap(e -> Integer.parseInt(e.split(": ")[0]), e -> e.split(": ")[1]));
        var data = in[1].split("\n");

        String regex = toRegex(0, rules, false);
        System.out.println(regex);
        Pattern pattern = Pattern.compile(regex);
        System.out.println(Arrays.stream(data).map(pattern::matcher).filter(Matcher::matches).count());

        rules.put(8, "42 | 42 8");
        rules.put(11, "42 31 | 42 11 31");
        //rules.put(42, "\"a\"");
        //rules.put(31, "\"b\"");

        regex = toRegex(0, rules, false);
        System.out.println(regex);
        Pattern pattern2 = Pattern.compile(regex);
        // 343 wrong
        // 340 wrong
        System.out.println(Arrays.stream(data).filter(x -> pattern2.matcher(x).matches()).count());
    }

    private static String toRegex(int ruleId, Map<Integer, String> rules, boolean handlingRecursive) {
        String rule = rules.get(ruleId);
        if (rule.matches("\".\"")) {
            return String.valueOf(rule.charAt(1));
        }
        String[] originalParts = rule.split("\\s*\\|\\s*");
        String[] orParts = handlingRecursive ?
                Arrays.stream(originalParts).filter(x -> !x.contains(String.valueOf(ruleId))).toArray(String[]::new)
                : originalParts;
        StringBuilder regex = new StringBuilder();
        if (orParts.length > 1)
            regex.append('(');
        for (int i = 0; i < orParts.length; i++) {
            String orPart = orParts[i];
            if (i != 0) {
                regex.append('|');
            }
            String[] rulesToMatch = orPart.split("\\s+");
            if (Arrays.stream(rulesToMatch).anyMatch(s -> s.equals(String.valueOf(ruleId)))) {
                StringBuilder pre = new StringBuilder();
                StringBuilder main = new StringBuilder();
                StringBuilder post = new StringBuilder();
                boolean foundSelf = false;
                for (String toMatch : rulesToMatch) {
                    int nextid = Integer.parseInt(toMatch);
                    if (nextid == ruleId) {
                        foundSelf = true;
                        main.append(toRegex(nextid, rules, true));
                    } else {
                        (foundSelf ? post : pre).append(toRegex(nextid, rules, false));
                    }
                }
                regex.append('(');
                for (int j = 0; j < 10; j++) {
                    if (j == 0) {
                        regex.append(main);
                        continue;
                    }
                    regex.append('|');
                    regex.append("(").append(pre).append("){").append(j).append("}").append(main);
                    if (post.length() != 0) {
                        regex.append('(').append(post).append("){").append(j).append("}");
                    }
                }
                regex.append(')');
            } else {
                for (String toMatch : rulesToMatch) {
                    regex.append(toRegex(Integer.parseInt(toMatch), rules, false));
                }
            }
        }
        if (orParts.length > 1)
            regex.append(')');
        return regex.toString();
    }
}
