package day2;

import java.nio.file.*;
import java.util.function.*;

public class Main2 {

    record PasswordEntry(String password, char letter, int num1, int num2) {
    }

    public static void main(String[] args) throws Throwable {
        var lines = Files.readAllLines(Paths.get("in2.txt")).toArray(new String[0]);
        int validCount = countValidPasswords(lines, Main2::valid);
        System.out.println(validCount);
        int validCount2 = countValidPasswords(lines, Main2::validNew);
        System.out.println(validCount2);
    }

    private static int countValidPasswords(String[] lines, Predicate<PasswordEntry> tester) {
        int count = 0;
        for (var line : lines) {
            var parts = line.split("[\\s:]+");
            var numbers = parts[0].split("-");
            var min = Integer.parseInt(numbers[0]);
            var max = Integer.parseInt(numbers[1]);
            var letter = parts[1].charAt(0);
            var password = parts[2];
            if (tester.test(new PasswordEntry(password, letter, min, max))) {
                count++;
            }
        }
        return count;
    }

    private static boolean valid(PasswordEntry entry) {
        var noChar = entry.password.replaceAll("" + entry.letter, "");
        int count = entry.password.length() - noChar.length();
        return count >= entry.num1 && count <= entry.num2;
    }

    private static boolean validNew(PasswordEntry entry) {
        int pos1 = entry.num1 - 1;
        int pos2 = entry.num2 - 1;
        int c1 = entry.password.charAt(pos1) == entry.letter ? 1 : 0;
        int c2 = entry.password.charAt(pos2) == entry.letter ? 1 : 0;
        return c1 + c2 == 1;
    }
}
