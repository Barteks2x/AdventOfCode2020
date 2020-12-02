package day2;

import java.nio.file.*;
import java.util.function.*;

public class Main2 {

    record PasswordEntry(String password, char letter, int num1, int num2) {
    }

    public static void main(String[] args) throws Throwable {
        var lines = Files.readAllLines(Paths.get("in2.txt")).toArray(new String[0]);
        System.out.println(countValidPasswords(lines, entry -> {
                    int count = entry.password.length() - entry.password.replaceAll("" + entry.letter, "").length();
                    return count >= entry.num1 && count <= entry.num2;
                })
        );
        System.out.println(countValidPasswords(lines, entry ->
                (entry.password.charAt(entry.num1 - 1) == entry.letter) ^
                        (entry.password.charAt(entry.num2 - 1) == entry.letter))
        );
    }

    private static int countValidPasswords(String[] lines, Predicate<PasswordEntry> tester) {
        int count = 0;
        for (var line : lines) {
            var parts = line.split("[\\s:]+");
            var numbers = parts[0].split("-");
            if (tester.test(new PasswordEntry(parts[2], parts[1].charAt(0), Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])))) {
                count++;
            }
        }
        return count;
    }
}
